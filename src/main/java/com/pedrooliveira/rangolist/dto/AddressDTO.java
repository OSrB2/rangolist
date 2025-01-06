package com.pedrooliveira.rangolist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddressDTO {
  private String rua;
  private String cidade;
  private String estado;
  private String cep;
}
