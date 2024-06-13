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
@Table(name = "proveedor")
public class Proveedor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "proveedor-sequence")
    private Long id;
    @Column(unique = true, nullable = false)
    private String empresa;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String contacto;

}