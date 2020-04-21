package userService.controller;

import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import userService.model.User;
import userService.service.UserService;

@RequestMapping("/user")
@RestController
@Validated
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping
	public User createUser(@Validated @RequestBody User user) {
		return userService.createUser(user);
	}

	@DeleteMapping("/id/{userId}")
	public String deleteUser(@PathVariable @Positive int userId) {
		return userService.deleteUser(userId);
	}

	@GetMapping("/id/{userId}")
	public User getUserById(@PathVariable @Positive int userId) {
		return userService.getUserById(userId);
	}

	@GetMapping("/name/{firstName}/{lastName}")
	public User getUserByName(@PathVariable String firstName, @PathVariable String lastName) {
		return userService.getUserByName(firstName, lastName);
	}
}
