package com.pedrooliveira.rangolist.mapper;

import com.pedrooliveira.rangolist.dto.RestaurantDTO;
import com.pedrooliveira.rangolist.model.Address;
import com.pedrooliveira.rangolist.model.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class RestaurantMapperTest {
  @Autowired
  private RestaurantMapper restaurantMapper;

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
  }

  @DisplayName("Should map Restaurant to RestaurantDTO")
  @Test
  public void testRestaurantToRestaurantDTO() {
    RestaurantDTO restaurantDTO = restaurantMapper.toRestaurantDto(restaurant);

    assertNotNull(restaurantDTO, "RestaurantDTO should be not null");

    assertEquals(restaurant.getName(), restaurantDTO.getNome(), "Name should be mapped correctly");
    assertEquals(restaurant.getImage(), restaurantDTO.getImagem(), "Image should be mapped correctly");
    assertEquals(restaurant.getOpeningHours(), restaurantDTO.getFuncionamento(), "Opening hours should be mapped correctly");

    assertNotNull(restaurantDTO.getEndereco(), "Address should be mapped");
    assertEquals(address.getStreet(), restaurantDTO.getEndereco().getRua(), "Address should be mapped correctly");
  }
}
