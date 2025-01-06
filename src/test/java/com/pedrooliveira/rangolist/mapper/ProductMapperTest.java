package com.pedrooliveira.rangolist.mapper;

import com.pedrooliveira.rangolist.dto.ProductDTO;
import com.pedrooliveira.rangolist.model.Address;
import com.pedrooliveira.rangolist.model.Product;
import com.pedrooliveira.rangolist.model.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ProductMapperTest {
  @Autowired
  private ProductMapper productMapper;

  private Product product;

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

    product = new Product();
    product.setId(1L);
    product.setName("Name Test");
    product.setCategory("Category Test");
    product.setPrice(9.99);
    product.setImage("uploaded/path/image.jpg");
    product.setRestaurant(restaurant);
    product.setStatus(true);
  }

  @DisplayName("Should map Product to ProductDTO")
  @Test
  public void testProductToProductDTO() {
    ProductDTO productDTO = productMapper.toProductDTO(product);

    assertNotNull(productDTO, "ProductDTO should be not null");

    assertEquals(product.getImage(), productDTO.getImagem(), "Image should be mapped correctly");
    assertEquals(product.getName(), productDTO.getNome(), "Name should be mapped correctly");
    assertEquals(product.getPrice(), productDTO.getPreco(), "Price should be mapped correctly");
    assertEquals(product.getCategory(), productDTO.getCategoria(), "Category should be mapped correctly");
    assertNotNull(productDTO.getRestaurante(), "RestaurantDTO should not be null");
    assertEquals(product.getRestaurant().getName(), productDTO.getRestaurante().getNome(), "Restaurant name should be mapped correctly");
    assertEquals(product.getRestaurant().getImage(), productDTO.getRestaurante().getImagem(), "Restaurant image should be mapped correctly");
    assertEquals(product.getRestaurant().getOpeningHours(), productDTO.getRestaurante().getFuncionamento(), "Restaurant opening hours should be mapped correctly");

    assertNotNull(productDTO.getRestaurante().getEndereco(), "AddressDTO should not be null");
    assertEquals(product.getRestaurant().getAddress().getStreet(), productDTO.getRestaurante().getEndereco().getRua(), "Street should be mapped correctly");
    assertEquals(product.getRestaurant().getAddress().getCity(), productDTO.getRestaurante().getEndereco().getCidade(), "City should be mapped correctly");
    assertEquals(product.getRestaurant().getAddress().getState(), productDTO.getRestaurante().getEndereco().getEstado(), "State should be mapped correctly");
    assertEquals(product.getRestaurant().getAddress().getZipcode(), productDTO.getRestaurante().getEndereco().getCep(), "ZipCode should be mapped correctly");
  }
}
