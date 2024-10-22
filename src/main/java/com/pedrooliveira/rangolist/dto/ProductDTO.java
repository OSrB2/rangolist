package com.pedrooliveira.rangolist.dto;

import com.pedrooliveira.rangolist.model.Restaurant;
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
public class ProductDTO {
  private Restaurant restaurante;
  private String imagem;
  private String nome;
  private Double preco;
  private String categoria;
}

