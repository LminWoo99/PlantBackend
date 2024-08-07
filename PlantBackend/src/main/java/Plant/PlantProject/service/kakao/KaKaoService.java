package Plant.PlantProject.service.kakao;

import Plant.PlantProject.common.config.auth.JwtTokenUtil;
import Plant.PlantProject.domain.Entity.Member;
import Plant.PlantProject.common.exception.ErrorCode;
import Plant.PlantProject.repository.MemberRepository;
import Plant.PlantProject.service.user.RefreshTokenService;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.parser.JSONParser;
import com.nimbusds.jose.shaded.json.parser.ParseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static Plant.PlantProject.domain.Entity.SocialLogin.KAKAO;

@Service
@RequiredArgsConstructor
@Slf4j
public class KaKaoService extends DefaultOAuth2UserService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    @Value("${kakao.host}")
    private String host;
    @Value("${kakao.clientId}")
    private String client;
    @Value("${kakao.redirect}")
    private String redirectUrl;

    public Map<String, Object> getToken(String code) throws IOException {
        Map<String, Object> userInfo = new HashMap<>();
        // 인가코드로 토큰받기
        URL url = new URL(host);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        Map<String, Object> key = new HashMap<>();
        try {
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true); // 데이터 기록 알려주기

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id="+client);
            sb.append("&redirect_uri="+redirectUrl);
            sb.append("&code=" + code);

            bw.write(sb.toString());
            bw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            // json parsing
            JSONParser parser = new JSONParser();
            JSONObject elem = (JSONObject) parser.parse(result);

            String access_token = elem.get("access_token").toString();
            String refresh_token = elem.get("refresh_token").toString();

            key.put("access_token", access_token);
            key.put("refresh_token", refresh_token);
            br.close();
            bw.close();
            userInfo = getUserInfo(key);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return userInfo;
    }
    public Map<String, Object> getUserInfo(Map<String, Object> token) throws IOException {
        String host = "https://kapi.kakao.com/v2/user/me";
        Map<String, Object> result = new HashMap<>();
        Object access_token = token.get("access_token");
        Object refresh_token = token.get("refresh_token");

        try {
            URL url = new URL(host);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Bearer " +access_token);
            urlConnection.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            String res = "";
            while((line=br.readLine())!=null)
            {
                res+=line;
            }

            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(res);
            JSONObject kakao_account = (JSONObject) obj.get("kakao_account");
            JSONObject properties = (JSONObject) obj.get("properties");


            String nickname = properties.get("nickname").toString();
            String email = kakao_account.get("email").toString();
            String password="1234";

            String username = email;


            if (!memberRepository.existsByEmail(email)) {
                // 존재하지 않는 회원인 경우 새로 생성
                Member member=Member.builder()
                        .email(email)
                        .username(username)
                        .password(password)
                        .nickname(nickname)
                        .build();
                String accessToken = generateAndSaveTokens(result, member);

                member.encryptPassword(passwordEncoder.encode(member.getPassword()), KAKAO);


                memberRepository.save(member);
                result.put("id", member.getId());
                result.put("access_token", accessToken);
            } else {
                // 이미 존재하는 회원이면 그냥 넘어가거나 다른 로직 수행 (예: 로그 작성 등)
                Member byUsername = memberRepository.findByUsername(username).orElseThrow(ErrorCode::throwMemberNotFound);

                generateAndSaveTokens(result, byUsername);

                result.put("id", byUsername.getId());
                log.info("Existing member detected with email: {}", email);
            }
            br.close();


        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    private String generateAndSaveTokens(Map<String, Object> result, Member member) {
        String memberNo = member.getId().toString();
        String accessToken=jwtTokenUtil.generateAccessToken(memberNo);
        String refreshToken=jwtTokenUtil.generateRefreshToken(memberNo);

        refreshTokenService.saveTokenInfo(memberNo, accessToken, refreshToken);

        result.put("nickname", member.getNickname());
        result.put("email", member.getEmail());
        result.put("access_token", accessToken);
        result.put("refresh_token", refreshToken);
        result.put("username", member.getUsername());
        return accessToken;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Member user = memberRepository.findByUsername(userName).orElseThrow(ErrorCode::throwMemberNotFound);
        if(user == null) {
            log.error("User not found in the database {}", userName);
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database: {}", userName);
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        return new User(user.getUsername(),user.getPassword(), authorities);
    }
}
