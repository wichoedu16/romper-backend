package com.romper.service;

import com.romper.model.Empleado;
import com.romper.response.EmpleadoResponseRest;
import org.springframework.http.ResponseEntity;

public interface IEmpleadoService {

    public ResponseEntity<EmpleadoResponseRest> search();
    public ResponseEntity<EmpleadoResponseRest> searchById(Long id);
    public ResponseEntity<EmpleadoResponseRest> save(Empleado employee);
    public ResponseEntity<EmpleadoResponseRest> update(Empleado employee, Long id);
    public ResponseEntity<EmpleadoResponseRest> deleteById(Long id);
}
