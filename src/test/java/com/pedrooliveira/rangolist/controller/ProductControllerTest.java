package com.pedrooliveira.rangolist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedrooliveira.rangolist.dto.OnlyProductDTO;
import com.pedrooliveira.rangolist.dto.ProductDTO;
import com.pedrooliveira.rangolist.dto.RestaurantDTO;
import com.pedrooliveira.rangolist.exception.HandleIDNotFound;
import com.pedrooliveira.rangolist.exception.HandleNoHasProducts;
import com.pedrooliveira.rangolist.model.Address;
import com.pedrooliveira.rangolist.model.Product;
import com.pedrooliveira.rangolist.model.Restaurant;
import com.pedrooliveira.rangolist.repository.ProductRepository;
import com.pedrooliveira.rangolist.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProductControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @MockBean
  private ProductRepository productRepository;
  @MockBean
  private ProductService productService;

  private Product product;
  private ProductDTO productDTO;
  private OnlyProductDTO onlyProductDTO;
  private MockMultipartFile image;

  @BeforeEach
  void config() {
    Address address = new Address();
    address.setStreet("Test Street");
    address.setCity("Test City");
    address.setState("Test State");
    address.setZipcode("00000-000");

    Restaurant restaurant = new Restaurant();
    restaurant.setId(1L);
    restaurant.setName("Test Restaurant");
    restaurant.setOpeningHours("10:00 - 22:00");
    restaurant.setAddress(address);
    restaurant.setStatus(true);
    restaurant.setImage("image.jpg");

    RestaurantDTO restaurantDTO = new RestaurantDTO();
    restaurantDTO.setNome("Test Restaurant");
    restaurantDTO.setFuncionamento("10:00 - 22:00");
    restaurantDTO.setImagem("image.jpg");

    product = new Product();
    product.setId(1L);
    product.setName("Test Product");
    product.setPrice(9.99);
    product.setCategory("Test Category");
    product.setStatus(true);
    product.setImage("image.jpg");
    product.setRestaurant(restaurant);

    productDTO = new ProductDTO();
    productDTO.setNome("Test Product");
    productDTO.setPreco(9.99);
    productDTO.setCategoria("Test Category");
    productDTO.setImagem("image.jpg");
    productDTO.setRestaurante(restaurantDTO);

    onlyProductDTO = new OnlyProductDTO();
    onlyProductDTO.setNome("Test Product");
    onlyProductDTO.setCategoria("Test Category");
    onlyProductDTO.setImagem("image.jpg");

    image = new MockMultipartFile(
        "file",
        "test-image.jpg",
        MediaType.IMAGE_JPEG_VALUE,
        "Test Image Content".getBytes()
    );
  }

  @DisplayName("Should register a product with image")
  @Test
  public void testRegisterProductWithImage() throws Exception {
    MockMultipartFile productJson = new MockMultipartFile(
        "product",
        "image",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(product)
    );

    when(productService.createProductWithImage(Mockito.any(Product.class), Mockito.any(MockMultipartFile.class)))
        .thenReturn(product);

    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/product")
        .file(image)
            .param("name", product.getName())
            .param("price", String.valueOf(product.getPrice()))
            .param("category", product.getCategory())
            .param("restaurant", String.valueOf(product.getRestaurant().getId()))
        .contentType(MediaType.MULTIPART_FORM_DATA))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.nome").value(product.getName()))
        .andExpect(jsonPath("$.preco").value(product.getPrice()))
        .andExpect(jsonPath("$.categoria").value(product.getCategory()))
        .andExpect(jsonPath("$.imagem").value(product.getImage()))
        .andExpect(jsonPath("$.restaurante").exists());
  }

  @DisplayName("Should display all products")
  @Test
  public void testDisplayAllProducts() throws Exception {
    List<ProductDTO> productDTOList = new ArrayList<>();
    productDTOList.add(productDTO);

    when(productService.listAllProducts()).thenReturn(productDTOList);

    mockMvc.perform(get("/api/product")
          .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].nome").value("Test Product"))
        .andExpect(jsonPath("$.[0].preco").value(9.99))
        .andExpect(jsonPath("$.[0].categoria").value("Test Category"))
        .andExpect(jsonPath("$.[0].imagem").value("image.jpg"))
        .andExpect(jsonPath("$.[0].restaurante").exists());
  }

  @DisplayName("Should display all products without restaurants")
  @Test
  public void testDisplayAllProductsWithoutRestaurants() throws Exception {
    List<OnlyProductDTO> productList = new ArrayList<>();
    productList.add(onlyProductDTO);

    when(productService.listProductusWithoutRestaurants()).thenReturn(productList);

    mockMvc.perform(get("/api/product/all")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].nome").value("Test Product"))
        .andExpect(jsonPath("$.[0].categoria").value("Test Category"))
        .andExpect(jsonPath("$.[0].imagem").value("image.jpg"));
  }

  @DisplayName("Should return a exception")
  @Test
  public void testNoHasProduct() throws Exception {
    doThrow(HandleNoHasProducts.class).when(productService).listAllProducts();
    mockMvc.perform(get("/api/product"))
        .andExpect(status().isNotFound());
  }

  @DisplayName("Should find product by ID")
  @Test
  public void testFindProductById() throws Exception {
    when(productService.findProductById(1L)).thenReturn(Optional.of(product));
    mockMvc.perform(get("/api/product/{id}", 1L)
          .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.name").value("Test Product"))
        .andExpect(jsonPath("$.price").value(9.99))
        .andExpect(jsonPath("$.category").value("Test Category"))
        .andExpect(jsonPath("$.image").value("image.jpg"))
        .andExpect(jsonPath("$.restaurant").exists());
  }

  @DisplayName("Should return a exception if there is no id")
  @Test
  public void testIdNotFound() throws Exception {
    doThrow(HandleIDNotFound.class).when(productService).findProductById(product.getId());
    mockMvc.perform(get("/api/product/{id}", 1L))
        .andExpect(status().isNotFound());
  }

  @DisplayName("Should update product by name")
  @Test
  public void testUpdateProductById() throws Exception {
    product.setName("Update Product");

    when(productService.updateProductById(eq(1L), any(Product.class))).thenReturn(product);

    mockMvc.perform(put("/api/product/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(product)))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.name").value("Update Product"))
        .andExpect(jsonPath("$.price").value(9.99))
        .andExpect(jsonPath("$.category").value("Test Category"))
        .andExpect(jsonPath("$.image").value("image.jpg"))
        .andExpect(jsonPath("$.restaurant").exists());
  }

  @DisplayName("Should delete product by ID")
  @Test
  public void testDeleteProductById() throws Exception {
    when(productService.findProductById(1L)).thenReturn(Optional.of(product));

    mockMvc.perform(delete("/api/product/{id}", 1L))
        .andExpect(status().isNoContent());
  }
}
