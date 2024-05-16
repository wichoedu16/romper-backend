package com.romper.service;

import com.romper.model.Employee;
import com.romper.response.EmployeeResponseRest;
import org.springframework.http.ResponseEntity;

public interface IEmployeeService {

    public ResponseEntity<EmployeeResponseRest> search();
    public ResponseEntity<EmployeeResponseRest> searchById(Long id);
    public ResponseEntity<EmployeeResponseRest> save(Employee employee);
    public ResponseEntity<EmployeeResponseRest> update(Employee employee, Long id);
}
