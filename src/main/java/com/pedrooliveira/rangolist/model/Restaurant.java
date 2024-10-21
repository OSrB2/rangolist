package com.pedrooliveira.rangolist.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tb_restaurants")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Restaurant {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String image;
  private String name;

  @Embedded
  private Address address;

  private String openingHours;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Product> products;

  private Boolean status = true;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }
}
