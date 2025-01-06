package com.pedrooliveira.rangolist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedrooliveira.rangolist.dto.OnlyProductDTO;
import com.pedrooliveira.rangolist.dto.ProductDTO;
import com.pedrooliveira.rangolist.dto.PromotionDTO;
import com.pedrooliveira.rangolist.dto.RestaurantDTO;
import com.pedrooliveira.rangolist.exception.HandleIDNotFound;
import com.pedrooliveira.rangolist.exception.HandleNoHasPromotions;
import com.pedrooliveira.rangolist.model.Address;
import com.pedrooliveira.rangolist.model.Product;
import com.pedrooliveira.rangolist.model.Promotion;
import com.pedrooliveira.rangolist.model.Restaurant;
import com.pedrooliveira.rangolist.repository.PromotionRepository;
import com.pedrooliveira.rangolist.service.PromotionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(PromotionController.class)
class PromotionControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PromotionService promotionService;

  @Autowired
  private ObjectMapper objectMapper;

  private Promotion promotion;
  private PromotionDTO promotionDTO;
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

    Product product = new Product();
    product.setId(1L);
    product.setName("Test Product");
    product.setPrice(9.99);
    product.setCategory("Test Category");
    product.setStatus(true);
    product.setImage("image.jpg");
    product.setRestaurant(restaurant);

    ProductDTO productDTO = new ProductDTO();
    productDTO.setNome("Test Product");
    productDTO.setPreco(9.99);
    productDTO.setCategoria("Test Category");
    productDTO.setImagem("image.jpg");
    productDTO.setRestaurante(restaurantDTO);

    OnlyProductDTO onlyProductDTO = new OnlyProductDTO();
    onlyProductDTO.setNome("Test Product");
    onlyProductDTO.setCategoria("Test Category");
    onlyProductDTO.setImagem("image.jpg");

    image = new MockMultipartFile(
        "file",
        "test-image.jpg",
        MediaType.IMAGE_JPEG_VALUE,
        "Test Image Content".getBytes()
    );

    promotion = new Promotion();
    promotion.setId(1L);
    promotion.setProduct(product);
    promotion.setDescription("Description Test");
    promotion.setPromoPrice(8.99);
    promotion.setPromoDays("Monday - Friday");
    promotion.setPromoHours("18:00 - 20:00");
    promotion.setStatus(true);

    promotionDTO = new PromotionDTO();
    promotionDTO.setProduto(productDTO);
    promotionDTO.setDescricao("Description Test");
    promotionDTO.setPromocao_preco(8.99);
    promotionDTO.setPromocao_dias("Monday - Friday");
    promotionDTO.setPromocao_horas("18:00 - 20:00");
  }

  @DisplayName("Should register promotion")
  @Test
  public void testRegisterPromotion() throws Exception {
    when(promotionService.createPromotion(any(Promotion.class))).thenReturn(promotionDTO);

    mockMvc.perform(post("/api/promotion")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(promotion)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.produto.nome").value("Test Product"))
        .andExpect(jsonPath("$.descricao").value("Description Test"))
        .andExpect(jsonPath("$.promocao_preco").value(8.99))
        .andExpect(jsonPath("$.promocao_dias").value("Monday - Friday"))
        .andExpect(jsonPath("$.promocao_horas").value("18:00 - 20:00"));
  }

  @DisplayName("Should display all promotions")
  @Test
  public void testDisplayAllPromotions() throws Exception {
    List<PromotionDTO> promotionDTOList = new ArrayList<>();
    promotionDTOList.add(promotionDTO);

    when(promotionService.listAllPromotions()).thenReturn(promotionDTOList);

    mockMvc.perform(get("/api/promotion")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].produto.nome").value("Test Product"))
        .andExpect(jsonPath("$.[0].descricao").value("Description Test"))
        .andExpect(jsonPath("$.[0].promocao_preco").value(8.99))
        .andExpect(jsonPath("$.[0].promocao_dias").value("Monday - Friday"))
        .andExpect(jsonPath("$.[0].promocao_horas").value("18:00 - 20:00"));
  }

  @DisplayName("Should return a exception")
  @Test
  public void testNoHasPromotions() throws Exception {
    doThrow(HandleNoHasPromotions.class).when(promotionService).listAllPromotions();
    mockMvc.perform(get("/api/promotion"))
        .andExpect(status().isNotFound());
  }

  @DisplayName("Should find promotion by ID")
  @Test
  public void testFindPromotionByID() throws Exception {
    when(promotionService.findPromotionById(1L)).thenReturn(Optional.of(promotion));

    mockMvc.perform(get("/api/promotion/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.product.name").value("Test Product"))
        .andExpect(jsonPath("$.description").value("Description Test"))
        .andExpect(jsonPath("$.promoPrice").value(8.99))
        .andExpect(jsonPath("$.promoDays").value("Monday - Friday"))
        .andExpect(jsonPath("$.promoHours").value("18:00 - 20:00"));
  }

  @DisplayName("Should return a exception if there no ID")
  @Test
  public void testIdNotFound() throws Exception {
    doThrow(HandleIDNotFound.class).when(promotionService).findPromotionById(promotion.getId());
    mockMvc.perform(get("/api/promotion/{id}", 1L))
        .andExpect(status().isNotFound());
  }

  @DisplayName("Should update promotion by ID")
  @Test
  public void testUpdatePromotionByID() throws Exception {
    promotion.setDescription("Update Description");

    when(promotionService.updatePromotionById(eq(1L), any(Promotion.class))).thenReturn(promotion);

    mockMvc.perform(put("/api/promotion/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(promotion)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.product.name").value("Test Product"))
        .andExpect(jsonPath("$.description").value("Update Description"))
        .andExpect(jsonPath("$.promoPrice").value(8.99))
        .andExpect(jsonPath("$.promoDays").value("Monday - Friday"))
        .andExpect(jsonPath("$.promoHours").value("18:00 - 20:00"));
  }

  @DisplayName("Should delete promotion by ID")
  @Test
  public void testDeletePromotionByID() throws Exception {
    when(promotionService.findPromotionById(1L)).thenReturn(Optional.of(promotion));

    mockMvc.perform(delete("/api/promotion/{id}", 1L))
        .andExpect(status().isNoContent());
  }
}
