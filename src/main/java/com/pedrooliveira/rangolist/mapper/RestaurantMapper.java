package com.pedrooliveira.rangolist.mapper;

import com.pedrooliveira.rangolist.dto.RestaurantDTO;
import com.pedrooliveira.rangolist.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RestaurantMapper {
  RestaurantMapper INSTANCE = Mappers.getMapper( RestaurantMapper.class );
  RestaurantDTO toRestaurantDto(Restaurant restaurant);
}
