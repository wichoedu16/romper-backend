package com.romper.response;

import lombok.Data;
import com.romper.model.Empleado;

import java.util.List;
@Data
public class EmpleadoResponse {

    private List<Empleado> empleados;
}
