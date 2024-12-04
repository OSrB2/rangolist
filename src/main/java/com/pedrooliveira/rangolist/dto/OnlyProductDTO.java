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
public class OnlyProductDTO {
  private String imagem;
  private String nome;
  private String categoria;
}
