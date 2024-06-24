package com.romper.response;

import com.romper.model.Ingrediente;
import lombok.Data;

import java.util.List;

@Data
public class IngredienteResponse {

    private List<Ingrediente> ingredientes;
}
