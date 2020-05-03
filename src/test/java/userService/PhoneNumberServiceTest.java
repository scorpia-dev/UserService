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
import userService.service.PhoneNumberService;
import userService.service.UserService;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class PhoneNumberServiceTest {

	@Autowired
	PhoneNumberService phoneNumberService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

	@Autowired
	EntityManager entityManager;

	User user;
	private List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();
	private List<Email> emails = new ArrayList<Email>();

	@BeforeEach
	public void setUp() {
		user = new User("Nick", "Prendergast", emails, phoneNumbers);
	}

	@Transactional
	@Test
	public void addNewPhoneNumberTest() {
		User user1 = userService.createUser(user);
		PhoneNumber phoneNumber = new PhoneNumber("12345678910", user1);
		phoneNumberService.addNewPhoneNumber(phoneNumber, user1.getId());
		entityManager.refresh(user1);

		assertTrue(user1.getPhoneNumbers().size() == 1);
	}

	@Transactional
	@Test
	public void addMultipleNewPhoneNumbersTest() {
		PhoneNumber phoneNumber = new PhoneNumber("23456789101", user);
		PhoneNumber phoneNumber1 = new PhoneNumber("19876543210", user);

		User newUser = userRepository.save(user);

		phoneNumberService.addNewPhoneNumber(phoneNumber, newUser.getId());
		phoneNumberService.addNewPhoneNumber(phoneNumber1, newUser.getId());
		entityManager.refresh(newUser);

		assertTrue(newUser.getPhoneNumbers().get(0).getNumber().equals("23456789101"));
		assertTrue(newUser.getPhoneNumbers().get(1).getNumber().equals("19876543210"));
		assertTrue(newUser.getPhoneNumbers().size() == 2);
	}

	@Transactional
	@Test
	public void updatePhoneNumberTest() {
		PhoneNumber phoneNumber = new PhoneNumber("12345678910", user);
		PhoneNumber phoneNumber1 = new PhoneNumber("19876543210", user);

		User newUser = userRepository.save(user);
		phoneNumberService.addNewPhoneNumber(phoneNumber, newUser.getId());
		entityManager.refresh(newUser);

		int phoneId = newUser.getPhoneNumbers().get(0).getId();
		phoneNumberService.updatePhoneNumber(phoneNumber1, phoneId);

		assertTrue(newUser.getPhoneNumbers().get(0).getNumber().equals("19876543210"));
		assertTrue(newUser.getPhoneNumbers().size() == 1);
	}

}
