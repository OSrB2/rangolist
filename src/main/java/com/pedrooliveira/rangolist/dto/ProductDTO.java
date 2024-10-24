package com.pedrooliveira.rangolist.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
  private String imagem;
  private String nome;
  private Double preco;
  private String categoria;
  private RestaurantDTO restaurante;
}

