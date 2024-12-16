package com.pedrooliveira.rangolist.repository;

import com.pedrooliveira.rangolist.model.Address;
import com.pedrooliveira.rangolist.model.Product;
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
public class ProductRepositoryTest {
  @Autowired
  private TestEntityManager testEntityManager;
  @Autowired
  private ProductRepository productRepository;

  private Product mockProduct;

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

    mockProduct = new Product();
    mockProduct.setName("Product Test");
    mockProduct.setImage("uploaded/path/image.jpg");
    mockProduct.setPrice(9.99);
    mockProduct.setCategory("Category Test");
    mockProduct.setRestaurant(mockRestaurant);
  }

  @DisplayName("Should find all active products")
  @Test
  public void testFindAllActiveProducts() {
    testEntityManager.persistAndFlush(mockProduct);

    List<Product> productList = productRepository.findAllActiveProducts();

    assertEquals(1, productList.size());
    Product foundProduct = productList.get(0);
    assertEquals(foundProduct.getId(), mockProduct.getId());
    assertEquals(foundProduct.getName(), mockProduct.getName());
    assertEquals(foundProduct.getPrice(), mockProduct.getPrice());
    assertEquals(foundProduct.getImage(), mockProduct.getImage());
    assertEquals(foundProduct.getCategory(), mockProduct.getCategory());
    assertEquals(foundProduct.getRestaurant(), mockProduct.getRestaurant());
  }

  @DisplayName("Should find product by ID if status is true")
  @Test
  public void testFindProductByIDIfStatusIsTrue() {
    testEntityManager.persistAndFlush(mockProduct);

    Optional<Product> productOptional = productRepository.findActiveProductById(mockProduct.getId());

    assertTrue(productOptional.isPresent(), "Product should be present");
    Product foundProduct = productOptional.get();
    assertThat(foundProduct.getId()).isEqualTo(mockProduct.getId());
    assertThat(foundProduct.getName()).isEqualTo(mockProduct.getName());
    assertThat(foundProduct.getPrice()).isEqualTo(mockProduct.getPrice());
    assertThat(foundProduct.getImage()).isEqualTo(mockProduct.getImage());
    assertThat(foundProduct.getCategory()).isEqualTo(mockProduct.getCategory());
    assertThat(foundProduct.getRestaurant()).isEqualTo(mockProduct.getRestaurant());
  }

  @DisplayName("Should find product by Name if status is true")
  @Test
  public void testFindProductByNameIfStatusIsTrue() {
    testEntityManager.persistAndFlush(mockProduct);

    List<Product> productList = productRepository.findActiveProductByName(mockProduct.getName());

    assertEquals(1, productList.size());
    Product foundProduct = productList.get(0);
    assertEquals(foundProduct.getId(), mockProduct.getId());
    assertEquals(foundProduct.getName(), mockProduct.getName());
    assertEquals(foundProduct.getPrice(), mockProduct.getPrice());
    assertEquals(foundProduct.getImage(), mockProduct.getImage());
    assertEquals(foundProduct.getCategory(), mockProduct.getCategory());
    assertEquals(foundProduct.getRestaurant(), mockProduct.getRestaurant());
  }

  @DisplayName("Should deactivate product by ID")
  @Test
  public void testDeativateProductByID() {
    testEntityManager.persistAndFlush(mockProduct);

    productRepository.deactivateProductById(mockProduct.getId());
    testEntityManager.flush();
    testEntityManager.clear();

    Product foundProduct = testEntityManager.find(Product.class, mockProduct.getId());
    assertFalse(foundProduct.getStatus(), "Product should be deactivated");
  }
}
