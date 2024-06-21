package com.romper.dao;

import com.romper.model.Preparacion;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IPreparacionDao extends CrudRepository<Preparacion, Long> {
    List<Preparacion> findByNombreProductoContainingIgnoreCase(String nombre);
    List<Preparacion> findByPlatoId(Long id);

    Preparacion findByPlatoIdAndIngredienteId(Long platoId, Long ingredienteId);
}
