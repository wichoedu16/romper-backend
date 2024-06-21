package com.romper.controller;

import com.romper.model.Proveedor;
import com.romper.response.IngredienteResponseRest;
import com.romper.response.ProveedorResponseRest;
import com.romper.service.IProveedorService;
import com.romper.util.ExportadorExcelProveedores;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1/proveedores")
public class ProveedorRestController {
    private final IProveedorService proveedorService;

    public ProveedorRestController(IProveedorService proveedorService){
        this.proveedorService = proveedorService;
    }

    @GetMapping()
    public ResponseEntity<ProveedorResponseRest> buscarTodos() {
        return proveedorService.buscarTodos();
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProveedorResponseRest> buscarPorId(@PathVariable Long id) {
        return proveedorService.buscarPorId(id);
    }

    @GetMapping("/{id}/ingredientes")
    public ResponseEntity<IngredienteResponseRest> buscarIngredientesPorId(@PathVariable Long id) {
        return proveedorService.buscarIngredientesPorId(id);
    }

    @PostMapping()
    public ResponseEntity<ProveedorResponseRest> crear(@RequestBody Proveedor proveedor){
        return proveedorService.crear(proveedor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProveedorResponseRest> actualizar(@RequestBody Proveedor proveedor, @PathVariable Long id) {
        return proveedorService.actualizar(proveedor, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProveedorResponseRest> eliminar(@PathVariable Long id) {
        return proveedorService.eliminar(id);
    }

    @GetMapping("/exportar/excel")
    public void exportarAExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String nombreCabecera = "Content-Disposition";
        String valorCabecera = "attachment; filename=proveedores.xlsx";
        response.setHeader(nombreCabecera, valorCabecera);
        ResponseEntity<ProveedorResponseRest> proveedorResponse =  proveedorService.buscarTodos();
        ExportadorExcelProveedores exportadorExcel = new ExportadorExcelProveedores(proveedorResponse.getBody().getProveedorResponse().getProveedores());
        exportadorExcel.exportar(response);
    }
}
