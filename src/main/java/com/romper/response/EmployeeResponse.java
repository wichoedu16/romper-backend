package com.romper.response;

import lombok.Data;
import com.romper.model.Employee;

import java.util.List;
@Data
public class EmployeeResponse {

    private List<Employee> employee;
}
