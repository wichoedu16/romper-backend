package com.romper.service;

import com.romper.model.Cargo;
import com.romper.model.Ingrediente;
import com.romper.response.CargoResponseRest;
import com.romper.response.IngredienteResponseRest;
import org.springframework.http.ResponseEntity;

public interface IIngredienteService {
    public ResponseEntity<IngredienteResponseRest> buscarTodos();
    public ResponseEntity<IngredienteResponseRest> buscarPorId(Long id);
    public ResponseEntity<IngredienteResponseRest> buscarPorNombre(String nombre);
    public ResponseEntity<IngredienteResponseRest> crear(Ingrediente cargo);
    public ResponseEntity<IngredienteResponseRest> actualizar(Ingrediente cargo, Long id);
    public ResponseEntity<IngredienteResponseRest> eliminar(Long id);
}
