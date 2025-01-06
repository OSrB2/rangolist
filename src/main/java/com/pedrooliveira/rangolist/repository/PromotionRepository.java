package com.pedrooliveira.rangolist.repository;

import com.pedrooliveira.rangolist.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

  @Query("Select u FROM Promotion u WHERE u.status = true")
  List<Promotion> findAllActivePromotions();

  @Query("Select u FROM Promotion u WHERE u.status = true AND u.id = :id")
  Optional<Promotion> findActivePromotionById(@Param("id") Long id);

  @Modifying
  @Query("UPDATE Promotion u SET u.status = false WHERE u.id = :id")
  void deactivatePromotionById(@Param("id") Long id);
}
