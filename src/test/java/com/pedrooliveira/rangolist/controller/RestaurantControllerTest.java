package com.pedrooliveira.rangolist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedrooliveira.rangolist.dto.AddressDTO;
import com.pedrooliveira.rangolist.dto.RestaurantDTO;
import com.pedrooliveira.rangolist.exception.HandleIDNotFound;
import com.pedrooliveira.rangolist.exception.HandleNoHasRestaurants;
import com.pedrooliveira.rangolist.model.Address;
import com.pedrooliveira.rangolist.model.Restaurant;
import com.pedrooliveira.rangolist.repository.RestaurantRepository;
import com.pedrooliveira.rangolist.service.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RestaurantControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @MockBean
  private RestaurantRepository restaurantRepository;
  @MockBean
  private RestaurantService restaurantService;

  private Restaurant restaurant;
  private RestaurantDTO restaurantDTO;
  private MockMultipartFile image;

  @BeforeEach
  void config() {
    Address address = new Address();
    address.setStreet("Test Street");
    address.setCity("Test City");
    address.setState("Test State");
    address.setZipcode("00000-000");

    restaurant = new Restaurant();
    restaurant.setId(1L);
    restaurant.setName("Test Restaurant");
    restaurant.setOpeningHours("10:00 - 22:00");
    restaurant.setAddress(address);
    restaurant.setStatus(true);
    restaurant.setImage("image.jpg");

    image = new MockMultipartFile(
        "file",
        "test-image.jpg",
        MediaType.IMAGE_JPEG_VALUE,
        "Test Image Content".getBytes()
    );

    AddressDTO addressDTO = new AddressDTO();
    addressDTO.setRua("Test Street");
    addressDTO.setCidade("Test City");
    addressDTO.setEstado("Test State");
    addressDTO.setCep("00000-000");

    restaurantDTO = new RestaurantDTO();
    restaurantDTO.setNome("Test Restaurant");
    restaurantDTO.setFuncionamento("10:00 - 22:00");
    restaurantDTO.setEndereco(addressDTO);
    restaurantDTO.setImagem("image.jpg");
  }

  @DisplayName("Should register a restaurant with image")
  @Test
  public void testRegisterRestaurantWithImage() throws Exception {
    MockMultipartFile restaurantJson = new MockMultipartFile(
        "restaurant",
        "image",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(restaurant)
    );

    when(restaurantService.createRestaurantWithImage(Mockito.any(Restaurant.class), Mockito.any(MockMultipartFile.class)))
        .thenReturn(restaurant);
    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/restaurant")
        .file(image)
            .param("name", restaurant.getName())
            .param("openingHours", restaurant.getOpeningHours())
            .param("address.street", restaurant.getAddress().getStreet())
            .param("address.city", restaurant.getAddress().getCity())
            .param("address.state", restaurant.getAddress().getState())
            .param("address.zipcode", restaurant.getAddress().getZipcode())
        .contentType(MediaType.MULTIPART_FORM_DATA))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.nome").value(restaurant.getName()))
        .andExpect(jsonPath("$.funcionamento").value(restaurant.getOpeningHours()))
        .andExpect(jsonPath("$.endereco.rua").value(restaurant.getAddress().getStreet()))
        .andExpect(jsonPath("$.endereco.cidade").value(restaurant.getAddress().getCity()))
        .andExpect(jsonPath("$.endereco.estado").value(restaurant.getAddress().getState()))
        .andExpect(jsonPath("$.endereco.cep").value(restaurant.getAddress().getZipcode()))
        .andExpect(jsonPath("$.imagem").value(restaurant.getImage()));
  }

  @DisplayName("Should display all restaurants")
  @Test
  public void testDisplayAllRestaurants() throws Exception {
    List<RestaurantDTO> restaurantDTOList = new ArrayList<>();
    restaurantDTOList.add(restaurantDTO);

    when(restaurantService.listAllRestaurant()).thenReturn(restaurantDTOList);
    mockMvc.perform(get("/api/restaurant")
          .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].nome").value("Test Restaurant"))
        .andExpect(jsonPath("$.[0].funcionamento").value("10:00 - 22:00"))
        .andExpect(jsonPath("$.[0].endereco.rua").value("Test Street"))
        .andExpect(jsonPath("$.[0].endereco.cidade").value("Test City"))
        .andExpect(jsonPath("$.[0].endereco.estado").value("Test State"))
        .andExpect(jsonPath("$.[0].endereco.cep").value("00000-000"))
        .andExpect(jsonPath("$.[0].imagem").value("image.jpg"));
  }

  @DisplayName("Should return a exception if there no restaurants")
  @Test
  public void testNoHasRestaurants() throws Exception {
    doThrow(HandleNoHasRestaurants.class).when(restaurantService).listAllRestaurant();
    mockMvc.perform(get("/api/restaurant"))
        .andExpect(status().isNotFound());
  }

  @DisplayName("Should find restaurant by ID")
  @Test
  public void testFindRestaurantByID() throws Exception {
    when(restaurantService.findRestaurantById(1L)).thenReturn(Optional.of(restaurant));
    mockMvc.perform(get("/api/restaurant/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.name").value("Test Restaurant"))
        .andExpect(jsonPath("$.openingHours").value("10:00 - 22:00"))
        .andExpect(jsonPath("$.address.street").value("Test Street"))
        .andExpect(jsonPath("$.address.city").value("Test City"))
        .andExpect(jsonPath("$.address.state").value("Test State"))
        .andExpect(jsonPath("$.address.zipcode").value("00000-000"))
        .andExpect(jsonPath("$.image").value("image.jpg"));
  }

  @DisplayName("Should return a exception if theres is no id")
  @Test
  public void testIdNotFound() throws Exception {

    doThrow(HandleIDNotFound.class).when(restaurantService).findRestaurantById(restaurant.getId());
    mockMvc.perform(get("/api/restaurant/{id}", 1L))
        .andExpect(status().isNotFound());
  }

  @DisplayName("Should find restaurant by Name")
  @Test
  public void testFindRestaurantByName() throws Exception {
    List<RestaurantDTO> restaurantDTOList = new ArrayList<>();
    restaurantDTOList.add(restaurantDTO);

    String paramName = "name";
    String paramValue = "Test Restaurant";

    when(restaurantService.findRestaurantByName("Test Restaurant")).thenReturn(restaurantDTOList);
    mockMvc.perform(get("/api/restaurant/find")
            .param(paramName, paramValue)
    .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].nome").value("Test Restaurant"))
        .andExpect(jsonPath("$.[0].funcionamento").value("10:00 - 22:00"))
        .andExpect(jsonPath("$.[0].endereco.rua").value("Test Street"))
        .andExpect(jsonPath("$.[0].endereco.cidade").value("Test City"))
        .andExpect(jsonPath("$.[0].endereco.estado").value("Test State"))
        .andExpect(jsonPath("$.[0].endereco.cep").value("00000-000"))
        .andExpect(jsonPath("$.[0].imagem").value("image.jpg"));
  }

  @DisplayName("Should update a restaurant by Id")
  @Test
  public void testUpdateRestaurantByID() throws Exception {
    restaurant.setName("Update Restaurant");

    when(restaurantService.updateRestaurantById(eq(1L), any(Restaurant.class)))
        .thenReturn(restaurant);

    mockMvc.perform(put("/api/restaurant/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(restaurant)))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.name").value("Update Restaurant"))
        .andExpect(jsonPath("$.openingHours").value("10:00 - 22:00"))
        .andExpect(jsonPath("$.address.street").value("Test Street"))
        .andExpect(jsonPath("$.address.city").value("Test City"))
        .andExpect(jsonPath("$.address.state").value("Test State"))
        .andExpect(jsonPath("$.address.zipcode").value("00000-000"))
        .andExpect(jsonPath("$.image").value("image.jpg"));
  }

  @DisplayName("Should delede restaurant by ID")
  @Test
  public void testDeleteRestaurantByID() throws Exception {
    when(restaurantService.findRestaurantById(1L)).thenReturn(Optional.of(restaurant));

    mockMvc.perform(delete("/api/restaurant/{id}", 1L))
        .andExpect(status().isNoContent());
  }
}
