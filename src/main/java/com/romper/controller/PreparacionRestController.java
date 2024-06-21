package com.romper.controller;

import com.romper.model.Preparacion;
import com.romper.response.PreparacionResponseRest;
import com.romper.service.IPreparacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1/preparaciones")
public class PreparacionRestController {
    private final IPreparacionService preparacionService;

    public PreparacionRestController(IPreparacionService preparacionService){
        this.preparacionService = preparacionService;
    }

    @GetMapping()
    public ResponseEntity<PreparacionResponseRest> buscarTodos() {
        return preparacionService.buscarTodos();
    }

    @GetMapping("/plato/{id}")
    public ResponseEntity<PreparacionResponseRest> buscarPorPlatoId(@PathVariable Long id) {
        return preparacionService.buscarPorPlatoId(id);
    }

    @GetMapping("/filtro/{nombre}")
    public ResponseEntity<PreparacionResponseRest> buscarPorNombre(@PathVariable String nombre) {
        return preparacionService.buscarPorNombre(nombre);
    }

    @PostMapping()
    public ResponseEntity<PreparacionResponseRest> crear(@RequestBody Preparacion preparacion){
        return preparacionService.crear(preparacion);
    }

    @DeleteMapping("/plato/{platoId}/{ingredienteId}")
    public ResponseEntity<PreparacionResponseRest> eliminar(@PathVariable Long platoId, @PathVariable Long ingredienteId) {
        return preparacionService.eliminar(platoId, ingredienteId);
    }
}
