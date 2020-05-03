package userService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import userService.model.Email;
import userService.service.EmailService;

import javax.validation.constraints.Positive;

@RequestMapping("/email")
@RestController
@Validated
public class EmailController {

	@Autowired
	EmailService emailService;

	@PutMapping("/update/{mailId}")
	public Email updateExistingMail(@PathVariable @Positive int mailId, @RequestBody @Validated Email mail) {
		return emailService.updateEmail(mail, mailId);
	}

	@PutMapping("/add/{userId}")
	public Email addNewEmail(@PathVariable @Positive int userId, @Validated @RequestBody Email email) {
		return emailService.addNewEmail(email, userId);
	}

}
