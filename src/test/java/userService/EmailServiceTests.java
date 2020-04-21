package userService;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import userService.model.Email;
import userService.model.PhoneNumber;
import userService.model.User;
import userService.repositories.UserRepository;
import userService.service.EmailService;
import userService.service.UserService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EmailServiceTests {

	@Autowired
	UserService userService;

	@Autowired
	EmailService emailService;

	@Autowired
	UserRepository userRepository;

	private List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();
	private List<Email> emails = new ArrayList<Email>();

	User user;

	@BeforeEach
	public void setUp() {
		user = new User("Nick", "Prendergast", emails, phoneNumbers);
	}

	@Transactional
	@Test
	public void addNewEmailTest() {

		Email email = new Email("test@mail.com", user);

		User newUser = userRepository.save(user);
		newUser.getEmails().add(emailService.addNewEmail(email, newUser.getId()));

		assertTrue(newUser.getEmails().get(0).getMail().equals("test@mail.com"));
		assertTrue(newUser.getEmails().size() == 1);
	}

	@Transactional
	@Test
	public void addMultipleNewEmailsTest() {

		Email email = new Email("test@mail.com", user);
		Email email1 = new Email("test2@mail.com", user);

		User newUser = userRepository.save(user);

		newUser.getEmails().add(emailService.addNewEmail(email, newUser.getId()));
		newUser.getEmails().add(emailService.addNewEmail(email1, newUser.getId()));

		assertTrue(newUser.getEmails().get(0).getMail().equals("test@mail.com"));
		assertTrue(newUser.getEmails().get(1).getMail().equals("test2@mail.com"));
		assertTrue(newUser.getEmails().size() == 2);

	}

	@Transactional
	@Test
	public void updateEmailTest() {
		Email email = new Email("test@mail.com", user);
		Email email1 = new Email("updated@mail.com", user);

		User newUser = userRepository.save(user);
		newUser.getEmails().add(emailService.addNewEmail(email, newUser.getId()));
		int mailId = newUser.getEmails().get(0).getId();

		emailService.updateEmail(email1, mailId);

		assertTrue(newUser.getEmails().get(0).getMail().equals("updated@mail.com"));
		assertTrue(newUser.getEmails().size() == 1);
	}

}
