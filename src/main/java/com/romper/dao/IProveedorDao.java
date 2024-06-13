package com.romper.dao;

import com.romper.model.Cargo;
import com.romper.model.Proveedor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface IProveedorDao extends CrudRepository<Proveedor, Long> {

}
