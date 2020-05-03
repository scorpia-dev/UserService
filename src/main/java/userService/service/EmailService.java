package userService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import userService.model.Email;
import userService.model.User;
import userService.repositories.EmailRepository;
import userService.repositories.UserRepository;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.Positive;

@Service
public class EmailService {

	@Autowired
	EmailRepository emailRepository;

	@Autowired
	UserRepository userRepository;

	public Email addNewEmail(Email email, int userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException("the user with id " + userId + " was not found"));

		email.setUser(user);

		return emailRepository.save(email);
	}

	public Email updateEmail(Email mail, @Positive int mailId) {
		Email existingMail = emailRepository.findById(mailId)
				.orElseThrow(() -> new EntityNotFoundException("the email with id " + mailId + " was not found"));

		existingMail.setMail(mail.getMail());
		return emailRepository.save(existingMail);
	}

	public Email getEmailById(int id) {
		return emailRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("the email with id " + id + " was not found"));
	}

}
