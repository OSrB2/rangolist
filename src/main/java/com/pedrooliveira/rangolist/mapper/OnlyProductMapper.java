package com.pedrooliveira.rangolist.mapper;

import com.pedrooliveira.rangolist.dto.OnlyProductDTO;
import com.pedrooliveira.rangolist.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OnlyProductMapper {
  OnlyProductMapper INSTANCE = Mappers.getMapper(OnlyProductMapper.class);

  @Mapping(source = "image", target = "imagem")
  @Mapping(source = "name", target = "nome")
  @Mapping(source = "category", target = "categoria")

  OnlyProductDTO toOnlyProductDTO(Product product);
}
