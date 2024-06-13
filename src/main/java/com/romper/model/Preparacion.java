package com.romper.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "preparacion")
public class Preparacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "preparacion-sequence")
    private Long id;
    @Column(nullable = false)
    private String nombreProducto;
    @Column(nullable = false)
    private String cantidad;

    @ManyToOne
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "ingrediente_id")
    private Ingrediente ingrediente;
    @Column(name = "ingrediente_id", insertable = false, updatable = false)
    private Long ingredienteId;

    @ManyToOne
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "receta_id")
    private Receta receta;
    @Column(name = "receta_id", insertable = false, updatable = false)
    private Long recetaId;
}
