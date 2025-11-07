package com.github.Dangerwind.springbootinertiareact;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SpringBootInertiaReactApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;


	@Test
	public void inertiaResponseTest() throws Exception {

		MvcResult result = mockMvc.perform(get("/products")
						.header("X-Inertia", "true")
						.header("Accept", "text/html"))
				.andExpect(status().isOk())
				.andReturn();
		String responseContent = result.getResponse().getContentAsString();
		var jsonResponse = objectMapper.readTree(responseContent);

		// Проверка  ключевых полей Inertia-структуры
		assertThat(jsonResponse.has("component")).isTrue();
		assertThat(jsonResponse.has("props")).isTrue();
		assertThat(jsonResponse.get("component").asText()).isEqualTo("ProductsList");


		var props = jsonResponse.get("props");
		assertThat(props.has("products")).isTrue();
		assertThat(props.has("pagination")).isTrue();
	}
}
