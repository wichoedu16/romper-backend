package com.romper.dao;

import com.romper.model.Receta;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IRecetaDao extends CrudRepository<Receta, Long> {

    List<Receta> findByNombreContainingIgnoreCase(String nombre);
}
