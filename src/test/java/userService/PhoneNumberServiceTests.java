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
import userService.service.PhoneNumberService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PhoneNumberServiceTests {

	@Autowired
	PhoneNumberService phoneNumberService;

	@Autowired
	UserRepository userRepository;

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

		PhoneNumber phoneNumber = new PhoneNumber("12345678910", user);

		User newUser = userRepository.save(user);
		newUser.getPhoneNumbers().add(phoneNumberService.addNewPhoneNumber(phoneNumber, newUser.getId()));

		assertTrue(newUser.getPhoneNumbers().size() == 1);
	}

	@Transactional
	@Test
	public void addMultipleNewPhoneNumbersTest() {

		PhoneNumber phoneNumber = new PhoneNumber("23456789101", user);
		PhoneNumber phoneNumber1 = new PhoneNumber("19876543210", user);

		User newUser = userRepository.save(user);

		newUser.getPhoneNumbers().add(phoneNumberService.addNewPhoneNumber(phoneNumber, newUser.getId()));
		newUser.getPhoneNumbers().add(phoneNumberService.addNewPhoneNumber(phoneNumber1, newUser.getId()));

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
		newUser.getPhoneNumbers().add(phoneNumberService.addNewPhoneNumber(phoneNumber, newUser.getId()));
		int phoneNumberId = newUser.getPhoneNumbers().get(0).getId();

		phoneNumberService.updatePhoneNumber(phoneNumber1, phoneNumberId);

		assertTrue(newUser.getPhoneNumbers().get(0).getNumber().equals("19876543210"));
		assertTrue(newUser.getPhoneNumbers().size() == 1);
	}

}
