package com.pedrooliveira.rangolist.service;

import com.pedrooliveira.rangolist.dto.PromotionDTO;
import com.pedrooliveira.rangolist.mapper.PromotionMapper;
import com.pedrooliveira.rangolist.model.Product;
import com.pedrooliveira.rangolist.model.Promotion;
import com.pedrooliveira.rangolist.repository.ProductRepository;
import com.pedrooliveira.rangolist.repository.PromotionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PromotionService {
  @Autowired
  PromotionRepository promotionRepository;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  PromotionMapper promotionMapper;

  public PromotionDTO createPromotion(Promotion promotion) {
    if (promotion.getProduct() != null) {
      Product product = productRepository.findById(promotion.getProduct().getId())
          .orElseThrow(() -> new RuntimeException("Product not found"));
      promotion.setProduct(product);
    }
    promotionRepository.save(promotion);
    return promotionMapper.toPromotionDTO(promotion);
  }

  public List<PromotionDTO> listAllPromotions() {
    List<Promotion> promotions = promotionRepository.findAllActivePromotions();
    List<PromotionDTO> productDTOS = new ArrayList<>();

    for (Promotion promotion : promotions) {
      productDTOS.add(promotionMapper.toPromotionDTO(promotion));
    }
    return productDTOS;
  }

  public Optional<Promotion> findPromotionById(Long id) {
    Optional<Promotion> promotionOptional = promotionRepository.findActivePromotionById(id);
    return promotionOptional;
  }

  public Promotion updatePromotionById(Long id, Promotion promotion) {
    Optional<Promotion> promotionOptional = promotionRepository.findActivePromotionById(id);
    Promotion promotionUpdate = promotionOptional.get();

    if (promotionUpdate.getDescription() != null) {
      promotionUpdate.setDescription(promotion.getDescription());
    }

    if (promotionUpdate.getPromoPrice() != null) {
      promotionUpdate.setPromoPrice(promotion.getPromoPrice());
    }

    if (promotionUpdate.getPromoDays() != null) {
      promotionUpdate.setPromoDays(promotion.getPromoDays());
    }

    if (promotionUpdate.getPromoHours() != null) {
      promotionUpdate.setPromoHours(promotion.getPromoHours());
    }

    return promotionRepository.save(promotionUpdate);
  }

  @Transactional
  public void deletePromotionById(Long id) {
    Optional<Promotion> promotionOptional = promotionRepository.findActivePromotionById(id);

    if (promotionOptional.isEmpty()) {
      throw new RuntimeException("Id not found");
    }

    promotionRepository.deactivatePromotionById(id);
  }
}
