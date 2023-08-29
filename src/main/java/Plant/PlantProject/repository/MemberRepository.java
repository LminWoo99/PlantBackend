package Plant.PlantProject.repository;

import Plant.PlantProject.Entity.Member;
import Plant.PlantProject.Entity.TradeBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByUsername(String username);


    Optional<Member> findByPassword(String password);

    boolean existsByEmail(String email);

    Member findByUserId(String userId);

//    Member findByEmail(String email);
    Optional <Member> findByEmail(String email);


}
