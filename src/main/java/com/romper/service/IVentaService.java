package com.romper.service;

import com.romper.model.Venta;
import com.romper.response.VentaResponseRest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface IVentaService {
    ResponseEntity<VentaResponseRest> buscarTodos();
    ResponseEntity<VentaResponseRest> buscarPorId(Long id);
    ResponseEntity<VentaResponseRest> buscarPorFechas(LocalDateTime inicio, LocalDateTime fin);
    ResponseEntity<VentaResponseRest> crear(Venta venta);
}
