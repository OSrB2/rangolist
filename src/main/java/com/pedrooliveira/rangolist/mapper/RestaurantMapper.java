package com.pedrooliveira.rangolist.mapper;

import com.pedrooliveira.rangolist.dto.RestaurantDTO;
import com.pedrooliveira.rangolist.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = AddressMapper.class)
public interface RestaurantMapper {
  RestaurantMapper INSTANCE = Mappers.getMapper( RestaurantMapper.class );
  @Mapping(source = "image", target = "imagem")
  @Mapping(source = "name", target = "nome")
  @Mapping(source = "address", target = "endereco")
  @Mapping(source = "openingHours", target = "funcionamento")

  RestaurantDTO toRestaurantDto(Restaurant restaurant);
}
