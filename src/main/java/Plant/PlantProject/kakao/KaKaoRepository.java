package Plant.PlantProject.kakao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface KaKaoRepository extends JpaRepository<KaKaoMember, Long> {
    Optional<KaKaoMember> findByEmail(String email);
}

