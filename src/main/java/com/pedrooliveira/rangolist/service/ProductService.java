package com.pedrooliveira.rangolist.service;

import com.pedrooliveira.rangolist.dto.ProductDTO;
import com.pedrooliveira.rangolist.exception.HandleIDNotFound;
import com.pedrooliveira.rangolist.exception.HandleNameNotFound;
import com.pedrooliveira.rangolist.exception.HandleNoHasProducts;
import com.pedrooliveira.rangolist.exception.Validations;
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

  @Autowired
  Validations validations;

  @Transactional
  public Product createProduct(Product product) {
    Product newProduct = new Product();

    if (product.getRestaurant() != null) {
      Restaurant restaurant = restaurantRepository.findById(product.getRestaurant().getId())
          .orElseThrow(() -> new HandleIDNotFound("ID not found!"));
      newProduct.setRestaurant(restaurant);
    }

    if (validations.isNameValid(product.getName()) &&
    validations.isNameCount(product.getName())) {
      newProduct.setName(product.getName());
    }

    if (validations.isPriceValid(product.getPrice())) {
      newProduct.setPrice(product.getPrice());
    }

    if (validations.isCategoryValid(product.getCategory())) {
      newProduct.setCategory(product.getCategory());
    }

    if (newProduct.getRestaurant() == null) {
      throw new HandleIDNotFound("Restaurant ID is mandatory!");
    }

    return productRepository.save(newProduct);
  }

  public List<ProductDTO> listAllProducts() {
    List<Product> products = productRepository.findAllActiveProducts();

    if (products.isEmpty()) {
      throw new HandleNoHasProducts("No prodcuts found!");
    }

    List<ProductDTO> productDTOS = new ArrayList<>();

    for (Product product : products) {
      productDTOS.add(productMapper.toProductDTO(product));
    }

    return productDTOS;
  }

  public Optional<Product> findProductById(Long id) {
    Optional<Product> productOptional = productRepository.findActiveProductById(id);

    if (productOptional.isEmpty()) {
      throw new HandleIDNotFound("ID not found!");
    }

    return productOptional;
  }

  public List<ProductDTO> findProductByName(String name) {
    List<Product> productList = productRepository.findActiveProductByName(name);

    if (productList.isEmpty()) {
      throw new HandleNameNotFound("No products found!");
    }

    return productList.stream()
        .map(productMapper::toProductDTO)
        .collect(Collectors.toList());
  }

  public Product updateProductById(Long id, Product product) {
    Optional<Product> productOptional = productRepository.findActiveProductById(id);

    if (productOptional.isEmpty()) {
      throw new HandleIDNotFound("ID not found!");
    }

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
      throw new HandleIDNotFound("ID not found!");
    }
    productRepository.deactivateProductById(id);
  }
}
