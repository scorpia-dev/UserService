package userService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import userService.model.PhoneNumber;

public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Integer> {

}
