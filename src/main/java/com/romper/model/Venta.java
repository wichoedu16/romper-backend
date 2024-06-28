package com.romper.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor(force = true)
@Table(name = "venta")
public class Venta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "venta-sequence")
    private Long id;
    @Column(name = "fecha_venta")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime fechaVenta;
    @Column(nullable = false)
    private int cantidad;
    @Column(nullable = false)
    private BigDecimal precio;
    @Column(nullable = false)
    private BigDecimal total;
    private String estado;

    @ManyToOne
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "plato_id")
    private Plato plato;
    @Column(name = "plato_id", insertable = false, updatable = false)
    private Long platoId;
}
