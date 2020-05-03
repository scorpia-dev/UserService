package userService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import userService.model.Email;
import userService.model.PhoneNumber;
import userService.model.User;
import userService.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class PhoneNumberControllerValidationTest {

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
	public void addInvalidPhoneNumberTest() throws Exception {

		PhoneNumber phoneNumber = new PhoneNumber("112zcxds", user);

		String json = objectMapper.writeValueAsString(phoneNumber);

		mvc.perform(put("/phonenumber/add/1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest()).andDo(print())
				.andExpect(content().string(
						containsString("not valid due to validation error: number: Phone number must be 11 digits")));
	}

	@Test
	public void addDuplicatePhoneNumberTest() throws Exception {

		userService.createUser(user);

		PhoneNumber phoneNumber = new PhoneNumber("12345678910", user);

		String json = objectMapper.writeValueAsString(phoneNumber);
		mvc.perform(put("/phonenumber/add/1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());

		mvc.perform(put("/phonenumber/add/1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isInternalServerError())
				.andDo(print()).andExpect(content().string(containsString(
						"not valid due to :org.hibernate.exception.ConstraintViolationException: could not execute statement")));
	}

	@Transactional
	@Test
	public void addNewPhoneNumberToNonExistingUserTest() throws Exception {
		PhoneNumber phoneNumber = new PhoneNumber("12345678910", user);

		String json = objectMapper.writeValueAsString(phoneNumber);

		mvc.perform(put("/phonenumber/add/2").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isNotFound()).andDo(print())
				.andExpect(content()
						.string(containsString("not valid due to validation error: the user with id 2 was not found")));
	}

	@Transactional
	@Test
	public void updateNonExistingPhoneNumberEntityTest() throws Exception {
		PhoneNumber phoneNumber = new PhoneNumber("12345678910", user);

		String json = objectMapper.writeValueAsString(phoneNumber);

		mvc.perform(put("/phonenumber/update/2").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isNotFound()).andDo(print())
				.andExpect(content().string(
						containsString("not valid due to validation error: the phoneNumber with id 2 was not found")));
	}

	@Transactional
	@Test
	public void updateUsingInvalidPhoneNumberTest() throws Exception {
		PhoneNumber phoneNumber = new PhoneNumber("12szx2", user);

		String json = objectMapper.writeValueAsString(phoneNumber);

		mvc.perform(put("/phonenumber/update/1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest()).andDo(print())
				.andExpect(content().string(
						containsString("not valid due to validation error: number: Phone number must be 11 digits")));
	}
}
