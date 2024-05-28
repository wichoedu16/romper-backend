package com.romper.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "cargo")
public class Cargo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "cargo-sequence")
    Long id;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "estado", length = 1, nullable = false)
    private String estado;

    @Column(name = "salario", nullable = false)
    private Double salario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="departamento_id", nullable=false)
    private Departamento departamento;
}