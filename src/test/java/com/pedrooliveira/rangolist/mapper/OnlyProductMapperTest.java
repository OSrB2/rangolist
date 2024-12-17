package com.pedrooliveira.rangolist.mapper;

import com.pedrooliveira.rangolist.dto.OnlyProductDTO;
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
public class OnlyProductMapperTest {
  @Autowired
  private OnlyProductMapper onlyProductMapper;

  private Product product;
  private Restaurant restaurant;
  private Address address;

  @BeforeEach
  void config() {
    address = new Address();
    address.setStreet("Stree test");
    address.setCity("City test");
    address.setState("State test");
    address.setZipcode("00000-000");

    restaurant = new Restaurant();
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

  @DisplayName("Should map Product to OnlyProductDTO")
  @Test
  public void testProductToOnlyProductDTO() {
    OnlyProductDTO onlyProductDTO = onlyProductMapper.toOnlyProductDTO(product);

    assertNotNull(onlyProductDTO, "OnlyProductDTO should be not null");

    assertEquals(product.getName(), onlyProductDTO.getNome(), "Name should be mapped correctly");
    assertEquals(product.getCategory(), onlyProductDTO.getCategoria(), "Category should be mapped correctly");
    assertEquals(product.getImage(), onlyProductDTO.getImagem(), "Image should be mapped correctly");
  }
}
