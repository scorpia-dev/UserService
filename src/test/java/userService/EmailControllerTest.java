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
import userService.service.EmailService;
import userService.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureTestDatabase(replace = Replace.ANY)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
public class EmailControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	UserService userService;

	@Autowired
	EmailService emailService;

	@Autowired
	ObjectMapper objectMapper;

	User user;
	private List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();
	private List<Email> emails = new ArrayList<Email>();

	@BeforeEach
	public void setUp() throws Exception {
		user = new User("Nick", "Prendergast", emails, phoneNumbers);
	}

	@Transactional
	@Test
	public void addNewEmailTest() throws Exception {
		Email email = new Email("test@mail.com", user);

		String userJson = objectMapper.writeValueAsString(user);
		String emailJson = objectMapper.writeValueAsString(email);

		mvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(userJson));

		mvc.perform(put("/email/add/1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(emailJson)).andExpect(status().isOk()).andDo(print())
				.andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("mail").value("test@mail.com"))
				.andExpect(MockMvcResultMatchers.jsonPath("user_id").value(1));
	}

	@Transactional
	@Test
	public void addUpdateEmailTest() throws Exception {
		Email email = new Email("test@mail.com", user);

		String userJson = objectMapper.writeValueAsString(user);
		String emailJson = objectMapper.writeValueAsString(email);

		mvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(userJson));

		mvc.perform(put("/email/add/1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(emailJson)).andExpect(status().isOk()).andDo(print())
				.andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("mail").value("test@mail.com"))
				.andExpect(MockMvcResultMatchers.jsonPath("user_id").value(1));

		Email email1 = new Email("updated@mail.com", user);
		String updatedEmailJson = objectMapper.writeValueAsString(email1);

		mvc.perform(put("/email/update/1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(updatedEmailJson)).andExpect(status().isOk()).andDo(print())
				.andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("mail").value("updated@mail.com"))
				.andExpect(MockMvcResultMatchers.jsonPath("user_id").value(1));
	}

}
