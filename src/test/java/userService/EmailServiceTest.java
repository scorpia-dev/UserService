package userService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import userService.model.Email;
import userService.model.PhoneNumber;
import userService.model.User;
import userService.repositories.UserRepository;
import userService.service.EmailService;
import userService.service.UserService;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class EmailServiceTest {

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

	@Autowired
	EntityManager entityManager;

	@Transactional
	@Test
	public void addNewEmailTest() {
		User user1 = userService.createUser(user);
		Email email = new Email("test@mail.com", user1);
		emailService.addNewEmail(email, user1.getId());

		entityManager.refresh(user1);

		assertTrue(user1.getEmails().get(0).getMail().equals("test@mail.com"));
		assertTrue(user1.getEmails().size() == 1);
	}

	@Transactional
	@Test
	public void addMultipleNewEmailsTest() {
		Email email = new Email("test@mail.com", user);
		Email email1 = new Email("test2@mail.com", user);

		User newUser = userRepository.save(user);

		emailService.addNewEmail(email, newUser.getId());
		emailService.addNewEmail(email1, newUser.getId());
		entityManager.refresh(newUser);

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
		emailService.addNewEmail(email, newUser.getId());
		entityManager.refresh(newUser);

		emailService.updateEmail(email1, 1);

		assertTrue(newUser.getEmails().get(0).getMail().equals("updated@mail.com"));
		assertTrue(newUser.getEmails().size() == 1);
	}

}
