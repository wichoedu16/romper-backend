package com.romper.service;

import com.romper.model.Cargo;
import com.romper.response.CargoResponseRest;
import org.springframework.http.ResponseEntity;

public interface ICargoService {
    ResponseEntity<CargoResponseRest> buscarTodos();
    ResponseEntity<CargoResponseRest> buscarPorId(Long id);
    ResponseEntity<CargoResponseRest> buscarPorNombre(String nombre);
    ResponseEntity<CargoResponseRest> crear(Cargo cargo);
    ResponseEntity<CargoResponseRest> actualizar(Cargo cargo, Long id);
    ResponseEntity<CargoResponseRest> eliminar(Long id);
}
