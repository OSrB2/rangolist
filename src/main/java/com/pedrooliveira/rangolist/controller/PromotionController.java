package com.pedrooliveira.rangolist.controller;

import com.pedrooliveira.rangolist.dto.PromotionDTO;
import com.pedrooliveira.rangolist.model.Promotion;
import com.pedrooliveira.rangolist.service.PromotionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/promotion")
public class PromotionController {
  @Autowired
  PromotionService promotionService;

  @PostMapping
  public PromotionDTO register(@Valid @RequestBody Promotion promotion) {
    return promotionService.createPromotion(promotion);
  }

  @GetMapping
  public ResponseEntity<List<PromotionDTO>> listAllPromotions() {
    return ResponseEntity.ok(promotionService.listAllPromotions());
  }

  @GetMapping(path = "/{id}")
  public Optional<Promotion> findById(@PathVariable Long id) {
    return promotionService.findPromotionById(id);
  }

  @PutMapping(path = "/{id}")
  public ResponseEntity<?> editPromotion(@PathVariable Long id, @RequestBody Promotion promotion) {
    return ResponseEntity.ok(promotionService.updatePromotionById(id, promotion));
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<?> deletePromotionById(@PathVariable Long id) {
    promotionService.deletePromotionById(id);
    return ResponseEntity.noContent().build();
  }
}

