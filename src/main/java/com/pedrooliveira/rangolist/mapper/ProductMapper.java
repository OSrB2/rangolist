package com.pedrooliveira.rangolist.mapper;

import com.pedrooliveira.rangolist.dto.ProductDTO;
import com.pedrooliveira.rangolist.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = RestaurantMapper.class)
public interface ProductMapper {
  ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

  @Mapping(source = "restaurant", target = "restaurante")
  @Mapping(source = "image", target = "imagem")
  @Mapping(source = "name", target = "nome")
  @Mapping(source = "price", target = "preco")
  @Mapping(source = "category", target = "categoria")

  ProductDTO toProductDTO(Product product);
}
