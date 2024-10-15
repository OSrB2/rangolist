package com.pedrooliveira.rangolist.repository;

import com.pedrooliveira.rangolist.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
