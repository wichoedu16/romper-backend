package com.romper.service;

import com.romper.model.Plato;
import com.romper.response.PlatoResponseRest;
import org.springframework.http.ResponseEntity;

public interface IPlatoService {
    ResponseEntity<PlatoResponseRest> buscarTodos();
    ResponseEntity<PlatoResponseRest> buscarPorId(Long id);
    ResponseEntity<PlatoResponseRest> crear(Plato plato);
    ResponseEntity<PlatoResponseRest> actualizar(Plato plato, Long id);
    ResponseEntity<PlatoResponseRest> eliminar(Long id);
    ResponseEntity<PlatoResponseRest> buscarPorNombre(String nombre);
}
