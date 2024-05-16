package com.romper.controller;

import com.romper.model.Employee;
import com.romper.response.EmployeeResponseRest;
import com.romper.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class EmployeeRestController {
    @Autowired
    private IEmployeeService employeeService;

    /**
     * Get all employees
     * @return List<Employees>
     */
    @GetMapping("/employees")
    public ResponseEntity<EmployeeResponseRest> searchEmployees(){
        ResponseEntity<EmployeeResponseRest> response = employeeService.search();
        return response;
    }

    /**
     * Get an employee by id
     * @param id
     * @return Employee
     */
    @GetMapping("/employees/{id}")
    public ResponseEntity<EmployeeResponseRest> searchEmployeesById(@PathVariable Long id){
        ResponseEntity<EmployeeResponseRest> response = employeeService.searchById(id);
        return response;
    }

    /**
     * Save employee
     * @param employee
     */
    @PostMapping("/employees")
    public ResponseEntity<EmployeeResponseRest> save(@RequestBody Employee employee){
        ResponseEntity<EmployeeResponseRest> response = employeeService.save(employee);
        return response;
    }

    /**
     * Save employee
     * @param employee
     */
    @PutMapping("/employees/{id}")
    public ResponseEntity<EmployeeResponseRest> update(@RequestBody Employee employee, @PathVariable Long id){
        ResponseEntity<EmployeeResponseRest> response = employeeService.update(employee, id);
        return response;
    }
}
