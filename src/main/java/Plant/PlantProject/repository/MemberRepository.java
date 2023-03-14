package Plant.PlantProject.repository;

import Plant.PlantProject.Entity.Member;
import Plant.PlantProject.dto.MemberDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByName(String name);

    Member findByUserId(String userId);
}
