package com.github.Dangerwind.springbootinertiareact;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.Dangerwind.springbootinertiareact.dto.ProductDTO;
import com.github.Dangerwind.springbootinertiareact.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class ControllerTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
    @Autowired
    private ProductRepository productRepository;
	@Autowired
	private ObjectMapper om;

	@Test
	public void inertiaGETTest() throws Exception {

		var result = mockMvc.perform(get("/products")
// для имитации запроса Inertia ставим в заголовке X-Inertia: true
// ответ в виде JSON со кодом 200 (isOk) на GET запросы
// иначе запрос вернет страницу app.html c подставленными туда component и props
						.header("X-Inertia", "true")
						.header("Accept", "text/html"))
				.andExpect(status().isOk())
				.andReturn();
		String responseContent = result.getResponse().getContentAsString();
		var jsonResponse = objectMapper.readTree(responseContent);

// Проверка ключевых полей Inertia-структуры
		assertThat(jsonResponse.has("component")).isTrue();
		assertThat(jsonResponse.has("props")).isTrue();

// Проверки своих данных
		assertThat(jsonResponse.get("component").asText()).isEqualTo("ProductsList");
		var props = jsonResponse.get("props");
		assertThat(props.has("products")).isTrue();
		assertThat(props.has("pagination")).isTrue();
	}

	@Test
	public void inertiaPOSTTest() throws Exception {

		productRepository.deleteAll();

		ProductDTO productDTO = new ProductDTO();
		productDTO.setTitle("Test product title");
		productDTO.setDescription("Test product Description");

		mockMvc.perform(post("/products/create")
						.contentType(MediaType.APPLICATION_JSON)
						.content(om.writeValueAsString(productDTO))
// в заголовке X-Inertia: true чтобы запрос был от Inertia
						.header("X-Inertia", "true"))
// при redirect возвращается код 302/303 (is3xxRedirection) а не 200 (isOk)
				.andExpect(status().is3xxRedirection())
// url куда должен быть редирект
				.andExpect(redirectedUrl("/products"))
				.andReturn();

		var products = productRepository.findFirstByTitle(productDTO.getTitle());

		assertThat(products).isPresent();
		assertThat(products).isNotNull();
		assertThat(products.get().getDescription()).isEqualTo(productDTO.getDescription());

		productRepository.deleteAll();
	}
}
