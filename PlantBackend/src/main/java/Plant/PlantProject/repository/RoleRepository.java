package Plant.PlantProject.repository;

import Plant.PlantProject.Entity.Member;
import Plant.PlantProject.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String roleName);
}
