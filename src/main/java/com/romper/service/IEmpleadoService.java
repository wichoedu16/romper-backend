package com.romper.service;

import com.romper.model.Empleado;
import com.romper.response.EmpleadoResponseRest;
import org.springframework.http.ResponseEntity;

public interface IEmpleadoService {

    public ResponseEntity<EmpleadoResponseRest> buscarTodos();
    public ResponseEntity<EmpleadoResponseRest> buscarPorId(Long id);
    public ResponseEntity<EmpleadoResponseRest> buscarPorNombre(String nombre);
    public ResponseEntity<EmpleadoResponseRest> crear(Empleado employee);
    public ResponseEntity<EmpleadoResponseRest> actualizar(Empleado employee, Long id);
    public ResponseEntity<EmpleadoResponseRest> eliminar(Long id);
}
