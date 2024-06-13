package com.romper.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "receta")
public class Receta implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "receta-sequence")
    private Long id;
    @Column(unique = true, nullable = false)
    private String nombre;
    @Column(nullable = false)
    private BigDecimal pvp;

}
