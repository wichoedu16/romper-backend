package com.romper.service;

import com.romper.model.Cargo;
import com.romper.response.CargoResponseRest;
import org.springframework.http.ResponseEntity;

public interface ICargoService {
    public ResponseEntity<CargoResponseRest> buscarTodos();
    public ResponseEntity<CargoResponseRest> buscarPorId(Long id);
    public ResponseEntity<CargoResponseRest> buscarPorNombre(String nombre);
    public ResponseEntity<CargoResponseRest> crear(Cargo cargo);
    public ResponseEntity<CargoResponseRest> actualizar(Cargo cargo, Long id);
    public ResponseEntity<CargoResponseRest> eliminar(Long id);
}
