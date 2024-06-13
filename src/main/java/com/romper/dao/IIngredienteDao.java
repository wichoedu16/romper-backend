package com.romper.dao;

import com.romper.model.Departamento;
import com.romper.model.Ingrediente;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface IIngredienteDao extends CrudRepository<Ingrediente, Long> {
    List<Ingrediente> findByNombreContainingIgnoreCase(String nombre);

    List<Ingrediente> findIngredienteByProveedorId(Long id);
}
