package com.romper.service;

import com.romper.model.Receta;
import com.romper.response.RecetaResponseRest;
import org.springframework.http.ResponseEntity;

public interface IRecetaService {
    ResponseEntity<RecetaResponseRest> buscarTodos();
    ResponseEntity<RecetaResponseRest> buscarPorId(Long id);
    ResponseEntity<RecetaResponseRest> crear(Receta receta);
    ResponseEntity<RecetaResponseRest> actualizar(Receta receta, Long id);
    ResponseEntity<RecetaResponseRest> eliminar(Long id);
    ResponseEntity<RecetaResponseRest> buscarPorNombre(String nombre);
}
