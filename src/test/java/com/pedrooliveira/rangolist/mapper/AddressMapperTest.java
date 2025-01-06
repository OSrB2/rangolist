package com.pedrooliveira.rangolist.mapper;

import com.pedrooliveira.rangolist.dto.AddressDTO;
import com.pedrooliveira.rangolist.model.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class AddressMapperTest {
  @Autowired
  private AddressMapper addressMapper;

  private Address address;

  @BeforeEach
  void config() {
    address = new Address();
    address.setStreet("Stree test");
    address.setCity("City test");
    address.setState("State test");
    address.setZipcode("00000-000");
  }

  @DisplayName("Should map Address to AddressDTO")
  @Test
  public void testAddressToAddressDTO() {
    AddressDTO addressDTO = addressMapper.toAddressDto(address);

    assertNotNull(addressDTO, "AddressDTO should be not null");

    assertEquals(address.getStreet(), addressDTO.getRua(), "Street should be mapped correctly");
    assertEquals(address.getCity(), addressDTO.getCidade(), "City should be mapped correctly");
    assertEquals(address.getState(), addressDTO.getEstado(), "State should be mapped correctly");
    assertEquals(address.getZipcode(), addressDTO.getCep(), "ZipCode should be mapped correctly");
  }
}
