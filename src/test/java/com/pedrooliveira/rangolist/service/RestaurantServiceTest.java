package com.pedrooliveira.rangolist.service;

import com.pedrooliveira.rangolist.dto.RestaurantDTO;
import com.pedrooliveira.rangolist.exception.*;
import com.pedrooliveira.rangolist.mapper.RestaurantMapper;
import com.pedrooliveira.rangolist.model.Address;
import com.pedrooliveira.rangolist.model.Restaurant;
import com.pedrooliveira.rangolist.repository.RestaurantRepository;
import com.pedrooliveira.rangolist.utils.UploadUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceTest {
  @Mock
  private RestaurantRepository restaurantRepository;

  @Mock
  private RestaurantMapper restaurantMapper;

  @Mock
  private Validations validations;

  @Mock
  private UploadUtil uploadUtil;

  @InjectMocks
  private RestaurantService restaurantService;

  @BeforeEach
  void config() {
    MockitoAnnotations.openMocks(this);
  }

  @DisplayName("Should validate new restaurant")
  @Test
  public void testValidateNewRestaurant() {
    Address address = new Address();
    address.setStreet("stree test");
    address.setCity("city test");
    address.setState("state test");
    address.setZipcode("00000-000");

    Restaurant newRestaurant = new Restaurant();
    newRestaurant.setName("Test Name");
    newRestaurant.setOpeningHours("00:00 - 00:00");
    newRestaurant.setAddress(address);

    Restaurant savedRestaurant = new Restaurant();
    savedRestaurant.setName("Test Name");
    savedRestaurant.setOpeningHours("00:00 - 00:00");
    savedRestaurant.setAddress(address);

    when(validations.isNameValid(newRestaurant.getName())).thenReturn(true);
    when(validations.isNameCount(newRestaurant.getName())).thenReturn(true);
    when(validations.isOpeningHoursValid(newRestaurant.getOpeningHours())).thenReturn(true);
    when(validations.isAddressValid(newRestaurant.getAddress())).thenReturn(true);

    when(restaurantRepository.save(Mockito.any(Restaurant.class))).thenReturn(savedRestaurant);

    Restaurant result = restaurantService.validateAndMapRestaurant(newRestaurant);

    assertNotNull(result);
    assertEquals("Test Name", result.getName());
    assertEquals("00:00 - 00:00", result.getOpeningHours());
    assertEquals(address, result.getAddress());

    Mockito.verify(validations).isNameValid(newRestaurant.getName());
    Mockito.verify(validations).isNameCount(newRestaurant.getName());
    Mockito.verify(validations).isOpeningHoursValid(newRestaurant.getOpeningHours());
    Mockito.verify(validations).isAddressValid(newRestaurant.getAddress());
    Mockito.verify(restaurantRepository, Mockito.times(1)).save(Mockito.any(Restaurant.class));
  }

  @DisplayName("Should register a new restaurant with a image")
  @Test
  public void testRegisterRestaurantWithImage() {
    Address address = new Address();
    address.setStreet("stree test");
    address.setCity("city test");
    address.setState("state test");
    address.setZipcode("00000-000");

    Restaurant newRestaurant = new Restaurant();
    newRestaurant.setName("Test Name");
    newRestaurant.setOpeningHours("00:00 - 00:00");
    newRestaurant.setAddress(address);

    Restaurant validateRestaurant = new Restaurant();
    validateRestaurant.setName("Test Name");
    validateRestaurant.setOpeningHours("00:00 - 00:00");
    validateRestaurant.setAddress(address);

    MockMultipartFile mockImage = new MockMultipartFile(
        "file",
        "image.jpg",
        "image/jpeg",
        "test image content".getBytes()
    );

    Restaurant savedRestaurant = new Restaurant();
    savedRestaurant.setName("Test Name");
    savedRestaurant.setOpeningHours("00:00 - 00:00");
    savedRestaurant.setAddress(address);
    savedRestaurant.setImage("uploaded/path/to/image.jpg");

    when(validations.isNameValid(newRestaurant.getName())).thenReturn(true);
    when(validations.isNameCount(newRestaurant.getName())).thenReturn(true);
    when(validations.isOpeningHoursValid(newRestaurant.getOpeningHours())).thenReturn(true);
    when(validations.isAddressValid(newRestaurant.getAddress())).thenReturn(true);

    when(restaurantRepository.save(Mockito.any(Restaurant.class))).thenReturn(savedRestaurant);

    try (MockedStatic<UploadUtil> mockedUploadUtil = Mockito.mockStatic(UploadUtil.class)) {
      mockedUploadUtil.when(() -> UploadUtil.saveFile(mockImage)).thenReturn("uploaded/path/to/image.jpg");

      Restaurant result = restaurantService.createRestaurantWithImage(newRestaurant, mockImage);

      assertNotNull(result);
      assertEquals("Test Name", result.getName());
      assertEquals("00:00 - 00:00", result.getOpeningHours());
      assertEquals(address, result.getAddress());
      assertEquals("uploaded/path/to/image.jpg", result.getImage());

      Mockito.verify(validations).isNameValid(newRestaurant.getName());
      Mockito.verify(validations).isNameCount(newRestaurant.getName());
      Mockito.verify(validations).isOpeningHoursValid(newRestaurant.getOpeningHours());
      Mockito.verify(validations).isAddressValid(newRestaurant.getAddress());
      Mockito.verify(restaurantRepository, Mockito.times(2)).save(Mockito.any(Restaurant.class));
    }
  }

  @DisplayName("Should throw exception when image is not provided")
  @Test
  public void testCreateRestaurantWithImageThrowWhenImageIsNull() {
    Restaurant newRestaurant = new Restaurant();
    newRestaurant.setName("Test Name");

    HandleNoHasFile exception = assertThrows(
        HandleNoHasFile.class,
        () -> restaurantService.createRestaurantWithImage(newRestaurant, null)
    );
    assertEquals("The file is required and was not provided.", exception.getMessage());
  }

  @DisplayName("Should throw exception when upload fails")
  @Test
  public void testCreateRestaurantWithImageThrowsWhenUploadFails() {
    Restaurant newRestaurant = new Restaurant();
    newRestaurant.setName("Test Name");
    newRestaurant.setOpeningHours("00:00 - 00:00");

    MockMultipartFile mockFile = Mockito.mock(MockMultipartFile.class);
    when(mockFile.isEmpty()).thenReturn(false);

    try (MockedStatic<UploadUtil> mockedUploadUtil = Mockito.mockStatic(UploadUtil.class)) {
      mockedUploadUtil.when(() -> UploadUtil.saveFile(mockFile))
          .thenThrow(new IOException("Uploaded Failed"));

      assertThrows(HandleNoHasFile.class, () -> {
        restaurantService.createRestaurantWithImage(newRestaurant, mockFile);
      });
    }
  }

  @DisplayName("Should return a list of restaurants")
  @Test
  public void testListAllRestaurants() {
    Restaurant restaurant1 = new Restaurant();
    Restaurant restaurant2 = new Restaurant();

    List<Restaurant> restaurantList = Arrays.asList(restaurant1, restaurant2);

    when(restaurantRepository.findAllActiveRestaurants()).thenReturn(restaurantList);

    List<RestaurantDTO> restaurantDTOList = restaurantService.listAllRestaurant();

    assertEquals(2, restaurantDTOList.size());
  }

  @DisplayName("Should return an exception if there is no restaurants")
  @Test
  public void testNoHasRestaurants() {
    when(restaurantRepository.findAllActiveRestaurants()).thenReturn(new ArrayList<>());

    assertThrows(HandleNoHasRestaurants.class, () -> {
      restaurantService.listAllRestaurant();
    });
  }

  @DisplayName("Should return a restaurant by ID")
  @Test
  public void testFindRestaurantByID() {
    Restaurant restaurant = new Restaurant();
    restaurant.setId(1L);

    when(restaurantRepository.findActiveRestaurantById(restaurant.getId())).thenReturn(Optional.of(restaurant));

    Optional<Restaurant> result = restaurantService.findRestaurantById(1L);

    assertTrue(result.isPresent());
    assertEquals(restaurant, result.get());

    verify(restaurantRepository, times(1)).findActiveRestaurantById(1L);
  }

  @DisplayName("Should return exception when id no found")
  @Test
  public void testIdNotFound() {
    Long id = 1L;

    assertThrows(HandleIDNotFound.class, () -> {
      restaurantService.findRestaurantById(id);
    });
  }

  @DisplayName("Should return a restaurant by name")
  @Test
  public void testFindRestaurantByName() {
    Restaurant restaurant = new Restaurant();
    restaurant.setName("Name");

    List<Restaurant> restaurantList = Arrays.asList(restaurant);

    when(restaurantRepository.findActiveRestaurantByName(restaurant.getName())).thenReturn(restaurantList);

    RestaurantDTO restaurantDTO = new RestaurantDTO();
    restaurantDTO.setNome("Name");

    when(restaurantMapper.toRestaurantDto(Mockito.any(Restaurant.class))).thenReturn(restaurantDTO);

    List<RestaurantDTO> result = restaurantService.findRestaurantByName("Name");

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Name", result.get(0).getNome());

    Mockito.verify(restaurantRepository, Mockito.times(1)).findActiveRestaurantByName("Name");
  }

  @DisplayName("Should return a exception when name not found")
  @Test
  public void testNameNotFound() {
    String name = "Name";

    assertThrows(HandleNameNotFound.class, () -> {
      restaurantService.findRestaurantByName(name);
    });
  }

  @DisplayName("Should update a restaurant by ID")
  @Test
  public void testUpdateRestaurantById() {
    Address address = new Address();
    address.setStreet("stree test");
    address.setCity("city test");
    address.setState("state test");
    address.setZipcode("00000-000");

    Restaurant existingRestaurant = new Restaurant();
    existingRestaurant.setId(1L);
    existingRestaurant.setName("Test Name");
    existingRestaurant.setOpeningHours("00:00 - 00:00");
    existingRestaurant.setAddress(address);

    Restaurant updatedRestaurant = new Restaurant();
    updatedRestaurant.setName("Updated Name");
    updatedRestaurant.setOpeningHours("09:00 - 16:00");
    updatedRestaurant.setAddress(address);

    when(restaurantRepository.findActiveRestaurantById(existingRestaurant.getId())).thenReturn(Optional.of(existingRestaurant));

    when(restaurantRepository.save(existingRestaurant)).thenReturn(existingRestaurant);

    Restaurant restaurant = restaurantService.updateRestaurantById(existingRestaurant.getId(), updatedRestaurant);

    assertEquals(updatedRestaurant.getName(), restaurant.getName());
    assertEquals(updatedRestaurant.getOpeningHours(), restaurant.getOpeningHours());
    assertEquals(updatedRestaurant.getAddress(), restaurant.getAddress());

    verify(restaurantRepository).save(existingRestaurant);
  }

  @DisplayName("Should delete restaurant by ID")
  @Test
  public void testDeleteRestaurantByID() {
    Restaurant restaurantExist = new Restaurant();
    restaurantExist.setId(1L);

    Optional<Restaurant> restaurantOptional = Optional.of(restaurantExist);

    when(restaurantRepository.findActiveRestaurantById(restaurantExist.getId())).thenReturn(restaurantOptional);

    restaurantService.deleteRestaurantById(restaurantExist.getId());
  }
}
