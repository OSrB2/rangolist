package com.pedrooliveira.rangolist.dto;

import com.pedrooliveira.rangolist.model.Address;
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
public class RestaurantDTO {
  private String imagem;
  private String nome;
  private AddressDTO endereco;
  private String funcionamento;
}
