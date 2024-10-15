package com.pedrooliveira.rangolist.service;

import com.pedrooliveira.rangolist.dto.RestaurantDTO;
import com.pedrooliveira.rangolist.mapper.RestaurantMapper;
import com.pedrooliveira.rangolist.model.Restaurant;
import com.pedrooliveira.rangolist.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {
  @Autowired
  private RestaurantRepository restaurantRepository;

  @Autowired
  private RestaurantMapper restaurantMapper;

  public Restaurant createRestaurant (Restaurant restaurant) {
    return restaurantRepository.save(restaurant);
  }

  public List<RestaurantDTO> listAllRestaurant() {
    List<Restaurant> restaurants = restaurantRepository.findAll();

    List<RestaurantDTO> restaurantDTOS = new ArrayList<>();

    for (Restaurant restaurant : restaurants) {
      restaurantDTOS.add(restaurantMapper.toRestaurantDto(restaurant));
    }
    return restaurantDTOS;
  }

  public Optional<Restaurant> findRestaurantById(Long id) {
    Optional<Restaurant> restaurantOptional = restaurantRepository.findById(id);

    return restaurantOptional;
  }

  public List<RestaurantDTO> findRestaurantByName(String name) {
    List<Restaurant> restaurantList = restaurantRepository.findByName(name);
    List<RestaurantDTO> restaurantDto = new ArrayList<>();

    for (Restaurant restaurant : restaurantList) {
      restaurantDto.add(restaurantMapper.toRestaurantDto(restaurant));
    }
    return restaurantDto;
  }

  public Restaurant updateRestaurantById(Long id, Restaurant restaurant) {
    Optional<Restaurant> restaurantOptional = restaurantRepository.findById(id);

    Restaurant restaurantUpdate = restaurantOptional.get();
    if (restaurantUpdate.getName() != null) {
      restaurantUpdate.setName(restaurant.getName());
    }

    if (restaurantUpdate.getImage() != null) {
      restaurantUpdate.setImage(restaurant.getImage());
    }

    if (restaurantUpdate.getAddress() != null) {
      restaurantUpdate.setAddress(restaurant.getAddress());
    }

    if (restaurantUpdate.getOpeningHours() != null) {
      restaurantUpdate.setOpeningHours(restaurant.getOpeningHours());
    }

    if (restaurantUpdate.getProducts() != null) {
      restaurantUpdate.setProducts(restaurant.getProducts());
    }

    return restaurantRepository.save(restaurantUpdate);
  }

  public void deleteRestaurantById(Long id) {
    Optional<Restaurant> restaurantOptional = restaurantRepository.findById(id);

    restaurantRepository.deleteById(id);
  }
}
