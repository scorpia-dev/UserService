package userService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import userService.model.Email;
import userService.model.PhoneNumber;
import userService.model.User;
import userService.service.PhoneNumberService;
import userService.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureTestDatabase(replace = Replace.ANY)
@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class PhoneNumberControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	UserService userService;

	@Autowired
	PhoneNumberService phoneNumberService;

	@Autowired
	ObjectMapper objectMapper;

	User user;
	private List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();
	private List<Email> emails = new ArrayList<Email>();

	@BeforeEach
	public void setUp() {
		user = new User("Nick", "Prendergast", emails, phoneNumbers);
	}

	@Transactional
	@Test
	public void addNewPhoneNumberTest() throws Exception {
		PhoneNumber phoneNumber = new PhoneNumber("12345678910", user);

		String userJson = objectMapper.writeValueAsString(user);
		String phoneNumberJson = objectMapper.writeValueAsString(phoneNumber);

		mvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(userJson));

		mvc.perform(put("/phonenumber/add/1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(phoneNumberJson)).andExpect(status().isOk()).andDo(print())
				.andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("number").value("12345678910"))
				.andExpect(MockMvcResultMatchers.jsonPath("user_id").value(1));
	}

	@Transactional
	@Test
	public void updatePhoneNumberTest() throws Exception {
		PhoneNumber phoneNumber = new PhoneNumber("12345678910", user);
		PhoneNumber phoneNumber1 = new PhoneNumber("01987654321", user);

		String userJson = objectMapper.writeValueAsString(user);
		String phoneNumberJson = objectMapper.writeValueAsString(phoneNumber);
		String updatedPhoneNumberJson = objectMapper.writeValueAsString(phoneNumber1);

		mvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(userJson));

		mvc.perform(put("/phonenumber/add/1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(phoneNumberJson)).andExpect(status().isOk()).andDo(print())
				.andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("number").value("12345678910"))
				.andExpect(MockMvcResultMatchers.jsonPath("user_id").value(1));

		mvc.perform(put("/phonenumber/update/1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(updatedPhoneNumberJson)).andExpect(status().isOk())
				.andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("number").value("01987654321"))
				.andExpect(MockMvcResultMatchers.jsonPath("user_id").value(1));

	}

}
