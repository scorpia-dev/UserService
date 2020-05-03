package userService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import userService.model.Email;
import userService.model.PhoneNumber;
import userService.model.User;
import userService.repositories.EmailRepository;
import userService.repositories.PhoneNumberRepository;
import userService.repositories.UserRepository;
import userService.service.EmailService;
import userService.service.PhoneNumberService;
import userService.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

@AutoConfigureTestDatabase(replace = Replace.ANY)
@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceTest {

	@Autowired
	UserService userService;

	@Autowired
	EmailService emailService;

	@Autowired
	PhoneNumberService phoneNumberService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PhoneNumberRepository phoneNumberRepository;

	@Autowired
	EmailRepository emailRepository;

	private List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();
	private List<Email> emails = new ArrayList<Email>();

	User user;

	@BeforeEach
	public void setUp() {
		user = new User("Nick", "Prendergast", emails, phoneNumbers);
	}

	@Transactional
	@Test
	public void createUserTest() {
		User newUser = userService.createUser(user);

		assertTrue(newUser.getId() == 1);
		assertTrue(newUser.getFirstName() == "Nick");
		assertTrue(newUser.getLastName() == "Prendergast");
		assertTrue(newUser.getEmails().isEmpty());
		assertTrue(newUser.getPhoneNumbers().isEmpty());

		assertTrue(userRepository.findAll().size() == 1);
	}

	@Transactional
	@Test
	public void getUserByIdTest() {
		User newUser = userService.createUser(user);
		assertTrue(userService.getUserById(newUser.getId()) == newUser);
	}

	@Transactional
	@Test
	public void getUserByNameTest() {
		User newUser = userService.createUser(user);
		assertTrue(userService.getUserByName("Nick", "Prendergast") == newUser);
	}

	@Transactional
	@Test
	public void deleteUserTest() {
		Email email = new Email("test@mail.com", user);
		PhoneNumber phoneNumber = new PhoneNumber("12345678910", user);

		User newUser = userService.createUser(user);

		Email email1 = emailService.addNewEmail(email, newUser.getId());
		PhoneNumber phoneNumber1 = phoneNumberService.addNewPhoneNumber(phoneNumber, newUser.getId());

		assertTrue(userRepository.findAll().size() == 1);
		assertTrue(phoneNumberRepository.findAll().size() == 1);
		assertTrue(emailRepository.findAll().size() == 1);

		newUser.getPhoneNumbers().add(phoneNumber1);
		newUser.getEmails().add(email1);

		assertTrue(newUser.getEmails().get(0).getMail().equals("test@mail.com"));
		assertTrue(newUser.getPhoneNumbers().get(0).getNumber().equals("12345678910"));

		userService.deleteUser(newUser.getId());

		assertTrue(phoneNumberRepository.findAll().size() == 0);
		assertTrue(emailRepository.findAll().size() == 0);
		assertTrue(userRepository.findAll().size() == 0);
	}

}
