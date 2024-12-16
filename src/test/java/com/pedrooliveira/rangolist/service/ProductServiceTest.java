package com.pedrooliveira.rangolist.service;

import com.pedrooliveira.rangolist.dto.OnlyProductDTO;
import com.pedrooliveira.rangolist.dto.ProductDTO;
import com.pedrooliveira.rangolist.exception.HandleIDNotFound;
import com.pedrooliveira.rangolist.exception.HandleNameNotFound;
import com.pedrooliveira.rangolist.exception.HandleNoHasProducts;
import com.pedrooliveira.rangolist.exception.Validations;
import com.pedrooliveira.rangolist.mapper.OnlyProductMapper;
import com.pedrooliveira.rangolist.mapper.ProductMapper;
import com.pedrooliveira.rangolist.model.Product;
import com.pedrooliveira.rangolist.model.Restaurant;
import com.pedrooliveira.rangolist.repository.ProductRepository;
import com.pedrooliveira.rangolist.repository.RestaurantRepository;
import com.pedrooliveira.rangolist.utils.UploadUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
  @Mock
  private ProductRepository productRepository;
  @Mock
  private RestaurantRepository restaurantRepository;
  @Mock
  private ProductMapper productMapper;
  @Mock
  private OnlyProductMapper onlyProductMapper;
  @Mock
  private Validations validations;
  @InjectMocks
  private ProductService productService;

  @BeforeEach
  void config() {
    MockitoAnnotations.openMocks(this);
  }

  @DisplayName("Should validade new product")
  @Test
  public void testValidateNewProduct() {
    Restaurant restaurant = new Restaurant();
    restaurant.setId(1L);
    restaurant.setName("Restaurant Name");

    Product product = new Product();
    product.setName("Test Name");
    product.setPrice(10.00);
    product.setCategory("Category Test");
    product.setRestaurant(restaurant);

    Product savedProduct = new Product();
    savedProduct.setName("Test Name");
    savedProduct.setPrice(10.00);
    savedProduct.setCategory("Category Test");
    savedProduct.setRestaurant(restaurant);

    when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
    when(validations.isNameValid(product.getName())).thenReturn(true);
    when(validations.isPriceValid(product.getPrice())).thenReturn(true);
    when(validations.isCategoryValid(product.getCategory())).thenReturn(true);
    when(productRepository.save(Mockito.any(Product.class))).thenReturn(savedProduct);

    Product result = productService.validateAndMapProduct(product);

    assertNotNull(result);
    assertEquals("Test Name", result.getName());
    assertEquals(10.00, result.getPrice());
    assertEquals("Category Test", result.getCategory());
    assertEquals(restaurant, result.getRestaurant());

    Mockito.verify(restaurantRepository).findById(1L);
    Mockito.verify(validations).isNameValid(product.getName());
    Mockito.verify(validations).isPriceValid(product.getPrice());
    Mockito.verify(validations).isCategoryValid(product.getCategory());
    Mockito.verify(productRepository).save(Mockito.any(Product.class));
  }

  @DisplayName("Should register a new product with a image")
  @Test
  public void testRegisterProductWithImage() {
    Restaurant restaurant = new Restaurant();
    restaurant.setId(1L);
    restaurant.setName("Restaurant Name");

    Product product = new Product();
    product.setName("Test Name");
    product.setPrice(10.00);
    product.setCategory("Category Test");
    product.setRestaurant(restaurant);

    Product validateProduct = new Product();
    validateProduct.setName("Test Name");
    validateProduct.setPrice(10.00);
    validateProduct.setCategory("Category Test");
    validateProduct.setRestaurant(restaurant);

    MockMultipartFile mockImage = new MockMultipartFile(
        "file",
        "image.jpg",
        "image/jpeg",
        "test image content".getBytes()
    );
    Product savedProduct = new Product();
    savedProduct.setName("Test Name");
    savedProduct.setPrice(10.00);
    savedProduct.setCategory("Category Test");
    savedProduct.setRestaurant(restaurant);

    when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
    when(validations.isNameValid(product.getName())).thenReturn(true);
    when(validations.isPriceValid(product.getPrice())).thenReturn(true);
    when(validations.isCategoryValid(product.getCategory())).thenReturn(true);
    when(productRepository.save(Mockito.any(Product.class))).thenReturn(savedProduct);

    try (MockedStatic<UploadUtil> mockedUploadUtil = Mockito.mockStatic(UploadUtil.class)) {
      mockedUploadUtil.when(() -> UploadUtil.saveFile(mockImage)).thenReturn("uploaded/path/to/image.jpg");

      Product result = productService.createProductWithImage(product, mockImage);

      assertNotNull(result);
      assertEquals("Test Name", result.getName());
      assertEquals(10.00, result.getPrice());
      assertEquals("Category Test", result.getCategory());
      assertEquals(restaurant, result.getRestaurant());
      assertEquals("uploaded/path/to/image.jpg", result.getImage());

      Mockito.verify(validations).isNameValid(product.getName());
      Mockito.verify(validations).isPriceValid(product.getPrice());
      Mockito.verify(validations).isCategoryValid(product.getCategory());
      Mockito.verify(productRepository, Mockito.times(2)).save(Mockito.any(Product.class));
    }
  }

  @DisplayName("Should return a list of products")
  @Test
  public void testListAllProducts() {
    Product product1 = new Product();
    Product product2 = new Product();

    List<Product> productList = Arrays.asList(product1, product2);

    when(productRepository.findAllActiveProducts()).thenReturn(productList);

    List<ProductDTO> productDTOList = productService.listAllProducts();

    assertEquals(2, productDTOList.size());
  }

  @DisplayName("Should return a list of products withou restaurants")
  @Test
  public void testListAllProductsWithoutRestaurants() {
    Product product1 = new Product();
    Product product2 = new Product();

    List<Product> productList = Arrays.asList(product1, product2);

    when(productRepository.findAllActiveProducts()).thenReturn(productList);

    List<OnlyProductDTO> onlyProductDTOList = productService.listProductusWithoutRestaurants();

    assertEquals(2, onlyProductDTOList.size());
  }

  @DisplayName("Should return a exception if there is no products")
  @Test
  public void testNoHasProducts() {
    when(productRepository.findAllActiveProducts()).thenReturn(new ArrayList<>());

    assertThrows(HandleNoHasProducts.class, () -> {
      productService.listAllProducts();
    });
  }

  @DisplayName("Should return a product by ID")
  @Test
  public void testFindProductByID() {
    Product product = new Product();
    product.setId(1L);

    when(productRepository.findActiveProductById(product.getId())).thenReturn(Optional.of(product));

    Optional<Product> result = productService.findProductById(1L);

    assertTrue(result.isPresent());
    assertEquals(product, result.get());

    verify(productRepository, times(1)).findActiveProductById(1L);
  }

  @DisplayName("Should return exception when ID not found")
  @Test
  public void testIdNotFound() {
    Long id = 1L;

    assertThrows(HandleIDNotFound.class, () -> {
      productService.findProductById(id);
    });
  }

  @DisplayName("Should return product by Name")
  @Test
  public void testFindProductByName() {
    Product product = new Product();
    product.setName("Name");

    List<Product> productList = Arrays.asList(product);

    when(productRepository.findActiveProductByName(product.getName())).thenReturn(productList);

    ProductDTO productDTO = new ProductDTO();
    productDTO.setNome("Name");

    when(productMapper.toProductDTO(Mockito.any(Product.class))).thenReturn(productDTO);

    List<ProductDTO> result = productService.findProductByName("Name");

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Name", result.get(0).getNome());

    Mockito.verify(productRepository, Mockito.times(1)).findActiveProductByName("Name");
  }

  @DisplayName("Should return a exception when name not found")
  @Test
  public void testNameNotFound() {
    String name = "Name";

    assertThrows(HandleNameNotFound.class, () -> {
      productService.findProductByName(name);
    });
  }

  @DisplayName("Should update a product by ID")
  @Test
  public void testUpdateProductBYID() {
    Restaurant restaurant = new Restaurant();
    restaurant.setId(1L);
    restaurant.setName("Restaurant Name");

    Product existProduct = new Product();
    existProduct.setName("Test Name");
    existProduct.setPrice(10.00);
    existProduct.setCategory("Category Test");
    existProduct.setRestaurant(restaurant);

    Product updateProduct = new Product();
    updateProduct.setName("Update Name");
    updateProduct.setPrice(15.00);
    updateProduct.setCategory("Category Test");
    updateProduct.setRestaurant(restaurant);

    when(productRepository.findActiveProductById(existProduct.getId())).thenReturn(Optional.of(existProduct));

    when(productRepository.save(existProduct)).thenReturn(existProduct);

    Product product = productService.updateProductById(existProduct.getId(), updateProduct);

    assertEquals(updateProduct.getName(), product.getName());
    assertEquals(updateProduct.getPrice(), product.getPrice());
    assertEquals(updateProduct.getCategory(), product.getCategory());
    assertEquals(updateProduct.getRestaurant(), product.getRestaurant());

    verify(productRepository).save(existProduct);
  }

  @DisplayName("Should delete product by ID")
  @Test
  public void testDeleteProductByID() {
    Product productExist = new Product();
    productExist.setId(1L);

    Optional<Product> productOptional = Optional.of(productExist);

    when(productRepository.findActiveProductById(productExist.getId())).thenReturn(productOptional);

    productService.deleteProductById(productExist.getId());
  }
}
