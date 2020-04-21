package userService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import userService.model.Email;
import userService.model.PhoneNumber;
import userService.model.User;
import userService.service.UserService;

@AutoConfigureTestDatabase(replace = Replace.ANY)
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	UserService userService;

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
	public void createUserTest() throws Exception {

		String json = objectMapper.writeValueAsString(user);

		mvc.perform(
				post("/user").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json))

				.andExpect(status().isOk()).andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("firstName").value("Nick"))
				.andExpect(MockMvcResultMatchers.jsonPath("lastName").value("Prendergast"))
				.andExpect(MockMvcResultMatchers.jsonPath("emails", IsEmptyCollection.empty()))
				.andExpect(MockMvcResultMatchers.jsonPath("phoneNumbers", IsEmptyCollection.empty()));

		User newUser = userService.getUserById(1);
		assertThat(newUser.getId() == 1);
		assertThat(newUser.getFirstName().equals("Nick"));
		assertThat(newUser.getLastName().equals("Prendergast"));
		assertThat(newUser.getEmails() == IsEmptyCollection.empty());
		assertThat(newUser.getPhoneNumbers() == IsEmptyCollection.empty());
	}

	@Transactional
	@Test
	public void deleteUserTest() throws Exception {
		String json = objectMapper.writeValueAsString(user);

		mvc.perform(
				post("/user").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json));

		mvc.perform(delete("/user/id/{userId}", 1).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andDo(print()).andExpect(content().string(containsString("User with id 1 deleted")));

		EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> userService.getUserById(1));
		assertTrue(thrown.getMessage().contains("the user with id 1 was not found"));
	}

	@Transactional
	@Test
	public void getUserByIdTest() throws Exception {
		userService.createUser(user);

		mvc.perform(get("/user/id/{userId}", 1).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("firstName").value("Nick"))
				.andExpect(MockMvcResultMatchers.jsonPath("lastName").value("Prendergast"))
				.andExpect(MockMvcResultMatchers.jsonPath("emails", IsEmptyCollection.empty()))
				.andExpect(MockMvcResultMatchers.jsonPath("phoneNumbers", IsEmptyCollection.empty()));

		assertTrue(userService.getUserById(1) == user);
	}

	@Transactional
	@Test
	public void getUserByNameTest() throws Exception {
		userService.createUser(user);

		mvc.perform(
				get("/user/name/{firstName}/{lastName}", "Nick", "Prendergast").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print()).andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("firstName").value("Nick"))
				.andExpect(MockMvcResultMatchers.jsonPath("lastName").value("Prendergast"))
				.andExpect(MockMvcResultMatchers.jsonPath("emails", IsEmptyCollection.empty()))
				.andExpect(MockMvcResultMatchers.jsonPath("phoneNumbers", IsEmptyCollection.empty()));

		assertTrue(userService.getUserByName("Nick", "Prendergast") == user);
	}

}
