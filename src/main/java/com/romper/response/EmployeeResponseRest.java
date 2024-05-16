package com.romper.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeResponseRest extends ResponseRest{
    private EmployeeResponse employeeResponse = new EmployeeResponse();
}
