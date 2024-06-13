package com.romper.response;

import com.romper.model.Receta;
import lombok.Data;

import java.util.List;

@Data
public class RecetaResponse {

    private List<Receta> recetas;
}
