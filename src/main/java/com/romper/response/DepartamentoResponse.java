package com.romper.response;

import com.romper.model.Departamento;
import com.romper.model.Empleado;
import lombok.Data;

import java.util.List;

@Data
public class DepartamentoResponse {

    private List<Departamento> departamento;
}
