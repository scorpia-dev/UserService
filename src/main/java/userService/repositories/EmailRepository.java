package userService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import userService.model.Email;

public interface EmailRepository extends JpaRepository<Email, Integer> {

}
