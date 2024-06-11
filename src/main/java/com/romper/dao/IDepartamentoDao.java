package com.romper.dao;

import com.romper.model.Departamento;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface IDepartamentoDao extends CrudRepository<Departamento, Long> {
    List<Departamento> findByDescripcionContainingIgnoreCase(String nombre);
    Optional<Departamento> findDepartamentoByCodigo(String codigo);
}
