package com.pedrooliveira.rangolist.repository;

import com.pedrooliveira.rangolist.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

  @Query("SELECT u FROM Restaurant u WHERE u.status = true")
  List<Restaurant> findAllActiveRestaurants();

  @Query("SELECT u FROM Restaurant u WHERE u.status = true AND u.id = :id")
  Optional<Restaurant> findActiveRestaurantById(@Param("id") Long id);

  @Query("SELECT u FROM Restaurant u WHERE u.status = true AND u.name LIKE CONCAT('%', :name, '%')")
  List<Restaurant> findActiveRestaurantByName(@Param("name") String name);

  @Modifying
  @Query("UPDATE Restaurant r SET r.status = false WHERE  r.id = :id")
  void deactivateRestaurantById(@Param("id") Long id);

}
