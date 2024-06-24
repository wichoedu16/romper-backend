package com.romper.controller;

import com.romper.model.Empleado;
import com.romper.response.EmpleadoResponseRest;
import com.romper.service.IEmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1/empleados")
public class EmpleadoRestController {
    private final IEmpleadoService empleadoService;
    @Autowired
    public EmpleadoRestController(IEmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping()
    public ResponseEntity<EmpleadoResponseRest> buscarTodos() {
        return empleadoService.buscarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoResponseRest> buscarPorId(@PathVariable Long id) {
        return empleadoService.buscarPorId(id);
    }

    @GetMapping("/filter/{nombre}")
    public ResponseEntity<EmpleadoResponseRest> buscarPorNombre(@PathVariable String nombre) {
        return empleadoService.buscarPorNombre(nombre);
    }

    @PostMapping()
    public ResponseEntity<EmpleadoResponseRest> crear(@RequestBody Empleado employee) {
        return empleadoService.crear(employee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoResponseRest> actualizar(@RequestBody Empleado employee, @PathVariable Long id) {
        return empleadoService.actualizar(employee, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EmpleadoResponseRest> eliminar(@PathVariable Long id) {
        return empleadoService.eliminar(id);
    }
}