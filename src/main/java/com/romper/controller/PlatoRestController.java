package com.romper.controller;

import com.romper.model.Plato;
import com.romper.response.PlatoResponseRest;
import com.romper.service.IPlatoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1/platos")
public class PlatoRestController {
    private final IPlatoService platoService;

    public PlatoRestController(IPlatoService platoService){
        this.platoService = platoService;
    }

    @GetMapping()
    public ResponseEntity<PlatoResponseRest> buscarTodos() {
        return platoService.buscarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlatoResponseRest> buscarPorId(@PathVariable Long id) {
        return platoService.buscarPorId(id);
    }

    @GetMapping("/filtro/{nombre}")
    public ResponseEntity<PlatoResponseRest> buscarPorNombre(@PathVariable String nombre) {
        return platoService.buscarPorNombre(nombre);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<PlatoResponseRest> buscarPorEstado(@PathVariable String estado) {
        return platoService.buscarPorEstado(estado);
    }

    @PostMapping()
    public ResponseEntity<PlatoResponseRest> crear(@RequestBody Plato plato){
        return platoService.crear(plato);
    }


    @PutMapping("/{id}")
    public ResponseEntity<PlatoResponseRest> actualizar(@RequestBody Plato plato, @PathVariable Long id) {
        return platoService.actualizar(plato, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PlatoResponseRest> eliminar(@PathVariable Long id) {
        return platoService.eliminar(id);
    }
}
