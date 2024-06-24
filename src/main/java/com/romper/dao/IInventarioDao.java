package com.romper.dao;

import com.romper.model.Inventario;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IInventarioDao extends CrudRepository<Inventario, Long> {
    List<Inventario> findByTipoContainingIgnoreCase(String tipo);
    List<Inventario> findInventarioByIngredienteId(Long ingredienteId);
}
