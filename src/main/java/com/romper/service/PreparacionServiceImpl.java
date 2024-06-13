package com.romper.service;

import com.romper.dao.*;
import com.romper.model.*;
import com.romper.response.PreparacionResponseRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PreparacionServiceImpl implements IPreparacionService {
    private final IPreparacionDao preparacionDao;
    private final IIngredienteDao ingredienteDao;
    private final IRecetaDao recetaDao;
    private static final Logger logger = LoggerFactory.getLogger(PreparacionServiceImpl.class);

    @Autowired
    public PreparacionServiceImpl(IPreparacionDao preparacionDao, IIngredienteDao ingredienteDao, IRecetaDao recetaDao) {
        this.preparacionDao = preparacionDao;
        this.ingredienteDao = ingredienteDao;
        this.recetaDao = recetaDao;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<PreparacionResponseRest> buscarTodos() {
        PreparacionResponseRest response = new PreparacionResponseRest();
        try {
            List<Preparacion> preparaciones = (List<Preparacion>) preparacionDao.findAll();
            response.getPreparacionResponse().setPreparaciones(preparaciones);
            response.setMetadata("Respuesta ok", "00", "Respuesta exitosa");
        } catch (Exception e) {
            logger.error("Error al consultar todos los cargos", e);
            response.setMetadata("Respuesta nok", "-1", "Error al consultar");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<PreparacionResponseRest> buscarPorId(Long id) {
        PreparacionResponseRest response = new PreparacionResponseRest();
        try {
            Preparacion preparacion = preparacionDao.findById(id)
                    .orElseThrow(() -> new RuntimeException("Cargo no encontrado"));
            response.getPreparacionResponse().setPreparaciones(List.of(preparacion));
            response.setMetadata("Respuesta ok", "00", "Cargo encontrado");
        } catch (RuntimeException e) {
            logger.error("Error al consultar el cargo por id", e);
            response.setMetadata("Respuesta nok", "-1", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error al consultar el cargo por id", e);
            response.setMetadata("Respuesta nok", "-1", "Error al consultar por id");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<PreparacionResponseRest> buscarPorNombre(String nombre) {
        PreparacionResponseRest response = new PreparacionResponseRest();
        try {
            List<Preparacion> cargos = preparacionDao.findByNombreProductoContainingIgnoreCase(nombre);
            if (!cargos.isEmpty()) {
                response.getPreparacionResponse().setPreparaciones(cargos);
                response.setMetadata("Respuesta ok", "00", "Cargo encontrado");
            } else {
                response.setMetadata("Respuesta nok", "-1", "Cargo no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error al consultar el cargo por nombre", e);
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
                    !existeIdPlato(preparacion.getRecetaId())) {
                response.setMetadata("Respuesta nok", "-1", "No se encuntras ingredientes/platos");
                return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
            }

            Ingrediente ingrediente = ingredienteDao.findById(preparacion.getIngredienteId())
                    .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado"));
            preparacion.setIngrediente(ingrediente);
            Receta receta = recetaDao.findById(preparacion.getRecetaId())
                    .orElseThrow(() -> new RuntimeException("Receta no encontrado"));
            preparacion.setReceta(receta);

            Preparacion preparacionGuardada = preparacionDao.save(preparacion);
            response.getPreparacionResponse().setPreparaciones(List.of(preparacionGuardada));
            response.setMetadata("Respuesta ok", "00", "Preparacion guardada");
        } catch (RuntimeException e) {
            logger.error("Error al crear la preparacion", e);
            response.setMetadata("Respuesta nok", "-1", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error al crear el cargo", e);
            response.setMetadata("Respuesta nok", "-1", "Error al crear cargo");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private boolean existeIdIngrediente(Long ingredienteId) {
        return ingredienteDao.findById(ingredienteId).isPresent();
    }

    private boolean existeIdPlato(Long platoId) {
        return recetaDao.findById(platoId).isPresent();
    }

    @Override
    @Transactional
    public ResponseEntity<PreparacionResponseRest> eliminar(Long id) {
        PreparacionResponseRest response = new PreparacionResponseRest();
        try {
            preparacionDao.deleteById(id);
            response.setMetadata("Respuesta ok", "00", "Registro eliminado");
        } catch (Exception e) {
            logger.error("Error al eliminar la receta", e);
            response.setMetadata("Respuesta nok", "-1", "Error al eliminar");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}