package com.pedrooliveira.rangolist.service;

import com.pedrooliveira.rangolist.dto.RestaurantDTO;
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

  public RestaurantDTO createRestaurant (Restaurant restaurant) {
     restaurantRepository.save(restaurant);
     return restaurantMapper.toRestaurantDto(restaurant);
  }

  public List<RestaurantDTO> listAllRestaurant() {
    List<Restaurant> restaurants = restaurantRepository.findAllActiveRestaurants();
    List<RestaurantDTO> restaurantDTOS = new ArrayList<>();

    for (Restaurant restaurant : restaurants) {
      restaurantDTOS.add(restaurantMapper.toRestaurantDto(restaurant));
    }
    return restaurantDTOS;
  }

  public Optional<Restaurant> findRestaurantById(Long id) {
    Optional<Restaurant> restaurantOptional = restaurantRepository.findActiveRestaurantById(id);

    return restaurantOptional;
  }

  public List<RestaurantDTO> findRestaurantByName(String name) {
    List<Restaurant> restaurantList = restaurantRepository.findActiveRestaurantByName(name);

   return restaurantList.stream()
       .map(restaurantMapper::toRestaurantDto)
       .collect(Collectors.toList());
  }

  public Restaurant updateRestaurantById(Long id, Restaurant restaurant) {
    Optional<Restaurant> restaurantOptional = restaurantRepository.findActiveRestaurantById(id);
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

    //TODO -> Modificar a lógica ou a forma como as tabelas são criadas ou relacionadas, para quando um restaurante
    // for editado os produtos continuem relacionados a ele, no momento quando um restaurante é modificado,
    // os produtos relacionados a ele desaparecem.

//    if (restaurantUpdate.getProducts() != null) {
//      restaurantUpdate.setProducts(restaurant.getProducts());
//    }

    return restaurantRepository.save(restaurantUpdate);
  }

  @Transactional
  public void deleteRestaurantById(Long id) {
    Optional<Restaurant> restaurantOptional = restaurantRepository.findActiveRestaurantById(id);

    if (restaurantOptional.isEmpty()) {
      throw new RuntimeException("Id not found");
    }

    restaurantRepository.deactivateRestaurantById(id);
  }
}
