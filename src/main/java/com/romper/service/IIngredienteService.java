package com.romper.service;

import com.romper.model.Cargo;
import com.romper.model.Ingrediente;
import com.romper.response.CargoResponseRest;
import com.romper.response.IngredienteResponseRest;
import org.springframework.http.ResponseEntity;

public interface IIngredienteService {
    ResponseEntity<IngredienteResponseRest> buscarTodos();
    ResponseEntity<IngredienteResponseRest> buscarPorId(Long id);
    ResponseEntity<IngredienteResponseRest> buscarPorNombre(String nombre);
    ResponseEntity<IngredienteResponseRest> crear(Ingrediente cargo);
    ResponseEntity<IngredienteResponseRest> actualizar(Ingrediente cargo, Long id);
    ResponseEntity<IngredienteResponseRest> eliminar(Long id);
}
