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
import userService.model.User;
import userService.service.UserService;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class UserControllerValidationTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	UserService userService;

	@Autowired
	ObjectMapper objectMapper;

	User user;

	@BeforeEach
	public void setUp() {
		user = new User();
	}

	@Test
	public void createUserNoFirstNameTest() throws Exception {

		user.setLastName("Prendergast");
		String json = objectMapper.writeValueAsString(user);

		mvc.perform(
				post("/user").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest()).andDo(print()).andExpect(content().string(
						containsString("not valid due to validation error: firstName: First name can not be empty")));
	}

	@Test
	public void createUserInvalidFirstNameTest() throws Exception {

		user.setFirstName("23231+");
		user.setLastName("Prendergast");
		String json = objectMapper.writeValueAsString(user);

		mvc.perform(
				post("/user").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest()).andDo(print()).andExpect(content().string(containsString(
						"not valid due to validation error: firstName: a name can only contain letters and spaces")));
	}

	@Test
	public void createUserInvalidLastNameTest() throws Exception {

		user.setFirstName("Nick");
		user.setLastName("123+");
		String json = objectMapper.writeValueAsString(user);

		mvc.perform(
				post("/user").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest()).andDo(print()).andExpect(content().string(containsString(
						"not valid due to validation error: lastName: a name can only contain letters and spaces")));
	}

	@Test
	public void createUserNoLastNameTest() throws Exception {
		user.setFirstName("Nick");
		String json = objectMapper.writeValueAsString(user);

		mvc.perform(
				post("/user").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest()).andDo(print()).andExpect(content().string(
						containsString("not valid due to validation error: lastName: Last name can not be empty")));
	}

	@Transactional
	@Test
	public void deleteInvalidUserTest() throws Exception {
		String json = objectMapper.writeValueAsString(user);

		mvc.perform(
				post("/user").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json));

		mvc.perform(delete("/user/id/{userId}", 2).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(print()).andExpect(content()
						.string(containsString("not valid due to validation error: the user with id 2 was not found")));
	}

	@Test
	public void getMemberIdDoesNotExist() throws Exception {
		mvc.perform(
				get("/user/id/{userId}", 1).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(print()).andExpect(content()
						.string(containsString("not valid due to validation error: the user with id 1 was not found")));
	}

	@Test
	public void getMemberNameDoesNotExist() throws Exception {
		mvc.perform(get("/user/name/{firstName}/{lastName}", "test", "test").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andDo(print())
				.andExpect(content().string(
						containsString("not valid due to validation error: the user with name test was not found")));
	}

}
