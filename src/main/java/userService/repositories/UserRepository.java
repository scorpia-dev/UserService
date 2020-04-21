package userService.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import userService.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByFirstNameAndLastName(String firstName, String lastName);

}
