package com.romper.service;

import com.romper.model.Ingrediente;
import com.romper.model.Inventario;
import com.romper.response.IngredienteResponseRest;
import com.romper.response.InventarioResponse;
import com.romper.response.InventarioResponseRest;
import org.springframework.http.ResponseEntity;

public interface IInventarioService {
    ResponseEntity<InventarioResponseRest> buscarTodos();
    ResponseEntity<InventarioResponseRest> buscarPorIngredienteId(Long ingredienteId);
    ResponseEntity<InventarioResponseRest> buscarPorTipo(String tipo);
    ResponseEntity<InventarioResponseRest> crear(Inventario inventario);
}
