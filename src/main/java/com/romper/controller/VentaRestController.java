package com.romper.controller;

import com.romper.model.Venta;
import com.romper.response.VentaResponseRest;
import com.romper.service.IVentaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1/ventas")
public class VentaRestController {
    private final IVentaService ventaService;

    public VentaRestController(IVentaService ventaService){
        this.ventaService = ventaService;
    }

    @GetMapping()
    public ResponseEntity<VentaResponseRest> buscarTodos() {
        return ventaService.buscarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaResponseRest> buscarPorId(@PathVariable Long id) {
        return ventaService.buscarPorId(id);
    }

    @GetMapping("/filtro/{fechaDesde}/{fechaHasta}")
    public ResponseEntity<VentaResponseRest> buscarPorFechas(
            @PathVariable("fechaDesde") @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime fechaDesde,
            @PathVariable("fechaHasta") @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime fechaHasta) {

        return ventaService.buscarPorFechas(fechaDesde, fechaHasta);
    }

    @PostMapping()
    public ResponseEntity<VentaResponseRest> crear(@RequestBody Venta venta){
        return ventaService.crear(venta);
    }

}
