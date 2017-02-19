package br.com.security.web.controller;

import static java.lang.String.format;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.Map;

import javax.servlet.Filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapLikeType;

/**
 * The type User controller integrated test.
 *
 * @author Matheus
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class UserControllerIntegratedTest {

	private static final String BASIC_AUTHORIZATION_HEADER = "Basic dHJ1c3Q6c2VjcmV0";
	private static final String BEARER_AUTHORIZATION_HEADER = "Bearer %s";
	private static final String USERNAME = "test";
	private static final String PASSWORD = "12345";

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private Filter[] filters;

	@Autowired
	private ObjectMapper mapper;

	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		this.mockMvc = webAppContextSetup(this.context).addFilters(this.filters).build();
	}

	@Test
	public void user() throws Exception {
		this.mockMvc.perform(get("/users/me")
				.header(AUTHORIZATION, format(BEARER_AUTHORIZATION_HEADER, this.getAccessToken())))
				.andExpect(status().is2xxSuccessful());
	}

	@Test
	public void profiles() throws Exception {
		this.mockMvc.perform(get("/users/{username}/profiles", USERNAME))
				.andExpect(status().is2xxSuccessful())
				.andExpect(jsonPath("$.*", hasSize(3)));
	}

	private String getAccessToken() throws Exception {
		final String response = this.performLoginRequest();
		final MapLikeType mapType = this.mapper.getTypeFactory().constructMapLikeType(Map.class, String.class, String.class);
		final Map<String, String> responseMap = this.mapper.readValue(response, mapType);
		return responseMap.get("access_token");
	}

	private String performLoginRequest() throws Exception {
		final MvcResult mvcResult = this.mockMvc.perform(post("/oauth/token")
				.header(AUTHORIZATION, BASIC_AUTHORIZATION_HEADER)
				.contentType(APPLICATION_FORM_URLENCODED)
				.param("grant_type", "password")
				.param("scope", "read write")
				.param("username", USERNAME)
				.param("password", PASSWORD))
				.andExpect(status().is2xxSuccessful())
				.andReturn();
		return mvcResult.getResponse().getContentAsString();
	}
}