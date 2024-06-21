package com.romper.dao;

import com.romper.model.Plato;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IPlatoDao extends CrudRepository<Plato, Long> {

    List<Plato> findByNombreContainingIgnoreCase(String nombre);
}
