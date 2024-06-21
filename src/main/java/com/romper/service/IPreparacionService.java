package com.romper.service;

import com.romper.model.Preparacion;
import com.romper.response.PreparacionResponseRest;
import org.springframework.http.ResponseEntity;

public interface IPreparacionService {
    ResponseEntity<PreparacionResponseRest> buscarTodos();
    ResponseEntity<PreparacionResponseRest> buscarPorPlatoId(Long id);
    ResponseEntity<PreparacionResponseRest> buscarPorNombre(String nombre);
    ResponseEntity<PreparacionResponseRest> crear(Preparacion preparacion);
    ResponseEntity<PreparacionResponseRest> eliminar(Long platoId, Long ingredienteId);
}
