package com.romper.controller;

import com.romper.model.Receta;
import com.romper.response.RecetaResponseRest;
import com.romper.service.IRecetaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1/recetas")
public class RecetaRestController {
    private final IRecetaService recetaService;

    public RecetaRestController(IRecetaService recetaService){
        this.recetaService = recetaService;
    }

    @GetMapping()
    public ResponseEntity<RecetaResponseRest> buscarTodos() {
        return recetaService.buscarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecetaResponseRest> buscarPorId(@PathVariable Long id) {
        return recetaService.buscarPorId(id);
    }

    @GetMapping("/filtro/{nombre}")
    public ResponseEntity<RecetaResponseRest> buscarPorNombre(@PathVariable String nombre) {
        return recetaService.buscarPorNombre(nombre);
    }

    @PostMapping()
    public ResponseEntity<RecetaResponseRest> crear(@RequestBody Receta receta){
        return recetaService.crear(receta);
    }


    @PutMapping("/{id}")
    public ResponseEntity<RecetaResponseRest> actualizar(@RequestBody Receta receta, @PathVariable Long id) {
        return recetaService.actualizar(receta, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RecetaResponseRest> eliminar(@PathVariable Long id) {
        return recetaService.eliminar(id);
    }
}
