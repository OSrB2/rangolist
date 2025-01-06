package com.pedrooliveira.rangolist.repository;

import com.pedrooliveira.rangolist.model.Address;
import com.pedrooliveira.rangolist.model.Product;
import com.pedrooliveira.rangolist.model.Promotion;
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
public class PromotionRepositoryTest {
  @Autowired
  private TestEntityManager testEntityManager;
  @Autowired
  private PromotionRepository promotionRepository;

  private Promotion mockPromotion;

  @BeforeEach
  void config() {
    Address mockAddress = new Address();
    mockAddress.setStreet("Street Test");
    mockAddress.setCity("City Test");
    mockAddress.setState("State Test");
    mockAddress.setZipcode("00000-000");

    Restaurant mockRestaurant = new Restaurant();
    mockRestaurant.setName("Name Test");
    mockRestaurant.setImage("uploaded/path/image.jpg");
    mockRestaurant.setAddress(mockAddress);
    mockRestaurant.setOpeningHours("01:00 - 02:00");
    mockRestaurant.setStatus(true);

    mockRestaurant = testEntityManager.persistAndFlush(mockRestaurant);

    Product mockProduct = new Product();
    mockProduct.setName("Product Test");
    mockProduct.setImage("uploaded/path/image.jpg");
    mockProduct.setPrice(9.99);
    mockProduct.setCategory("Category Test");
    mockProduct.setRestaurant(mockRestaurant);

    mockProduct = testEntityManager.persistAndFlush(mockProduct);

    mockPromotion = new Promotion();
    mockPromotion.setDescription("Promotion Test");
    mockPromotion.setPromoPrice(9.99);
    mockPromotion.setPromoDays("Monday - Friday");
    mockPromotion.setPromoHours("20:00 - 21:00");
    mockPromotion.setProduct(mockProduct);
    mockPromotion.setStatus(true);

  }

  @DisplayName("Should find all active promotinos")
  @Test
  public void testFindAllActivePromotions() {
    testEntityManager.persistAndFlush(mockPromotion);

    List<Promotion> promotionList = promotionRepository.findAllActivePromotions();

    assertEquals(1, promotionList.size());
    Promotion foundPromotion = promotionList.get(0);
    assertEquals(foundPromotion.getId(), mockPromotion.getId());
    assertEquals(foundPromotion.getPromoPrice(), mockPromotion.getPromoPrice());
    assertEquals(foundPromotion.getPromoDays(), mockPromotion.getPromoDays());
    assertEquals(foundPromotion.getPromoHours(), mockPromotion.getPromoHours());
    assertEquals(foundPromotion.getProduct(), mockPromotion.getProduct());
    assertEquals(foundPromotion.getStatus(), mockPromotion.getStatus());
  }

  @DisplayName("Should find promotion by ID if status is true")
  @Test
  public void testFindPromotionByIdIfStatusIsTrue() {
    testEntityManager.persistAndFlush(mockPromotion);

    Optional<Promotion> promotionOptional = promotionRepository.findActivePromotionById(mockPromotion.getId());

    assertTrue(promotionOptional.isPresent(), "Promotion should be present");
    Promotion foundPromotion = promotionOptional.get();
    assertThat(foundPromotion.getId()).isEqualTo(mockPromotion.getId());
    assertThat(foundPromotion.getPromoPrice()).isEqualTo(mockPromotion.getPromoPrice());
    assertThat(foundPromotion.getPromoDays()).isEqualTo(mockPromotion.getPromoDays());
    assertThat(foundPromotion.getPromoHours()).isEqualTo(mockPromotion.getPromoHours());
    assertThat(foundPromotion.getProduct()).isEqualTo(mockPromotion.getProduct());
    assertThat(foundPromotion.getStatus()).isEqualTo(mockPromotion.getStatus());
  }

  @DisplayName("Should deactivate promotion by ID")
  @Test
  public void testDeativatePromotionByID() {
    testEntityManager.persistAndFlush(mockPromotion);

    promotionRepository.deactivatePromotionById(mockPromotion.getId());
    testEntityManager.flush();
    testEntityManager.clear();

    Promotion foundPromotion = testEntityManager.find(Promotion.class, mockPromotion.getId());
    assertFalse(foundPromotion.getStatus(), "Promotion should be deativate");
  }
}
