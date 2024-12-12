package com.pedrooliveira.rangolist.service;

import com.pedrooliveira.rangolist.dto.ProductDTO;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PromotionServiceTest {
  @Mock
  private PromotionRepository promotionRepository;
  @Mock
  private ProductRepository productRepository;
  @Mock
  private PromotionMapper promotionMapper;
  @Mock
  private Validations validations;
  @InjectMocks
  private PromotionService promotionService;

  private Product mockProduct;
  private Promotion mockPromotion;

  @BeforeEach
  void setUp() {
    mockProduct = new Product();
    mockProduct.setId(1L);

    mockPromotion = new Promotion();
    mockPromotion.setDescription("Test Promotion");
    mockPromotion.setPromoPrice(9.99);
    mockPromotion.setPromoDays("Monday - Friday");
    mockPromotion.setPromoHours("10:00 - 14:00");
    mockPromotion.setProduct(mockProduct);
  }

  @DisplayName("Should register a promotion")
  @Test
  public void testRegisterPromotion() throws Exception {
    when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));
    when(validations.isDescriptionValid("Test Promotion")).thenReturn(true);
    when(validations.isPromoPriceValid(9.99)).thenReturn(true);
    when(validations.isPromoDaysValid("Monday - Friday")).thenReturn(true);
    when(validations.isPromoHoursValid("10:00 - 14:00")).thenReturn(true);
    when(promotionRepository.save(any(Promotion.class))).thenAnswer(invocation ->
        invocation.getArgument(0));
    when(promotionMapper.toPromotionDTO(any(Promotion.class))).thenReturn(
        new PromotionDTO(null, "Test Promotion", 9.99,
            "Monday - Friday", "10:00 - 14:00")
    );

    PromotionDTO result = promotionService.createPromotion(mockPromotion);

    assertNotNull(result);
    assertEquals("Test Promotion", result.getDescricao());
    assertEquals(9.99, result.getPromocao_preco());
    assertEquals("Monday - Friday", result.getPromocao_dias());
    assertEquals("10:00 - 14:00", result.getPromocao_horas());

    verify(productRepository, times(1)).findById(1L);
    verify(validations, times(1)).isDescriptionValid("Test Promotion");
    verify(validations, times(1)).isPromoPriceValid(9.99);
    verify(validations, times(1)).isPromoDaysValid("Monday - Friday");
    verify(validations, times(1)).isPromoHoursValid("10:00 - 14:00");
    verify(promotionRepository, times(1)).save(any(Promotion.class));
    verify(promotionMapper, times(1)).toPromotionDTO(any(Promotion.class));
  }

  @DisplayName("Should throw exception when product is not found")
  @Test
  public void testRegisterPromotionWhenProductNotFound() throws Exception {
    when(productRepository.findById(1L)).thenReturn(Optional.empty());

    HandleNoHasProducts exception = assertThrows(HandleNoHasProducts.class, () ->
        promotionService.createPromotion(mockPromotion));

    assertEquals("Product not found!", exception.getMessage());
    verify(productRepository, times(1)).findById(1L);
    verifyNoInteractions(promotionRepository);
  }

  @DisplayName("Should throw exception when product ID is not provided")
  @Test
  public void testCreatePromotionWhenIDNotFound() {
    mockPromotion.setProduct(null);

    HandleIDNotFound exception = assertThrows(HandleIDNotFound.class,
        () -> promotionService.createPromotion(mockPromotion));

    assertEquals("Product ID is mandatory!", exception.getMessage());
    verifyNoInteractions(productRepository, promotionRepository);
  }

  @DisplayName("Should return a list of promotions")
  @Test
  public void testListAllPromotions() throws Exception {
    List<Promotion> promotionList = Arrays.asList(mockPromotion);

    when(promotionRepository.findAllActivePromotions()).thenReturn(promotionList);

    List<PromotionDTO> promotionDTOList = promotionService.listAllPromotions();

    assertEquals(1, promotionDTOList.size());
  }

  @DisplayName("Should return an exception if there is no promotions")
  @Test
  public void testNoHasPromotions() throws Exception {
    when(promotionRepository.findAllActivePromotions()).thenReturn(new ArrayList<>());

    assertThrows(HandleNoHasPromotions.class, () -> {
      promotionService.listAllPromotions();
    });
  }

  @DisplayName("Should return a promotion by ID")
  @Test
  public void testFindPromotionByID() {
    mockPromotion.setId(1L);

    when(promotionRepository.findActivePromotionById(mockPromotion.getId())).thenReturn(Optional.of(mockPromotion));

    Optional<Promotion> result = promotionService.findPromotionById(1L);

    assertTrue(result.isPresent());
    assertEquals(mockPromotion, result.get());

    verify(promotionRepository, times(1)).findActivePromotionById(1L);
  }

  @DisplayName("Should return exception when ID no found")
  @Test
  public void testIDNotFound() throws Exception {
    mockPromotion.setId(1L);

    when(promotionRepository.findActivePromotionById(mockPromotion.getId())).thenReturn(Optional.empty());

    assertThrows(HandleIDNotFound.class, () -> {
      promotionService.findPromotionById(mockPromotion.getId());
    });
  }

  @DisplayName("Should update a promotions by ID")
  @Test
  public void testUpdatePromotionByID() throws Exception {
    Promotion promotionUpdate = new Promotion();
    promotionUpdate.setDescription("Updated Promotion");
    promotionUpdate.setPromoPrice(5.50);

    when(promotionRepository.findActivePromotionById(mockPromotion.getId())).thenReturn(Optional.of(mockPromotion));

    when(promotionRepository.save(mockPromotion)).thenReturn(mockPromotion);

    Promotion result = promotionService.updatePromotionById(mockPromotion.getId(), promotionUpdate);

    assertEquals(promotionUpdate.getDescription(), result.getDescription());
    assertEquals(promotionUpdate.getPromoPrice(), result.getPromoPrice());
    verify(promotionRepository).save(mockPromotion);
  }

  @DisplayName("Should delete promotion by ID")
  @Test
  public void testDeletePromotionByID() throws Exception {
    Optional<Promotion> promotionOptional = Optional.of(mockPromotion);

    when(promotionRepository.findActivePromotionById(mockPromotion.getId())).thenReturn(promotionOptional);

    promotionService.deletePromotionById(mockPromotion.getId());
  }
}
