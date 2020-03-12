package com.blo.res;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import com.blo.res.entity.User;
import com.blo.res.exceptionhandlers.UserNotFoundException;
import com.blo.res.restcontroller.UserRestController;
import com.blo.res.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * This is the unit test class, so we arent starting any servers
 */

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserRestController.class)
public class RestControllerTest {

	@MockBean
	private UserService userService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	User mockUser;

	@Before
	public void setup() {
		this.mockUser = new User(1, "User1", "Password", 1);

	}

	private String getRootUrl() {
		return "/api/v1";
	}

	@Test
	public void userDoesNotExist() throws Exception {
		when(userService.findUserbyID(0)).thenThrow(new UserNotFoundException());
		this.mockMvc.perform(get(getRootUrl() + "/users/0")).andExpect(status().isNotFound())
				.andExpect(content().string("User not found"));
	}

	@Test
	public void testGetAllUsers() throws Exception {
		List<User> allUsers = Arrays.asList(mockUser);
		given(userService.getAllUsers()).willReturn(allUsers);

		// when + then
		this.mockMvc.perform(get(getRootUrl() + "/users")).andExpect(status().isOk()).andExpect(content()
				.json("[{'id': 1," + "'username': 'User1'," + "'password': 'Password', " + "'user_type' : 1}]"));

	}

	@Test
	public void testGetUserById() throws Exception {
		given(userService.findUserbyID(1)).willReturn(mockUser);

		this.mockMvc.perform(get(getRootUrl() + "/users/1")).andExpect(status().isOk())
				.andExpect(jsonPath("_links.self", notNullValue())).andExpect(content()
						.json("{'id': 1," + "'username': 'User1'," + "'password': 'Password', " + "'user_type' : 1}"));

	}

	@Test
	public void testPostUser() throws Exception {
			//this commented code works but we trying the expanded approach
//		given(userService.createUser(mockUser)).willReturn(mockUser);
//		this.mockMvc.perform(post(getRootUrl() + "/users").contentType("application/json")
//				// .param("sendWelcomeMail", "true")
//				.content(objectMapper.writeValueAsString(mockUser))).andExpect(status().isCreated());

		RequestBuilder requestBuilder= MockMvcRequestBuilders.post(getRootUrl() + "/users")
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(mockUser)) //this LOC converts mockUser to JSON
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult mvcResult= mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response= mvcResult.getResponse();
		
		assertEquals(HttpStatus.CREATED, response.getStatus());
		
	}

	@Test
	public void testDeleteUser() throws Exception {
		given(userService.findUserbyID(1)).willReturn(mockUser);
		this.mockMvc.perform(delete(getRootUrl() + "/users/1")).andExpect(status().is2xxSuccessful()).andExpect(
				content().json("{'User deleted':{'rel':'ALL USERS','href':'http://localhost/api/v1/users'}}", true));

	}

	@Test
	public void testPutUser() throws Exception {
		given(userService.findUserbyID(1)).willReturn(mockUser);

		mockMvc.perform(put(getRootUrl() + "/users/1")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(new User(2, "User2", "Password", 1)))) //update with new user
		
		.andExpect(status().isOk())
		.andDo(print());

	}

	@Test
	public void testPatchUser() throws JsonProcessingException, Exception {
		given(userService.findUserbyID(2)).willReturn(mockUser);
		mockMvc.perform(patch(getRootUrl() + "/users/2").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(mockUser))).andExpect(status().isOk());
	}

	// test for argument mismatch
	@Test
	public void whenMethodArgumentMismatch_thenBadRequest() throws Exception {

		mockMvc.perform(get(getRootUrl() + "/users/cc"))
				.andExpect(content().string(containsString("should be of type"))).andExpect(status().isBadRequest())
				.andDo(print());

	}

	@Test
	public void whenHttpRequestMethodNotSupported_thenMethodNotAllowed() throws Exception {
		// mockMvc.perform(patch(getRootUrl()+"/users/1")) //patch that is if patch
		// wasnt supported
		// .andExpect(status().isMethodNotAllowed())
		// .andExpect(content().string(containsString("Supported methods are")))
		// .andDo(print());
		//
	}

	// @Test
	// public void whenSendInvalidHttpMediaType_thenUnsupportedMediaType() {
	// Response response = givenAuth().body("").post(URL_PREFIX + "/api/foos");
	// ApiError error = response.as(ApiError.class);
	//
	// assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, error.getStatus());
	// assertEquals(1, error.getErrors().size());
	// assertTrue(error.getErrors().get(0).contains("media type is not supported"));
	// }
	//

}
