package com.romper.controller;

import com.romper.model.Departamento;
import com.romper.response.DepartamentoResponseRest;
import com.romper.service.IDepartamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1/departamentos")
public class DepartamentoRestController {
    private final IDepartamentoService departamentoService;

    public DepartamentoRestController(IDepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }
    @GetMapping()
    ResponseEntity<DepartamentoResponseRest> buscarTodos() {
        return departamentoService.buscarTodos();
    }
    @GetMapping("/{id}")
    ResponseEntity<DepartamentoResponseRest> buscarPorId(@PathVariable Long id){
        return  departamentoService.buscarPorId(id);
    }
    @GetMapping("/filter/{nombre}")
    ResponseEntity<DepartamentoResponseRest> buscarPorNombre(@PathVariable String nombre){
        return departamentoService.buscarPorNombre(nombre);
    }
    @PostMapping()
    ResponseEntity<DepartamentoResponseRest> crear(@RequestBody Departamento departamento) {
        return departamentoService.crear(departamento);
    }
    @PutMapping("/{id}")
    ResponseEntity<DepartamentoResponseRest> actualizar(@RequestBody Departamento departamento, @PathVariable Long id) {
        return departamentoService.actualizar(departamento, id);
    }
    @DeleteMapping("/{id}")
    ResponseEntity<DepartamentoResponseRest>  eliminar(@PathVariable Long id){
        return departamentoService.eliminar(id);
    }
}