package com.romper.service;

import com.romper.dao.*;
import com.romper.dto.PreparacionDTO;
import com.romper.model.*;
import com.romper.response.PreparacionResponseRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PreparacionServiceImpl implements IPreparacionService {
    private final IPreparacionDao preparacionDao;
    private final IIngredienteDao ingredienteDao;
    private final IPlatoDao platoDao;
    private static final Logger logger = LoggerFactory.getLogger(PreparacionServiceImpl.class);

    @Autowired
    public PreparacionServiceImpl(IPreparacionDao preparacionDao, IIngredienteDao ingredienteDao, IPlatoDao platoDao) {
        this.preparacionDao = preparacionDao;
        this.ingredienteDao = ingredienteDao;
        this.platoDao = platoDao;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<PreparacionResponseRest> buscarTodos() {
        PreparacionResponseRest response = new PreparacionResponseRest();
        try {
            List<Preparacion> preparaciones = (List<Preparacion>) preparacionDao.findAll();
            List<PreparacionDTO> dtoPreparacion = pasarEntidadADto(preparaciones);
            response.getPreparacionResponse().setPreparaciones(dtoPreparacion);
            response.setMetadata("Respuesta ok", "00", "Respuesta exitosa");
        } catch (Exception e) {
            logger.error("Error al consultar todos las preparacion", e);
            response.setMetadata("Respuesta nok", "-1", "Error al consultar");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<PreparacionResponseRest> buscarPorPlatoId(Long id) {
        PreparacionResponseRest response = new PreparacionResponseRest();
        try {
            List<Preparacion> preparaciones = preparacionDao.findByPlatoId(id);
            if (!preparaciones.isEmpty()) {
                List<PreparacionDTO> dtoPreparacion = pasarEntidadADto(preparaciones);
                response.getPreparacionResponse().setPreparaciones(dtoPreparacion);
                response.setMetadata("Respuesta ok", "00", "Preparaciones encontradas");
            } else {
                response.setMetadata("Respuesta nok", "-1", "No existen preparaciones para este plato");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error al consultar la preparacion para el plato ", e);
            response.setMetadata("Respuesta nok", "-1", "Error al consultar la preparacion");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private static List<PreparacionDTO> pasarEntidadADto(List<Preparacion> preparaciones) {
        List<PreparacionDTO> dtoPreparacion = new ArrayList<>();
        for (Preparacion receta : preparaciones) {
            PreparacionDTO dto = new PreparacionDTO();
            dto.setPlato(receta.getPlato().getNombre());
            dto.setIngrediente(receta.getIngrediente().getNombre());
            dto.setCantidad(receta.getCantidad());
            dto.setIngredienteId(receta.getIngredienteId());
            dtoPreparacion.add(dto);
        }
        return dtoPreparacion;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<PreparacionResponseRest> buscarPorNombre(String nombre) {
        PreparacionResponseRest response = new PreparacionResponseRest();
        try {
            List<Preparacion> preparaciones = preparacionDao.findByNombreProductoContainingIgnoreCase(nombre);
            if (!preparaciones.isEmpty()) {
                List<PreparacionDTO> dtoPreparacion = pasarEntidadADto(preparaciones);
                response.getPreparacionResponse().setPreparaciones(dtoPreparacion);
                response.setMetadata("Respuesta ok", "00", "Preparacion encontrado");
            } else {
                response.setMetadata("Respuesta nok", "-1", "Preparacion no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error al consultar el Preparacion por nombre", e);
            response.setMetadata("Respuesta nok", "-1", "Error al consultar por nombre");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<PreparacionResponseRest> crear(Preparacion preparacion) {
        PreparacionResponseRest response = new PreparacionResponseRest();
        try {
            if (!existeIdIngrediente(preparacion.getIngredienteId()) &&
                    !existeIdPlato(preparacion.getPlatoId())) {
                response.setMetadata("Respuesta nok", "-1", "No se encuentran ingredientes/platos");
                return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
            }

            Ingrediente ingrediente = ingredienteDao.findById(preparacion.getIngredienteId())
                    .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado"));
            preparacion.setIngrediente(ingrediente);
            Plato plato = platoDao.findById(preparacion.getPlatoId())
                    .orElseThrow(() -> new RuntimeException("Plato no encontrado"));
            preparacion.setPlato(plato);

            if (validarIngredientesRepetidos(ingrediente.getId(), plato)){
                response.setMetadata("Respuesta nok", "-1", "Se encontraron ingredientes repetidos");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }else {
                Preparacion preparacionGuardada = preparacionDao.save(preparacion);
                response.getPreparacionResponse().setPreparaciones(pasarEntidadADto(List.of(preparacionGuardada)));
                response.setMetadata("Respuesta ok", "00", "Preparacion guardada");
            }
        } catch (RuntimeException e) {
            logger.error("Error al crear la preparacion", e);
            response.setMetadata("Respuesta nok", "-1", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error al crear la Preparacion", e);
            response.setMetadata("Respuesta nok", "-1", "Error al crear Preparacion");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private boolean validarIngredientesRepetidos(Long ingredienteId, Plato plato) {
        boolean existe = false;
        List<Preparacion>  preparaciones = preparacionDao.findByPlatoId(plato.getId());
        for (Preparacion receta: preparaciones) {
            if (receta.getIngredienteId().equals(ingredienteId)) {
                existe = true;
            }
        }
        return existe;
    }

    @Override
    @Transactional
    public ResponseEntity<PreparacionResponseRest> eliminar(Long platoId, Long ingredienteId) {
        PreparacionResponseRest response = new PreparacionResponseRest();
        try {
            Preparacion preparacion = preparacionDao.findByPlatoIdAndIngredienteId(platoId, ingredienteId);
            if (null != preparacion){
                preparacionDao.delete(preparacion);
                response.setMetadata("Respuesta ok", "00", "ELiminado correctamente");
            }
        } catch (Exception e) {
            logger.error("Error al eliminar la receta", e);
            response.setMetadata("Respuesta nok", "-1", "Error al eliminar");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    private boolean existeIdIngrediente(Long ingredienteId) {
        return ingredienteDao.findById(ingredienteId).isPresent();
    }

    private boolean existeIdPlato(Long platoId) {
        return platoDao.findById(platoId).isPresent();
    }

}