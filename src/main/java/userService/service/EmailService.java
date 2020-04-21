package userService.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import userService.model.Email;
import userService.model.User;
import userService.repositories.EmailRepository;
import userService.repositories.UserRepository;

@Service
public class EmailService {

	@Autowired
	EmailRepository emailRepository;
	
	@Autowired
	UserRepository userRepository;
	
	public Email addNewEmail(Email email, int userId) {
		List<Email> emails1 = new ArrayList<Email>();
		User user1 = new User();
		
		User user = userRepository.findById(userId).orElseThrow(
				() -> new EntityNotFoundException("the user with id " + userId + " was not found"));
		
		email.setUser(user);
		Email newEmail = emailRepository.save(email);
		
		emails1.add(newEmail);
		user1.setEmails(emails1);
		return newEmail;
}
	
	public Email updateEmail(Email mail, @Positive int mailId) {
		Email existingMail = emailRepository.findById(mailId).orElseThrow(
				() -> new EntityNotFoundException("the email with id " + mailId + " was not found"));
		
		existingMail.setMail(mail.getMail());
		return emailRepository.save(existingMail);
	}
	
}
