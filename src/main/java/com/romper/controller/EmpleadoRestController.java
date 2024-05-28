package com.romper.controller;

import com.romper.model.Empleado;
import com.romper.response.EmpleadoResponseRest;
import com.romper.service.IEmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1")
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
    @GetMapping("/employees")
    public ResponseEntity<EmpleadoResponseRest> searchAll() {
        return empleadoService.search();
    }

    /**
     * Get an employee by id
     * @param id Employee ID
     * @return Employee
     */
    @GetMapping("/employees/{id}")
    public ResponseEntity<EmpleadoResponseRest> searchEmployeesById(@PathVariable Long id) {
        return empleadoService.searchById(id);
    }

    /**
     * Save employee
     * @param employee Employee object
     * @return Saved Employee
     */
    @PostMapping("/employees")
    public ResponseEntity<EmpleadoResponseRest> save(@RequestBody Empleado employee) {
        return empleadoService.save(employee);
    }

    /**
     * Update employee
     * @param employee Employee object
     * @param id Employee ID
     * @return Updated Employee
     */
    @PutMapping("/employees/{id}")
    public ResponseEntity<EmpleadoResponseRest> update(@RequestBody Empleado employee, @PathVariable Long id) {
        return empleadoService.update(employee, id);
    }

    /**
     * Delete employee by Id
     * @param id Employee ID
     * @return Deleted Employee
     */
    @DeleteMapping("/employees/{id}")
    public ResponseEntity<EmpleadoResponseRest> delete(@PathVariable Long id) {
        return empleadoService.deleteById(id);
    }
}