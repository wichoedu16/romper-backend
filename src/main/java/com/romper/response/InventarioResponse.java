package com.romper.response;

import com.romper.model.Inventario;
import lombok.Data;

import java.util.List;

@Data
public class InventarioResponse {

    private List<Inventario> inventarios;
}
