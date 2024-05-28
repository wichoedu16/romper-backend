package com.romper.dao;

import com.romper.model.Empleado;
import org.springframework.data.repository.CrudRepository;

public interface IEmpleadoDao extends CrudRepository<Empleado, Long> {

}
