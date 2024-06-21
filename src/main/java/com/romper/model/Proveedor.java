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
    private String nombreProveedor;
    @Column(nullable = false)
    private String telefonoProveedor;

    private String banco;
    @Column(name = "tipo_cuenta")
    private String tipoCuenta;
    @Column(name = "numero_cuenta", nullable = false)
    private String numeroCuenta;
    private String identificacion;
    @Column(name = "nombre_contacto", nullable = false)
    private String nombreContacto;
    @Column(name = "telefono_contacto", nullable = false)
    private String telefonoContacto;
    @Column(name = "correo_contacto", nullable = false)
    private String correoContacto;
}