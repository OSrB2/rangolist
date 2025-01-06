package com.pedrooliveira.rangolist.mapper;

import com.pedrooliveira.rangolist.dto.AddressDTO;
import com.pedrooliveira.rangolist.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AddressMapper {
  AddressMapper INSTANCE = Mappers.getMapper( AddressMapper.class );
  @Mapping(source = "street", target = "rua")
  @Mapping(source = "city", target = "cidade")
  @Mapping(source = "state", target = "estado")
  @Mapping(source = "zipcode", target = "cep")

  AddressDTO toAddressDto(Address address);
}
