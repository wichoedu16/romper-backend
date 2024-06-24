package com.romper.service;

import com.romper.model.Cargo;
import com.romper.model.Proveedor;
import com.romper.response.CargoResponseRest;
import com.romper.response.IngredienteResponseRest;
import com.romper.response.ProveedorResponse;
import com.romper.response.ProveedorResponseRest;
import org.springframework.http.ResponseEntity;

public interface IProveedorService {
    ResponseEntity<ProveedorResponseRest> buscarTodos();
    ResponseEntity<ProveedorResponseRest> buscarPorNombre(String nombre);
    ResponseEntity<ProveedorResponseRest> buscarPorId(Long id);
    ResponseEntity<ProveedorResponseRest> crear(Proveedor cargo);
    ResponseEntity<ProveedorResponseRest> actualizar(Proveedor cargo, Long id);
    ResponseEntity<ProveedorResponseRest> eliminar(Long id);
    ResponseEntity<IngredienteResponseRest> buscarIngredientesPorId(Long id);
}
