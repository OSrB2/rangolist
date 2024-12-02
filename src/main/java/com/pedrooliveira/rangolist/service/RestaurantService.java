package com.pedrooliveira.rangolist.service;

import com.pedrooliveira.rangolist.dto.RestaurantDTO;
import com.pedrooliveira.rangolist.exception.HandleIDNotFound;
import com.pedrooliveira.rangolist.exception.HandleNameNotFound;
import com.pedrooliveira.rangolist.exception.HandleNoHasRestaurants;
import com.pedrooliveira.rangolist.exception.Validations;
import com.pedrooliveira.rangolist.mapper.RestaurantMapper;
import com.pedrooliveira.rangolist.model.Restaurant;
import com.pedrooliveira.rangolist.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

  @Transactional
  public Restaurant createRestaurant(Restaurant restaurant) {
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
