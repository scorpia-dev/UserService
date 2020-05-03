package userService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import userService.model.Email;

@Repository
public interface EmailRepository extends JpaRepository<Email, Integer> {

}
