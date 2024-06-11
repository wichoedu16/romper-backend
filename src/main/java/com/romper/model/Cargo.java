package com.romper.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor(force = true)
@Table(name = "cargo")
public class Cargo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cargo-sequence")
    private Long id;
    @NonNull
    private String descripcion;
    @Column(unique = true, nullable = false)
    private String codigo;
    @NonNull
    private String estado;
    @NonNull
    private Double salario;


    @ManyToOne
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;
    @Column(name = "departamento_id", insertable = false, updatable = false)
    private Long departamentoId;
}
