package com.pedrooliveira.rangolist.mapper;

import com.pedrooliveira.rangolist.dto.PromotionDTO;
import com.pedrooliveira.rangolist.model.Address;
import com.pedrooliveira.rangolist.model.Product;
import com.pedrooliveira.rangolist.model.Promotion;
import com.pedrooliveira.rangolist.model.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class PromotionMapperTest {
  @Autowired
  private PromotionMapper promotionMapper;

  private Promotion promotion;

  @BeforeEach
  void config() {
    Address address = new Address();
    address.setStreet("Stree test");
    address.setCity("City test");
    address.setState("State test");
    address.setZipcode("00000-000");

    Restaurant restaurant = new Restaurant();
    restaurant.setId(1L);
    restaurant.setName("Name test");
    restaurant.setImage("uploaded/path/image.jpg");
    restaurant.setAddress(address);
    restaurant.setOpeningHours("10:00 - 20:00");
    restaurant.setStatus(true);

    Product product = new Product();
    product.setId(1L);
    product.setName("Name Test");
    product.setCategory("Category Test");
    product.setPrice(9.99);
    product.setImage("uploaded/path/image.jpg");
    product.setRestaurant(restaurant);
    product.setStatus(true);

    promotion = new Promotion();
    promotion.setId(1L);
    promotion.setProduct(product);
    promotion.setDescription("Description Test");
    promotion.setPromoPrice(5.99);
    promotion.setPromoDays("Monday - Tuesday");
    promotion.setPromoHours("17:00 - 18:00");
    promotion.setStatus(true);
  }

  @DisplayName("Should map Promotion to PromotionDTO")
  @Test
  public void testPromotionToPromotionDTO() {
    PromotionDTO promotionDTO = promotionMapper.toPromotionDTO(promotion);

    assertNotNull(promotionDTO, "PromotionDTO should be not null");

    assertEquals(promotion.getDescription(), promotionDTO.getDescricao(), "Description should be mapped correctly");
    assertEquals(promotion.getPromoPrice(), promotionDTO.getPromocao_preco(), "Promotion price should be mapped correctly");
    assertEquals(promotion.getPromoDays(), promotionDTO.getPromocao_dias(), "Promotion day should be mapped correctly");
    assertEquals(promotion.getPromoHours(), promotionDTO.getPromocao_horas(), "Promotion hours should be mapped correctly");

    assertEquals(promotion.getProduct().getImage(), promotionDTO.getProduto().getImagem(), "Image should be mapped correctly");
    assertEquals(promotion.getProduct().getName(), promotionDTO.getProduto().getNome(), "Name should be mapped correctly");
    assertEquals(promotion.getProduct().getPrice(), promotionDTO.getProduto().getPreco(), "Price should be mapped correctly");
    assertEquals(promotion.getProduct().getCategory(), promotionDTO.getProduto().getCategoria(), "Category should be mapped correctly");
    assertNotNull(promotionDTO.getProduto().getRestaurante(), "RestaurantDTO should not be null");
    assertEquals(promotion.getProduct().getRestaurant().getName(), promotionDTO.getProduto().getRestaurante().getNome(), "Restaurant name should be mapped correctly");
    assertEquals(promotion.getProduct().getRestaurant().getImage(), promotionDTO.getProduto().getRestaurante().getImagem(), "Restaurant image should be mapped correctly");
    assertEquals(promotion.getProduct().getRestaurant().getOpeningHours(), promotionDTO.getProduto().getRestaurante().getFuncionamento(), "Restaurant opening hours should be mapped correctly");

    assertNotNull(promotionDTO.getProduto().getRestaurante().getEndereco(), "AddressDTO should not be null");
    assertEquals(promotion.getProduct().getRestaurant().getAddress().getStreet(), promotionDTO.getProduto().getRestaurante().getEndereco().getRua(), "Street should be mapped correctly");
    assertEquals(promotion.getProduct().getRestaurant().getAddress().getCity(), promotionDTO.getProduto().getRestaurante().getEndereco().getCidade(), "City should be mapped correctly");
    assertEquals(promotion.getProduct().getRestaurant().getAddress().getState(), promotionDTO.getProduto().getRestaurante().getEndereco().getEstado(), "State should be mapped correctly");
    assertEquals(promotion.getProduct().getRestaurant().getAddress().getZipcode(), promotionDTO.getProduto().getRestaurante().getEndereco().getCep(), "ZipCode should be mapped correctly");
  }
}
