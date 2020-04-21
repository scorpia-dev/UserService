package userService.controller;

import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import userService.model.Email;
import userService.service.EmailService;

@RequestMapping("/email")
@RestController
@Validated
public class EmailController {

	@Autowired
	EmailService emailService;
	
	@PutMapping("/updateEmail/{mailId}")
public Email updateExistingMail(@PathVariable @Positive int mailId, @RequestBody @Validated Email mail) {
	return emailService.updateEmail(mail, mailId);
}
	
	@PutMapping("/addNewEmail/{userId}")
	public Email addNewEmail(@PathVariable int userId, @Validated @RequestBody Email email) {
		return emailService.addNewEmail(email, userId);
	}
	
	
	
}
