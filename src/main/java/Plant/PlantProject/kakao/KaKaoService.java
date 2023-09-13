package Plant.PlantProject.kakao;

import Plant.PlantProject.Entity.Member;
import Plant.PlantProject.Entity.Role;
import Plant.PlantProject.config.JwtTokenUtil;
import Plant.PlantProject.repository.MemberRepository;
import Plant.PlantProject.repository.RoleRepository;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.parser.JSONParser;
import com.nimbusds.jose.shaded.json.parser.ParseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static Plant.PlantProject.Entity.SocialLogin.KAKAO;

@Service
@RequiredArgsConstructor
@Slf4j
public class KaKaoService extends DefaultOAuth2UserService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenUtil jwtTokenUtil;
    public String getToken(String code) throws IOException {
        // 인가코드로 토큰받기
        String host = "https://kauth.kakao.com/oauth/token";
        URL url = new URL(host);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        String token = "";
        try {
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true); // 데이터 기록 알려주기

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=9f8bf0134e25a65f80e7b9849efc41ae");
            sb.append("&redirect_uri=http://54.180.44.32:3000/api/oauth2/login/kakao");
            sb.append("&code=" + code);

            bw.write(sb.toString());
            bw.flush();

            int responseCode = urlConnection.getResponseCode();
            System.out.println("responseCode = " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("result = " + result);
            // json parsing
            JSONParser parser = new JSONParser();
            JSONObject elem = (JSONObject) parser.parse(result);

            String access_token = elem.get("access_token").toString();
            String refresh_token = elem.get("refresh_token").toString();
            System.out.println("refresh_token = " + refresh_token);
            System.out.println("access_token = " + access_token);

            token = access_token;

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return token;
    }
    public Map<String, Object> getUserInfo(String access_token) throws IOException {
        String host = "https://kapi.kakao.com/v2/user/me";
        Map<String, Object> result = new HashMap<>();
        try {
            URL url = new URL(host);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Bearer " + access_token);
            urlConnection.setRequestMethod("GET");
            int responseCode = urlConnection.getResponseCode();
            System.out.println("responseCode = " + responseCode);


            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            String res = "";
            while((line=br.readLine())!=null)
            {
                res+=line;
            }
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(res);
            JSONObject kakao_account = (JSONObject) obj.get("kakao_account");
            JSONObject properties = (JSONObject) obj.get("properties");


            String nickname = properties.get("nickname").toString();
            String email = kakao_account.get("email").toString();
            String password="1234";

            String username = nickname;
            result.put("nickname", nickname);
            result.put("email", email);
            result.put("access_token", access_token);
            result.put("username", username);



            if (!memberRepository.existsByEmail(email)) {
                // 존재하지 않는 회원인 경우 새로 생성
                Member member = new Member(email,password, nickname,  KAKAO, username);
                member.setPassword(passwordEncoder.encode(member.getPassword()));
                member.getRole().add(roleRepository.findByName("ROLE_USER"));
                memberRepository.save(member);
                result.put("id", member.getId());
            } else {
                // 이미 존재하는 회원이면 그냥 넘어가거나 다른 로직 수행 (예: 로그 작성 등)
                Member byUsername = memberRepository.findByUsername(username);
                result.put("id", byUsername.getId());
                log.info("Existing member detected with email: {}", email);
            }
            br.close();


        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String getAgreementInfo(String access_token)
    {
        String result = "";
        String host = "https://kapi.kakao.com/v2/user/scopes";
        try{
            URL url = new URL(host);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Authorization", "Bearer "+access_token);

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            while((line=br.readLine())!=null)
            {
                result+=line;
            }

            int responseCode = urlConnection.getResponseCode();
            System.out.println("responseCode = " + responseCode);

            // result is json format
            br.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    public Role saveRole(Role role) {
        log.info("Saving new role {} to the db", role.getName());
        return roleRepository.save(role);
    }

    public void grantRoleToUser(String username, String roleName) {
        log.info("Grant new role {} to {}", roleName, username);
        Member member = memberRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);

        member.getRole().add(role);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Member user = memberRepository.findByUsername(userName);
        if(user == null) {
            log.error("User not found in the database {}", userName);
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database: {}", userName);
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRole().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        log.info(user.getUsername());
        return new User(user.getUsername(),user.getPassword(), authorities);
    }
}
