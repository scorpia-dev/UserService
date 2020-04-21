package userService;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import userService.model.Email;
import userService.model.PhoneNumber;
import userService.model.User;
import userService.service.UserService;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class EmailControllerValidationTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	UserService userService;

	@Autowired
	ObjectMapper objectMapper;

	private List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();
	private List<Email> emails = new ArrayList<Email>();

	User user;

	@BeforeEach
	public void setUp() {
		user = new User("Nick", "Prendergast", emails, phoneNumbers);
	}

	@Test
	public void addInvalidEmailAddressTest() throws Exception {

		Email email = new Email("testmailcom", user);

		String json = objectMapper.writeValueAsString(email);

		mvc.perform(put("/email/addNewEmail/1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest()).andDo(print())
				.andExpect(content().string(containsString(
						"not valid due to validation error: mail: must be a well-formed email address")));
	}

	@Transactional
	@Test
	public void addNewEmailToNonExistingUserTest() throws Exception {
		Email email = new Email("test@mail.com", user);

		String emailJson = objectMapper.writeValueAsString(email);

		mvc.perform(put("/email/addNewEmail/2").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(emailJson)).andExpect(status().isNotFound()).andDo(print())
				.andExpect(content()
						.string(containsString("not valid due to validation error: the user with id 2 was not found")));
	}

	@Test
	public void addDuplicateEmailTest() throws Exception {

		userService.createUser(user);

		Email email = new Email("test@mail.com", user);

		String json = objectMapper.writeValueAsString(email);

		mvc.perform(put("/email/addNewEmail/1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());

		mvc.perform(put("/email/addNewEmail/1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isInternalServerError())
				.andDo(print()).andExpect(content().string(containsString(
						"not valid due to :org.hibernate.exception.ConstraintViolationException: could not execute statement")));
	}

	@Transactional
	@Test
	public void updateNonExistingEmailEntityTest() throws Exception {
		Email email = new Email("test@mail.com", user);

		String emailJson = objectMapper.writeValueAsString(email);

		mvc.perform(put("/email/updateEmail/2").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(emailJson)).andExpect(status().isNotFound()).andDo(print())
				.andExpect(content().string(
						containsString("not valid due to validation error: the email with id 2 was not found")));
	}

	@Transactional
	@Test
	public void updateUsingInvalidEmailTest() throws Exception {
		Email email = new Email("testmailcom", user);

		String emailJson = objectMapper.writeValueAsString(email);

		mvc.perform(put("/email/updateEmail/1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(emailJson)).andExpect(status().isBadRequest())
				.andDo(print()).andExpect(content().string(containsString(
						"not valid due to validation error: mail: must be a well-formed email address")));
	}
}
