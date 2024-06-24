package com.romper.response;

import com.romper.model.Cargo;
import lombok.Data;

import java.util.List;

@Data
public class CargoResponse {

    private List<Cargo> cargos;
}
