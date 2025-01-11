package com.pedrooliveira.rangolist.repository;

import com.pedrooliveira.rangolist.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  @Query("SELECT u FROM Product u WHERE u.status = true")
  List<Product> findAllActiveProducts();

  @Query("SELECT u FROM Product u WHERE u.status = true AND u.id = :id")
  Optional<Product> findActiveProductById(@Param("id") Long id);

  @Query("SELECT u FROM Product u WHERE u.status = true AND LOWER(TRIM(u.name)) LIKE LOWER(CONCAT('%', TRIM(:name), '%'))")
  List<Product> findActiveProductByName(@Param("name") String name);

  @Modifying
  @Query("UPDATE Product r SET r.status = false WHERE  r.id = :id")
  void deactivateProductById(@Param("id") Long id);
}
