package Plant.PlantProject.service.user;

import Plant.PlantProject.common.config.auth.JwtTokenUtil;
import Plant.PlantProject.domain.redis.RefreshToken;
import Plant.PlantProject.repository.redis.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository repository;
    private final JwtTokenUtil jwtUtil;

    public void saveTokenInfo(String memberNo, String accessToken, String refreshToken) {
        repository.save(new RefreshToken(memberNo, accessToken, refreshToken));
    }

    public void removeRefreshToken(String accessToken) {
        RefreshToken token = repository.findByAccessToken(accessToken)
                .orElseThrow(IllegalArgumentException::new);

        repository.delete(token);
    }
    public String republishAccessToken(String accessToken) {
        // 액세스 토큰으로 Refresh 토큰 객체를 조회
        Optional<RefreshToken> refreshToken = repository.findByAccessToken(accessToken);

        // RefreshToken이 존재하고 유효하다면 실행
        if (refreshToken.isPresent() && jwtUtil.isJwtValid(refreshToken.get().getRefreshToken())) {
            // RefreshToken 객체를 꺼내온다.
            RefreshToken resultToken = refreshToken.get();
            // 권한과 아이디를 추출해 새로운 액세스토큰을 만든다.
            String newAccessToken = jwtUtil.generateAccessToken(resultToken.getId());
            // 액세스 토큰의 값을 수정해준다.
            resultToken.updateAccessToken(newAccessToken);
            repository.save(resultToken);
            // 새로운 액세스 토큰을 반환해준다.
            return newAccessToken;
        }

        return null;
    }
}