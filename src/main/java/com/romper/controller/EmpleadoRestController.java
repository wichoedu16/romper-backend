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

    /**
     * Get all employees
     * @return List of Employees
     */
    @GetMapping()
    public ResponseEntity<EmpleadoResponseRest> buscarTodos() {
        return empleadoService.buscarTodos();
    }

    /**
     * Get an employee by id
     * @param id Employee
     * @return Employee
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoResponseRest> buscarPorId(@PathVariable Long id) {
        return empleadoService.buscarPorId(id);
    }

    /**
     * Get an employee by nombre
     * @param nombre
     * @return Employee
     */
    @GetMapping("/filter/{nombre}")
    public ResponseEntity<EmpleadoResponseRest> buscarPorNombre(@PathVariable String nombre) {
        return empleadoService.buscarPorNombre(nombre);
    }

    /**
     * Save employee
     * @param employee Employee object
     * @return Saved Employee
     */
    @PostMapping()
    public ResponseEntity<EmpleadoResponseRest> crear(@RequestBody Empleado employee) {
        return empleadoService.crear(employee);
    }

    /**
     * Update employee
     * @param employee Employee object
     * @param id Employee ID
     * @return Updated Employee
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoResponseRest> actualizar(@RequestBody Empleado employee, @PathVariable Long id) {
        return empleadoService.actualizar(employee, id);
    }

    /**
     * Delete employee by Id
     * @param id Employee ID
     * @return Deleted Employee
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<EmpleadoResponseRest> eliminar(@PathVariable Long id) {
        return empleadoService.eliminar(id);
    }
}