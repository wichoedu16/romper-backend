package com.romper.response;

import com.romper.dto.PreparacionDTO;
import com.romper.model.Preparacion;
import lombok.Data;

import java.util.List;

@Data
public class PreparacionResponse {

    private List<PreparacionDTO> preparaciones;
}
