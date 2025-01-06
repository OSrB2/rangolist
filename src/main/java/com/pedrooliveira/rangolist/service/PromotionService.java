package com.pedrooliveira.rangolist.service;

import com.pedrooliveira.rangolist.dto.PromotionDTO;
import com.pedrooliveira.rangolist.exception.HandleIDNotFound;
import com.pedrooliveira.rangolist.exception.HandleNoHasProducts;
import com.pedrooliveira.rangolist.exception.HandleNoHasPromotions;
import com.pedrooliveira.rangolist.exception.Validations;
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

  @Autowired
  Validations validations;

  public PromotionDTO createPromotion(Promotion promotion) {
    Promotion newPromotion = new Promotion();

    if (promotion.getProduct() != null) {
      Product product = productRepository.findById(promotion.getProduct().getId())
          .orElseThrow(() -> new HandleNoHasProducts("Product not found!"));
      newPromotion.setProduct(product);
    }

    if (validations.isDescriptionValid(promotion.getDescription())) {
      newPromotion.setDescription(promotion.getDescription());
    }

    if (validations.isPromoPriceValid(promotion.getPromoPrice())) {
      newPromotion.setPromoPrice(promotion.getPromoPrice());
    }

    if (validations.isPromoDaysValid(promotion.getPromoDays())) {
      newPromotion.setPromoDays(promotion.getPromoDays());
    }

    if (validations.isPromoHoursValid(promotion.getPromoHours())) {
      newPromotion.setPromoHours(promotion.getPromoHours());
    }

    if (newPromotion.getProduct() == null) {
      throw new HandleIDNotFound("Product ID is mandatory!");
    }

    promotionRepository.save(newPromotion);
    return promotionMapper.toPromotionDTO(newPromotion);
  }

  public List<PromotionDTO> listAllPromotions() {
    List<Promotion> promotions = promotionRepository.findAllActivePromotions();

    if (promotions.isEmpty()) {
      throw new HandleNoHasPromotions("No promotions found!");
    }

    List<PromotionDTO> productDTOS = new ArrayList<>();

    for (Promotion promotion : promotions) {
      productDTOS.add(promotionMapper.toPromotionDTO(promotion));
    }
    return productDTOS;
  }

  public Optional<Promotion> findPromotionById(Long id) {
    Optional<Promotion> promotionOptional = promotionRepository.findActivePromotionById(id);

    if (promotionOptional.isEmpty()) {
      throw new HandleIDNotFound("ID not found!");
    }

    return promotionOptional;
  }

  public Promotion updatePromotionById(Long id, Promotion promotion) {
    Optional<Promotion> promotionOptional = promotionRepository.findActivePromotionById(id);

    if (promotionOptional.isEmpty()) {
      throw new HandleIDNotFound("ID not found!");
    }

    Promotion promotionUpdate = promotionOptional.get();

    if (promotion.getDescription() != null) {
      promotionUpdate.setDescription(promotion.getDescription());
    }

    if (promotion.getPromoPrice() != null) {
      promotionUpdate.setPromoPrice(promotion.getPromoPrice());
    }

    if (promotion.getPromoDays() != null) {
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
      throw new HandleIDNotFound("ID not found!");
    }

    promotionRepository.deactivatePromotionById(id);
  }
}
