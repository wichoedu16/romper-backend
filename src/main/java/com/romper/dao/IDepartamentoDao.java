package com.romper.dao;

import com.romper.model.Departamento;
import org.springframework.data.repository.CrudRepository;

public interface IDepartamentoDao extends CrudRepository<Departamento, Long> {
}
