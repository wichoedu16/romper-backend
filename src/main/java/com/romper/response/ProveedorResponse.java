package com.romper.response;

import com.romper.model.Proveedor;
import lombok.Data;

import java.util.List;

@Data
public class ProveedorResponse {

    private List<Proveedor> proveedores;
}
