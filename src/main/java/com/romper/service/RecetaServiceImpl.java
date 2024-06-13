package com.romper.service;


import com.romper.dao.IRecetaDao;
import com.romper.model.Receta;
import com.romper.response.RecetaResponseRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class RecetaServiceImpl implements IRecetaService {
    private static final Logger logger = LoggerFactory.getLogger(RecetaServiceImpl.class);
    private final IRecetaDao recetaDao;

    @Autowired
    public RecetaServiceImpl(IRecetaDao recetaDao) {
        this.recetaDao = recetaDao;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<RecetaResponseRest> buscarTodos() {
        RecetaResponseRest response = new RecetaResponseRest();
        try {
            List<Receta> recetas = (List<Receta>) recetaDao.findAll();
            if(recetas.isEmpty()){
                response.setMetadata("Respuesta ok", "01", "No existen recetas");
            }else{
                response.getRecetaResponse().setRecetas(recetas);
                response.setMetadata("Respuesta ok", "00", "Respuesta exitosa");
            }

        } catch (Exception e) {
            logger.error("Error al consultar todos los platos", e);
            response.setMetadata("Respuesta nok", "-1", "Error al consultar");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<RecetaResponseRest> buscarPorId(Long id) {
        RecetaResponseRest response = new RecetaResponseRest();
        try {
            Receta receta = recetaDao.findById(id)
                    .orElseThrow(() -> new RuntimeException("receta no encontrado"));
            response.getRecetaResponse().setRecetas(List.of(receta));
            response.setMetadata("Respuesta ok", "00", "receta encontrado");
        } catch (RuntimeException e) {
            logger.error("Error al consultar el plato por id", e);
            response.setMetadata("Respuesta nok", "-1", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error al consultar el plato por id", e);
            response.setMetadata("Respuesta nok", "-1", "Error al consultar por id");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<RecetaResponseRest> crear(Receta receta) {
        RecetaResponseRest response = new RecetaResponseRest();
        try {
            Receta recetaGuardado = recetaDao.save(receta);
            response.getRecetaResponse().setRecetas(List.of(recetaGuardado));
            response.setMetadata("Respuesta ok", "00", "Receta guardado");
        } catch (Exception e) {
            logger.error("Error al crear el receta", e);
            response.setMetadata("Respuesta nok", "-1", "Error al crear el receta");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RecetaResponseRest> actualizar(Receta receta, Long id) {
        RecetaResponseRest response = new RecetaResponseRest();
        try {
            Receta recetaBuscado = recetaDao.findById(id)
                    .orElseThrow(() -> new RuntimeException("Receta no encontrado"));
            actualizarDatosPlato(receta, recetaBuscado);
            Receta recetaActualizado = recetaDao.save(recetaBuscado);
            response.getRecetaResponse().setRecetas(List.of(recetaActualizado));
            response.setMetadata("Respuesta ok", "00", "Receta actualizado");
        } catch (RuntimeException e) {
            logger.error("Error al actualizar el receta", e);
            response.setMetadata("Respuesta nok", "-1", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error al actualizar el receta", e);
            response.setMetadata("Respuesta nok", "-1", "Error al actualizar receta");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RecetaResponseRest> eliminar(Long id) {
        RecetaResponseRest response = new RecetaResponseRest();
        //buscar si tiene ingredientes asociados y preparacion para que no se cayera
        try {
            recetaDao.deleteById(id);
            response.setMetadata("respuesta ok", "00", "Receta eliminado");
        } catch (Exception e) {
            logger.error("Error al eliminar la receta", e);
            response.setMetadata("Respuesta nok", "-1", "Error al eliminar");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RecetaResponseRest> buscarPorNombre(String nombre) {
        RecetaResponseRest response = new RecetaResponseRest();
        try {
            List<Receta> recetas = recetaDao.findByNombreContainingIgnoreCase(nombre);
            if(recetas.isEmpty()){
                response.setMetadata("Respuesta ok", "01", "No existen recetas");
            }else{
                response.getRecetaResponse().setRecetas(recetas);
                response.setMetadata("Respuesta ok", "00", "Respuesta exitosa");
            }

        } catch (Exception e) {
            logger.error("Error al consultar todos los platos", e);
            response.setMetadata("Respuesta nok", "-1", "Error al consultar");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void actualizarDatosPlato(Receta source, Receta target) {
        target.setNombre(source.getNombre() != null ? source.getNombre() : target.getNombre());
        target.setPvp(source.getPvp() != null ? source.getPvp() : target.getPvp());
    }
}