package com.pedrooliveira.rangolist.controller;

import com.pedrooliveira.rangolist.dto.ProductDTO;
import com.pedrooliveira.rangolist.mapper.ProductMapper;
import com.pedrooliveira.rangolist.model.Product;
import com.pedrooliveira.rangolist.service.ProductService;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/product")
public class ProductController {
  @Autowired
  ProductService productService;

  @Autowired
  ProductMapper productMapper;

  @PostMapping
  @Transactional
  public ResponseEntity<?> register(@ModelAttribute Product product,
                             @RequestParam("file") MultipartFile image) {
      Product savedProduct = productService.createProductWithImage(product, image);
      ProductDTO productDTO = productMapper.toProductDTO(savedProduct);

      return ResponseEntity.status(HttpStatus.CREATED).body(productDTO);
  }

  @GetMapping
  public ResponseEntity<List<ProductDTO>> listAllProducts() {
    return ResponseEntity.ok(productService.listAllProducts());
  }

  @GetMapping(path = "/{id}")
  public Optional<Product> findById(@PathVariable Long id) {
    return productService.findProductById(id);
  }

  @GetMapping(path = "/find")
  public ResponseEntity<List<ProductDTO>> searchByName(@RequestParam String name) {
    List<ProductDTO> products = productService.findProductByName(name);
    return ResponseEntity.ok(products);
  }

  @PutMapping(path = "/{id}")
  public ResponseEntity<?> editProduct(@PathVariable Long id, @RequestBody Product product) {
    return ResponseEntity.ok(productService.updateProductById(id, product));
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<?> deleteProductById(@PathVariable Long id) {
    productService.deleteProductById(id);
    return ResponseEntity.noContent().build();
  }
}
