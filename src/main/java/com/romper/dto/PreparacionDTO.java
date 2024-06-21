package com.romper.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.service.annotation.GetExchange;

@Data
@Getter
@Setter
public class PreparacionDTO {
    private String plato;
    private String ingrediente;
    private Long ingredienteId;
    private String cantidad;
}
