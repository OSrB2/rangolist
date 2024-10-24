package com.pedrooliveira.rangolist.controller;

import com.pedrooliveira.rangolist.dto.RestaurantDTO;
import com.pedrooliveira.rangolist.model.Restaurant;
import com.pedrooliveira.rangolist.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {
  @Autowired
  RestaurantService restaurantService;

  @Autowired
  RestaurantDTO restaurantDTO;

  @PostMapping
  public RestaurantDTO register(@Valid @RequestBody Restaurant restaurant) {
    return restaurantService.createRestaurant(restaurant);
  }

  @GetMapping
  public ResponseEntity<List<RestaurantDTO>> listAllRestaurants() {
    return ResponseEntity.ok(restaurantService.listAllRestaurant());
  }

  @GetMapping(path = "/{id}")
  public Optional<Restaurant> findById(@PathVariable Long id) {
    return restaurantService.findRestaurantById(id);
  }

  @GetMapping(path = "/find")
  public ResponseEntity<List<RestaurantDTO>> searchByName(@RequestParam String name) {
    List<RestaurantDTO> restaurants = restaurantService.findRestaurantByName(name);
    return ResponseEntity.ok(restaurants);
  }

  @PutMapping(path = "/{id}")
  public ResponseEntity<?> editRestaurant(@PathVariable Long id, @RequestBody Restaurant restaurant) {
    return ResponseEntity.ok(restaurantService.updateRestaurantById(id, restaurant));
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<?> deleteRestaurantById(@PathVariable Long id) {
    restaurantService.deleteRestaurantById(id);
    return ResponseEntity.noContent().build();
  }
}