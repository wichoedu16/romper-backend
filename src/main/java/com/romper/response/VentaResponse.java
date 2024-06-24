package com.romper.response;

import com.romper.model.Venta;
import lombok.Data;

import java.util.List;

@Data
public class VentaResponse {

    private List<Venta> ventas;
}
