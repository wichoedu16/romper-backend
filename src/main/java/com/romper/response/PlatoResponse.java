package com.romper.response;

import com.romper.model.Plato;
import lombok.Data;

import java.util.List;

@Data
public class PlatoResponse {

    private List<Plato> platos;
}
