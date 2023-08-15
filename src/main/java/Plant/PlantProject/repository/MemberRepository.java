package Plant.PlantProject.repository;

import Plant.PlantProject.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByUsername(String username);


    Member findByUserId(String userId);

    Member findByEmail(String email);
}
