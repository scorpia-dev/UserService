package userService.service;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import userService.model.User;
import userService.repositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	public User createUser(User user) {
		return userRepository.save(user);
	}

	public String deleteUser(int userId) {
			userRepository.delete(userRepository.findById(userId).orElseThrow(
						() -> new EntityNotFoundException("the user with id " + userId + " was not found")));
				return "user with id " + userId + " deleted";
			}
	
	
	public User getUserById(@Positive int id) {
		return userRepository.findById(id).orElseThrow(
				() -> new EntityNotFoundException("the user with id " + id + " was not found"));
	}

	public User getUserByName(String firstName, String lastName) {
		return userRepository.findByFirstNameAndLastName(firstName, lastName).orElseThrow(
				() -> new EntityNotFoundException("the user with name " +firstName +" was not found"));
	}


}
