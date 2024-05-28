package com.romper.service;

import com.romper.model.Departamento;
import com.romper.response.DepartamentoResponseRest;
import org.springframework.http.ResponseEntity;

public interface IDepartamentoService {

    public ResponseEntity<DepartamentoResponseRest> search();
    public ResponseEntity<DepartamentoResponseRest> searchById(Long id);
    public ResponseEntity<DepartamentoResponseRest> save(Departamento departamento);
    public ResponseEntity<DepartamentoResponseRest> update(Departamento departamento, Long id);
    public ResponseEntity<DepartamentoResponseRest> deleteById(Long id);
}
