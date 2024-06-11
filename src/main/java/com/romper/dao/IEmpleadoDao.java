package com.romper.dao;

import com.romper.model.Empleado;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IEmpleadoDao extends CrudRepository<Empleado, Long> {

    List<Empleado> findByNombreContainingOrApellidoPaternoContainingOrApellidoMaternoContaining(String nombre, String apellidoPaterno, String apellidoMaterno);
}
