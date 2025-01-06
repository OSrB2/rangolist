package com.pedrooliveira.rangolist.service;

import com.pedrooliveira.rangolist.dto.RestaurantDTO;
import com.pedrooliveira.rangolist.exception.*;
import com.pedrooliveira.rangolist.mapper.RestaurantMapper;
import com.pedrooliveira.rangolist.model.Restaurant;
import com.pedrooliveira.rangolist.repository.RestaurantRepository;
import com.pedrooliveira.rangolist.utils.UploadUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RestaurantService {
  @Autowired
  RestaurantRepository restaurantRepository;

  @Autowired
  RestaurantMapper restaurantMapper;

  @Autowired
  Validations validations;


  public Restaurant validateAndMapRestaurant(Restaurant restaurant) {
    Restaurant newRestaurant = new Restaurant();

    if (validations.isNameValid(restaurant.getName()) &&
        validations.isNameCount(restaurant.getName())) {
      newRestaurant.setName(restaurant.getName());
    }

    if (validations.isOpeningHoursValid(restaurant.getOpeningHours())) {
      newRestaurant.setOpeningHours(restaurant.getOpeningHours());
    }

    if (validations.isAddressValid(restaurant.getAddress())) {
      newRestaurant.setAddress(restaurant.getAddress());
    }
    return restaurantRepository.save(restaurant);
  }

  @Transactional
  public Restaurant createRestaurantWithImage(Restaurant restaurant, MultipartFile image) {
    Restaurant newRestaurant = validateAndMapRestaurant(restaurant);

    if (image == null || image.isEmpty()) {
      throw new HandleNoHasFile("The file is required and was not provided.");
    }

    if (!image.isEmpty()) {
      try {
        String imagePath = UploadUtil.saveFile(image);
        newRestaurant.setImage(imagePath);
      } catch (IOException e) {
        throw new HandleNoHasFile("Failed to upload image");
      }
    }
    return restaurantRepository.save(newRestaurant);
  }

  public List<RestaurantDTO> listAllRestaurant() {
    List<Restaurant> restaurants = restaurantRepository.findAllActiveRestaurants();

    if (restaurants.isEmpty()) {
      throw new HandleNoHasRestaurants("No restaurants found!");
    }

    List<RestaurantDTO> restaurantDTOS = new ArrayList<>();

    for (Restaurant restaurant : restaurants) {
      restaurantDTOS.add(restaurantMapper.toRestaurantDto(restaurant));
    }
    return restaurantDTOS;
  }

  public Optional<Restaurant> findRestaurantById(Long id) {
    Optional<Restaurant> restaurantOptional = restaurantRepository.findActiveRestaurantById(id);

    if (restaurantOptional.isEmpty()) {
      throw new HandleIDNotFound("Restaurant not found!");
    }
    return restaurantOptional;
  }

  public List<RestaurantDTO> findRestaurantByName(String name) {
    List<Restaurant> restaurantList = restaurantRepository.findActiveRestaurantByName(name);

    if (restaurantList.isEmpty()) {
      throw new HandleNameNotFound("Restaurant not found!");
    }

    return restaurantList.stream()
        .map(restaurantMapper::toRestaurantDto)
        .collect(Collectors.toList());
  }

  public Restaurant updateRestaurantById(Long id, Restaurant restaurant) {
    Optional<Restaurant> restaurantOptional = restaurantRepository.findActiveRestaurantById(id);

    if (restaurantOptional.isEmpty()) {
      throw new HandleIDNotFound("Restaurant not found!");
    }

    Restaurant restaurantUpdate = restaurantOptional.get();

    if (restaurant.getName() != null) {
      restaurantUpdate.setName(restaurant.getName());
    }

    if (restaurant.getImage() != null) {
      restaurantUpdate.setImage(restaurant.getImage());
    }

    if (restaurant.getAddress() != null) {
      restaurantUpdate.setAddress(restaurant.getAddress());
    }

    if (restaurant.getOpeningHours() != null) {
      restaurantUpdate.setOpeningHours(restaurant.getOpeningHours());
    }
    return restaurantRepository.save(restaurantUpdate);
  }

  @Transactional
  public void deleteRestaurantById(Long id) {
    Optional<Restaurant> restaurantOptional = restaurantRepository.findActiveRestaurantById(id);

    if (restaurantOptional.isEmpty()) {
      throw new HandleIDNotFound("Restaurant not found!");
    }
    restaurantRepository.deactivateRestaurantById(id);
  }
}
