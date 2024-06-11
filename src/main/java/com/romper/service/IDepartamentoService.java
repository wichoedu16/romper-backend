package com.romper.service;

import com.romper.model.Departamento;
import com.romper.response.DepartamentoResponseRest;
import org.springframework.http.ResponseEntity;

public interface IDepartamentoService {
    public ResponseEntity<DepartamentoResponseRest> buscarTodos();
    public ResponseEntity<DepartamentoResponseRest> buscarPorId(Long id);
    public ResponseEntity<DepartamentoResponseRest> buscarPorNombre(String nombre);
    public ResponseEntity<DepartamentoResponseRest> crear(Departamento departamento);
    public ResponseEntity<DepartamentoResponseRest> actualizar(Departamento departamento, Long id);
    public ResponseEntity<DepartamentoResponseRest> eliminar(Long id);
}
