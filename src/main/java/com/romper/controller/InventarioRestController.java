package com.romper.controller;

import com.romper.model.Inventario;
import com.romper.response.InventarioResponseRest;
import com.romper.service.IInventarioService;

import com.romper.util.ExportadorExcelInventario;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1/inventario")
public class InventarioRestController {
    private final IInventarioService inventarioService;

    public InventarioRestController(IInventarioService inventarioService){
        this.inventarioService = inventarioService;
    }

    @GetMapping()
    public ResponseEntity<InventarioResponseRest> buscarTodos() {
        return inventarioService.buscarTodos();
    }



    @GetMapping("/{id}")
    public ResponseEntity<InventarioResponseRest> buscarPorIngredienteId(@PathVariable Long id) {
        return inventarioService.buscarPorIngredienteId(id);
    }

    @GetMapping("/filtro/{tipo}")
    public ResponseEntity<InventarioResponseRest> buscarPorTipo(@PathVariable String tipo) {
        return inventarioService.buscarPorTipo(tipo);
    }

    @PostMapping()
    public ResponseEntity<InventarioResponseRest> crear(@RequestBody Inventario inventario){
        return inventarioService.crear(inventario);
    }

    @GetMapping("/exportar/excel")
    public void exportarAExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String nombreCabecera = "Content-Disposition";
        String valorCabecera = "attachment; filename=ingredientes.xls";
        response.setHeader(nombreCabecera, valorCabecera);
        ResponseEntity<InventarioResponseRest> inventariosResponse =  inventarioService.buscarTodos();
        ExportadorExcelInventario exportadorExcel = new ExportadorExcelInventario(inventariosResponse.getBody().getInventarioResponse().getInventarios());
        exportadorExcel.exportar(response);
    }
}
