package com.romper.dao;

import com.romper.model.Cargo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ICargoDao extends CrudRepository<Cargo, Long> {
    List<Cargo> findByDescripcionContainingIgnoreCase(String nombre);

    Optional<Cargo> findByCodigo(String codigo);
}
