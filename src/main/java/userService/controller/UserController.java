package userService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import userService.model.User;
import userService.service.UserService;

import javax.validation.constraints.Positive;

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
