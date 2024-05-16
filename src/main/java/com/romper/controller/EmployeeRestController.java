package com.romper.controller;

import com.romper.model.Employee;
import com.romper.response.EmployeeResponseRest;
import com.romper.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1")
public class EmployeeRestController {

    private final IEmployeeService employeeService;

    @Autowired
    public EmployeeRestController(IEmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Get all employees
     * @return List of Employees
     */
    @GetMapping("/employees")
    public ResponseEntity<EmployeeResponseRest> searchEmployees() {
        return employeeService.search();
    }

    /**
     * Get an employee by id
     * @param id Employee ID
     * @return Employee
     */
    @GetMapping("/employees/{id}")
    public ResponseEntity<EmployeeResponseRest> searchEmployeesById(@PathVariable Long id) {
        return employeeService.searchById(id);
    }

    /**
     * Save employee
     * @param employee Employee object
     * @return Saved Employee
     */
    @PostMapping("/employees")
    public ResponseEntity<EmployeeResponseRest> save(@RequestBody Employee employee) {
        return employeeService.save(employee);
    }

    /**
     * Update employee
     * @param employee Employee object
     * @param id Employee ID
     * @return Updated Employee
     */
    @PutMapping("/employees/{id}")
    public ResponseEntity<EmployeeResponseRest> update(@RequestBody Employee employee, @PathVariable Long id) {
        return employeeService.update(employee, id);
    }

    /**
     * Delete employee by Id
     * @param id Employee ID
     * @return Deleted Employee
     */
    @DeleteMapping("/employees/{id}")
    public ResponseEntity<EmployeeResponseRest> delete(@PathVariable Long id) {
        return employeeService.deleteById(id);
    }
}