package Plant.PlantProject.google;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoogleRepository extends JpaRepository<GoogleMember, Long> {
    Optional<GoogleMember> findByEmail(String email);

}
