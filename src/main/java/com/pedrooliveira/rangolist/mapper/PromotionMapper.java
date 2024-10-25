package com.pedrooliveira.rangolist.mapper;

import com.pedrooliveira.rangolist.dto.PromotionDTO;
import com.pedrooliveira.rangolist.model.Promotion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface PromotionMapper {
  PromotionMapper INSTANCE = Mappers.getMapper(PromotionMapper.class);

  @Mapping(source = "product", target = "produto")
  @Mapping(source = "description", target = "descricao")
  @Mapping(source = "promoPrice", target = "promocao_preco")
  @Mapping(source = "promoDays", target = "promocao_dias")
  @Mapping(source = "promoHours", target = "promocao_horas")

  PromotionDTO toPromotionDTO(Promotion promotion);
}
