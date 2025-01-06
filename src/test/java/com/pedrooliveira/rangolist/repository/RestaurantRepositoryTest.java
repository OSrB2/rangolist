package com.pedrooliveira.rangolist.repository;

import com.pedrooliveira.rangolist.model.Address;
import com.pedrooliveira.rangolist.model.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class RestaurantRepositoryTest {
  @Autowired
  private TestEntityManager testEntityManager;
  @Autowired
  private RestaurantRepository restaurantRepository;

  private Restaurant mockRestaurant;

  @BeforeEach
  void config() {
    Address mockAddress = new Address();
    mockAddress.setStreet("Street Test");
    mockAddress.setCity("City Test");
    mockAddress.setState("State Test");
    mockAddress.setZipcode("00000-000");

    mockRestaurant = new Restaurant();
    mockRestaurant.setName("Name Test");
    mockRestaurant.setImage("uploaded/path/image.jpg");
    mockRestaurant.setAddress(mockAddress);
    mockRestaurant.setOpeningHours("01:00 - 02:00");
    mockRestaurant.setStatus(true);
  }

  @DisplayName("Should find all active restaurants")
  @Test
  public void testFindAllActiveRestaurants() {
    testEntityManager.persistAndFlush(mockRestaurant);

    List<Restaurant> restaurantList = restaurantRepository.findAllActiveRestaurants();

    assertEquals(1, restaurantList.size());
    Restaurant foundRestaurant = restaurantList.get(0);
    assertEquals(foundRestaurant.getId(), mockRestaurant.getId());
    assertEquals(foundRestaurant.getImage(), mockRestaurant.getImage());
    assertEquals(foundRestaurant.getAddress(), mockRestaurant.getAddress());
    assertEquals(foundRestaurant.getOpeningHours(), mockRestaurant.getOpeningHours());
    assertEquals(foundRestaurant.getStatus(), mockRestaurant.getStatus());
  }

  @DisplayName("Should find restaurant by ID if status is true")
  @Test
  public void testFindRestaurantByIDIfStatusIsTrue() {
    testEntityManager.persistAndFlush(mockRestaurant);

    Optional<Restaurant> restaurantOptional = restaurantRepository.findActiveRestaurantById(mockRestaurant.getId());

    assertTrue(restaurantOptional.isPresent(), "Restaurante should be present");
    Restaurant foundRestaurant = restaurantOptional.get();
    assertThat(foundRestaurant.getId()).isEqualTo(mockRestaurant.getId());
    assertThat(foundRestaurant.getName()).isEqualTo(mockRestaurant.getName());
    assertThat(foundRestaurant.getAddress()).isEqualTo(mockRestaurant.getAddress());
    assertThat(foundRestaurant.getOpeningHours()).isEqualTo(mockRestaurant.getOpeningHours());
    assertThat(foundRestaurant.getStatus()).isEqualTo(mockRestaurant.getStatus());
  }

  @DisplayName("Should find restaurant by Name if statis is true")
  @Test
  public void testFindRestaurantByNameIfStatusIsTrue() {
    testEntityManager.persistAndFlush(mockRestaurant);

    List<Restaurant> restaurantList = restaurantRepository.findActiveRestaurantByName(mockRestaurant.getName());

    assertEquals(1, restaurantList.size());
    Restaurant foundRestaurant = restaurantList.get(0);
    assertEquals(foundRestaurant.getId(), mockRestaurant.getId());
    assertEquals(foundRestaurant.getImage(), mockRestaurant.getImage());
    assertEquals(foundRestaurant.getAddress(), mockRestaurant.getAddress());
    assertEquals(foundRestaurant.getOpeningHours(), mockRestaurant.getOpeningHours());
    assertEquals(foundRestaurant.getStatus(), mockRestaurant.getStatus());
  }

  @DisplayName("Should deactivate restaurant by ID")
  @Test
  public void testDeactivateRestaurantByID() {
    testEntityManager.persistAndFlush(mockRestaurant);

    restaurantRepository.deactivateRestaurantById(mockRestaurant.getId());
    testEntityManager.flush();
    testEntityManager.clear();

    Restaurant foundRestaurant = testEntityManager.find(Restaurant.class, mockRestaurant.getId());
    assertFalse(foundRestaurant.getStatus(), "Restaurant should be deactivated");
  }
}
