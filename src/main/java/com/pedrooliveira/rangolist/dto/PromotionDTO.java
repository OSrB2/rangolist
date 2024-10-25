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
public class PromotionDTO {
  private ProductDTO produto;
  private String descricao;
  private Double promocao_preco;
  private String promocao_dias;
  private String promocao_horas;
}
