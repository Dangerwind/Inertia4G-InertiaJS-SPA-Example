package com.github.Dangerwind.springbootinertiareact.controller;

import com.github.Dangerwind.springbootinertiareact.dto.ProductDTO;
import com.github.Dangerwind.springbootinertiareact.handler.NoIdException;
import com.github.Dangerwind.springbootinertiareact.handler.ValidationException;
import com.github.Dangerwind.springbootinertiareact.mapper.ProductMapper;
import com.github.Dangerwind.springbootinertiareact.model.Product;
import com.github.Dangerwind.springbootinertiareact.repository.ProductRepository;
import io.github.inertia4j.spring.Inertia;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final Inertia inertia;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @GetMapping("/")
    public ResponseEntity<String> index() {
        return inertia.render("Index", Map.of("description",
                "Демонстрационное одностраничное приложение (SPA), построенное с " +
                        "использованием Inertia.js и Spring Boot"));
    }

    @GetMapping("/about")
    public ResponseEntity<String> about() {
        return inertia.render("About", Map.of("description",
                "Это практический пример, который поможет лучше разобраться в создании динамичных " +
                        "SPA на Inertia.js с использованием Java/Spring Boot адаптер Inertia4J."));
    }

    @GetMapping("/products")
    public Object productsList(@RequestParam(value = "page", defaultValue = "0", required = false) int page) {
        Pageable pageable = PageRequest.of(page, 4);
        Page<Product> prodPage = productRepository.findAll(pageable);
        List<Product> prodCont = prodPage.getContent();

        List<Map<String, Object>> products = prodCont.stream()
                .map(product -> {
                    Map<String, Object> productMap = new HashMap<>();
                    productMap.put("id", product.getId());
                    productMap.put("title", product.getTitle());
                    productMap.put("description", product.getDescription());
                    return productMap;
                })
                .toList();

        Map<String, Object> pagination = new HashMap<>();
        pagination.put("current_page", page + 1);
        pagination.put("total_pages", prodPage.getTotalPages() );

        pagination.put("has_prev", page > 0);
        pagination.put("has_next", page < (prodPage.getTotalPages() - 1 ));
        pagination.put("prev_page", page > 0 ? page - 1 : null);
        pagination.put("next_page", page < (prodPage.getTotalPages() - 1 ) ? page + 1 : null);

        return inertia.render("ProductsList", Map.of(
                "products", products,
                "pagination", pagination
        ));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<String> productsDetail(@PathVariable Long id) {
        var prod = productRepository.findById(id).orElseThrow(
                () -> new NoIdException("Нет такого продукта, но вы можете его создать!")
        );

        Map<String, Object> product = Map.of(
                "id", prod.getId(),
                "title", prod.getTitle(),
                "description", prod.getDescription()
        );
        return inertia.render("ProductDetail", Map.of("product", product));
    }

    @GetMapping("/products/create")
    public ResponseEntity<String> createProduct() {
        return inertia.render("CreateProduct", Map.of());
    }

    @PostMapping("/products/create")
    public Object createProductPost(@RequestBody ProductDTO productDTO) {

        if (productRepository.existsByTitle(productDTO.getTitle())) {
            throw new ValidationException("Продукт с именем " + productDTO.getTitle() + " уже существует!");
        };

        Product product = productMapper.toModel(productDTO);
        productRepository.save(product);

        return inertia.redirect("/products");
    }
}
