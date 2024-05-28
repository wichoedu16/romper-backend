package com.romper.service;

import com.romper.model.Cargo;
import com.romper.model.Departamento;
import com.romper.response.CargoResponseRest;
import com.romper.response.DepartamentoResponseRest;
import org.springframework.http.ResponseEntity;

public interface ICargoService {

    public ResponseEntity<CargoResponseRest> search();
    public ResponseEntity<CargoResponseRest> searchById(Long id);
    ResponseEntity<CargoResponseRest> save(Cargo cargo);

    public ResponseEntity<CargoResponseRest> update(Cargo cargo, Long id);
    public ResponseEntity<CargoResponseRest> deleteById(Long id);
}
