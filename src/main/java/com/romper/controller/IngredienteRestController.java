package com.romper.controller;

import com.romper.model.Cargo;
import com.romper.model.Ingrediente;
import com.romper.response.CargoResponseRest;
import com.romper.response.IngredienteResponseRest;
import com.romper.service.ICargoService;
import com.romper.service.IIngredienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1/ingredientes")
public class IngredienteRestController {
    private final IIngredienteService ingredienteService;

    public IngredienteRestController(IIngredienteService ingredienteService){
        this.ingredienteService = ingredienteService;
    }

    @GetMapping()
    public ResponseEntity<IngredienteResponseRest> buscarTodos() {
        return ingredienteService.buscarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredienteResponseRest> buscarPorId(@PathVariable Long id) {
        return ingredienteService.buscarPorId(id);
    }

    @GetMapping("/filtro/{nombre}")
    public ResponseEntity<IngredienteResponseRest> buscarPorNombre(@PathVariable String nombre) {
        return ingredienteService.buscarPorNombre(nombre);
    }

    @PostMapping()
    public ResponseEntity<IngredienteResponseRest> crear(@RequestBody Ingrediente ingrediente){
        return ingredienteService.crear(ingrediente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredienteResponseRest> actualizar(@RequestBody Ingrediente ingrediente, @PathVariable Long id) {
        return ingredienteService.actualizar(ingrediente, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<IngredienteResponseRest> eliminar(@PathVariable Long id) {
        return ingredienteService.eliminar(id);
    }
}