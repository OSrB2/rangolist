package com.pedrooliveira.rangolist.service;

import com.pedrooliveira.rangolist.dto.ProductDTO;
import com.pedrooliveira.rangolist.mapper.ProductMapper;
import com.pedrooliveira.rangolist.model.Product;
import com.pedrooliveira.rangolist.model.Restaurant;
import com.pedrooliveira.rangolist.repository.RestaurantRepository;
import com.pedrooliveira.rangolist.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
  @Autowired
  ProductRepository productRepository;

  @Autowired
  RestaurantRepository restaurantRepository;

  @Autowired
  ProductMapper productMapper;

  @Transactional
  public Product createProduct(Product product) {
    if (product.getRestaurant() != null) {
      Restaurant restaurant = restaurantRepository.findById(product.getRestaurant().getId())
          .orElseThrow(() -> new RuntimeException("Restaurant not found"));
      product.setRestaurant(restaurant);
    }
    return productRepository.save(product);
  }

  public List<ProductDTO> listAllProducts() {
    List<Product> products = productRepository.findAllActiveProducts();
    List<ProductDTO> productDTOS = new ArrayList<>();

    for (Product product : products) {
      productDTOS.add(productMapper.toProductDTO(product));
    }

    return productDTOS;
  }

  public Optional<Product> findProductById(Long id) {
    Optional<Product> productOptional = productRepository.findActiveProductById(id);

    return productOptional;
  }

  public List<ProductDTO> findProductByName(String name) {
    List<Product> productList = productRepository.findActiveProductByName(name);

    return productList.stream()
        .map(productMapper::toProductDTO)
        .collect(Collectors.toList());
  }

  public Product updateProductById(Long id, Product product) {
    Optional<Product> productOptional = productRepository.findActiveProductById(id);
    Product productUpdate = productOptional.get();

    if (productUpdate.getImage() != null) {
      productUpdate.setImage(product.getImage());
    }

    if (productUpdate.getName() != null) {
      productUpdate.setName(product.getName());
    }

    if (productUpdate.getPrice() != null) {
      productUpdate.setPrice(product.getPrice());
    }

    if (productUpdate.getCategory() != null) {
      productUpdate.setCategory(product.getCategory());
    }

    return productRepository.save(productUpdate);
  }

  @Transactional
  public void deleteProductById(Long id) {
    Optional<Product> productOptional = productRepository.findActiveProductById(id);

    if (productOptional.isEmpty()) {
      throw new RuntimeException("Id not found");
    }

    productRepository.deactivateProductById(id);
  }
}
