package com.romper.dao;

import com.romper.model.Cargo;
import org.springframework.data.repository.CrudRepository;

public interface ICargoDao extends CrudRepository<Cargo, Long> {
}
