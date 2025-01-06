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

  private Address mockAddress;
  private Restaurant mockRestaurant;
  private Restaurant savedRestaurant;
  private MockMultipartFile mockImage;

  @BeforeEach
  void setUp() {
    mockAddress = new Address();
    mockAddress.setStreet("Street Test");
    mockAddress.setCity("City Test");
    mockAddress.setState("State Test");
    mockAddress.setZipcode("00000-000");

    mockRestaurant = new Restaurant();
    mockRestaurant.setName("Test Name");
    mockRestaurant.setOpeningHours("00:00 - 00:00");
    mockRestaurant.setAddress(mockAddress);

    savedRestaurant = new Restaurant();
    savedRestaurant.setName("Test Name");
    savedRestaurant.setOpeningHours("00:00 - 00:00");
    savedRestaurant.setAddress(mockAddress);

    mockImage = new MockMultipartFile(
        "file",
        "image.jpg",
        "image/jpeg",
        "test image content".getBytes()
    );
  }

  @DisplayName("Should validate new restaurant")
  @Test
  public void testValidateNewRestaurant() {
    when(validations.isNameValid(mockRestaurant.getName())).thenReturn(true);
    when(validations.isNameCount(mockRestaurant.getName())).thenReturn(true);
    when(validations.isOpeningHoursValid(mockRestaurant.getOpeningHours())).thenReturn(true);
    when(validations.isAddressValid(mockRestaurant.getAddress())).thenReturn(true);
    when(restaurantRepository.save(any(Restaurant.class))).thenReturn(savedRestaurant);

    Restaurant result = restaurantService.validateAndMapRestaurant(mockRestaurant);

    assertNotNull(result);
    assertEquals(mockRestaurant.getName(), result.getName());
    assertEquals(mockRestaurant.getOpeningHours(), result.getOpeningHours());
    assertEquals(mockRestaurant.getAddress(), result.getAddress());

    verify(validations).isNameValid(mockRestaurant.getName());
    verify(validations).isNameCount(mockRestaurant.getName());
    verify(validations).isOpeningHoursValid(mockRestaurant.getOpeningHours());
    verify(validations).isAddressValid(mockRestaurant.getAddress());
    verify(restaurantRepository).save(any(Restaurant.class));
  }

  @DisplayName("Should register a new restaurant with an image")
  @Test
  public void testRegisterRestaurantWithImage() {
    when(validations.isNameValid(mockRestaurant.getName())).thenReturn(true);
    when(validations.isNameCount(mockRestaurant.getName())).thenReturn(true);
    when(validations.isOpeningHoursValid(mockRestaurant.getOpeningHours())).thenReturn(true);
    when(validations.isAddressValid(mockRestaurant.getAddress())).thenReturn(true);
    when(restaurantRepository.save(any(Restaurant.class))).thenReturn(savedRestaurant);

    try (MockedStatic<UploadUtil> mockedUploadUtil = Mockito.mockStatic(UploadUtil.class)) {
      mockedUploadUtil.when(() -> UploadUtil.saveFile(mockImage)).thenReturn("uploaded/path/to/image.jpg");

      Restaurant result = restaurantService.createRestaurantWithImage(mockRestaurant, mockImage);

      assertNotNull(result);
      assertEquals("Test Name", result.getName());
      assertEquals("00:00 - 00:00", result.getOpeningHours());
      assertEquals(mockAddress, result.getAddress());
      assertEquals("uploaded/path/to/image.jpg", result.getImage());

      verify(validations).isNameValid(mockRestaurant.getName());
      verify(validations).isNameCount(mockRestaurant.getName());
      verify(validations).isOpeningHoursValid(mockRestaurant.getOpeningHours());
      verify(validations).isAddressValid(mockRestaurant.getAddress());
      verify(restaurantRepository, times(2)).save(any(Restaurant.class));
    }
  }

  @DisplayName("Should throw exception when image is not provided")
  @Test
  public void testCreateRestaurantWithImageThrowWhenImageIsNull() {
    HandleNoHasFile exception = assertThrows(
        HandleNoHasFile.class,
        () -> restaurantService.createRestaurantWithImage(mockRestaurant, null)
    );
    assertEquals("The file is required and was not provided.", exception.getMessage());
  }

  @DisplayName("Should throw exception when upload fails")
  @Test
  public void testCreateRestaurantWithImageThrowsWhenUploadFails() {
    try (MockedStatic<UploadUtil> mockedUploadUtil = Mockito.mockStatic(UploadUtil.class)) {
      mockedUploadUtil.when(() -> UploadUtil.saveFile(mockImage))
          .thenThrow(new IOException("Uploaded Failed"));

      assertThrows(HandleNoHasFile.class, () -> {
        restaurantService.createRestaurantWithImage(mockRestaurant, mockImage);
      });
    }
  }

  @DisplayName("Should return a list of restaurants")
  @Test
  public void testListAllRestaurants() {
    List<Restaurant> restaurantList = Arrays.asList(mockRestaurant, savedRestaurant);
    when(restaurantRepository.findAllActiveRestaurants()).thenReturn(restaurantList);

    List<RestaurantDTO> restaurantDTOList = restaurantService.listAllRestaurant();

    assertEquals(2, restaurantDTOList.size());
  }

  @DisplayName("Should throw exception if there are no restaurants")
  @Test
  public void testNoHasRestaurants() {
    when(restaurantRepository.findAllActiveRestaurants()).thenReturn(new ArrayList<>());

    assertThrows(HandleNoHasRestaurants.class, () -> restaurantService.listAllRestaurant());
  }

  @DisplayName("Should return a restaurant by ID")
  @Test
  public void testFindRestaurantByID() {
    mockRestaurant.setId(1L);
    when(restaurantRepository.findActiveRestaurantById(1L)).thenReturn(Optional.of(mockRestaurant));

    Optional<Restaurant> result = restaurantService.findRestaurantById(1L);

    assertTrue(result.isPresent());
    assertEquals(mockRestaurant, result.get());
    verify(restaurantRepository).findActiveRestaurantById(1L);
  }

  @DisplayName("Should throw exception when ID not found")
  @Test
  public void testIdNotFound() {
    when(restaurantRepository.findActiveRestaurantById(1L)).thenReturn(Optional.empty());

    assertThrows(HandleIDNotFound.class, () -> restaurantService.findRestaurantById(1L));
  }
}
