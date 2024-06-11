package com.romper.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "departamento")
public class Departamento implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "departamento-sequence")
    private Long id;
    @Column(unique = true, nullable = false)
    private String descripcion;
    @Column(unique = true, nullable = false)
    private String codigo;
    @Column(nullable = false)
    private String estado;
}
