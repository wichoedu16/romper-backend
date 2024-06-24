package com.romper.service;


import com.romper.dao.IPlatoDao;
import com.romper.model.Plato;
import com.romper.response.PlatoResponseRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class PlatoServiceImpl implements IPlatoService {
    private static final Logger logger = LoggerFactory.getLogger(PlatoServiceImpl.class);
    private final IPlatoDao platoDao;

    @Autowired
    public PlatoServiceImpl(IPlatoDao platoDao) {
        this.platoDao = platoDao;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<PlatoResponseRest> buscarTodos() {
        PlatoResponseRest response = new PlatoResponseRest();
        try {
            List<Plato> platos = (List<Plato>) platoDao.findAll();
            if(platos.isEmpty()){
                response.setMetadata("Respuesta ok", "01", "No existen platos");
            }else{
                response.getPlatoResponse().setPlatos(platos);
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
    public ResponseEntity<PlatoResponseRest> buscarPorId(Long id) {
        PlatoResponseRest response = new PlatoResponseRest();
        try {
            Plato plato = platoDao.findById(id)
                    .orElseThrow(() -> new RuntimeException("plato no encontrado"));
            response.getPlatoResponse().setPlatos(List.of(plato));
            response.setMetadata("Respuesta ok", "00", "plato encontrado");
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
    public ResponseEntity<PlatoResponseRest> crear(Plato plato) {
        PlatoResponseRest response = new PlatoResponseRest();
        try {
            Plato platoGuardado = platoDao.save(plato);
            response.getPlatoResponse().setPlatos(List.of(platoGuardado));
            response.setMetadata("Respuesta ok", "00", "Plato guardado");
        } catch (Exception e) {
            logger.error("Error al crear el plato", e);
            response.setMetadata("Respuesta nok", "-1", "Error al crear el plato");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PlatoResponseRest> actualizar(Plato plato, Long id) {
        PlatoResponseRest response = new PlatoResponseRest();
        try {
            Plato platoBuscado = platoDao.findById(id)
                    .orElseThrow(() -> new RuntimeException("Plato no encontrado"));
            actualizarDatosPlato(plato, platoBuscado);
            Plato platoActualizado = platoDao.save(platoBuscado);
            response.getPlatoResponse().setPlatos(List.of(platoActualizado));
            response.setMetadata("Respuesta ok", "00", "Plato actualizado");
        } catch (RuntimeException e) {
            logger.error("Error al actualizar el plato", e);
            response.setMetadata("Respuesta nok", "-1", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error al actualizar el plato", e);
            response.setMetadata("Respuesta nok", "-1", "Error al actualizar plato");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PlatoResponseRest> eliminar(Long id) {
        PlatoResponseRest response = new PlatoResponseRest();
        //buscar si tiene ingredientes asociados y preparacion para que no se cayera
        try {
            platoDao.deleteById(id);
            response.setMetadata("respuesta ok", "00", "Plato eliminado");
        } catch (Exception e) {
            logger.error("Error al eliminar la receta", e);
            response.setMetadata("Respuesta nok", "-1", "Error al eliminar");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PlatoResponseRest> buscarPorNombre(String nombre) {
        PlatoResponseRest response = new PlatoResponseRest();
        try {
            List<Plato> platos = platoDao.findByNombreContainingIgnoreCase(nombre);
            if(platos.isEmpty()){
                response.setMetadata("Respuesta ok", "01", "No existen platos");
            }else{
                response.getPlatoResponse().setPlatos(platos);
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
    public ResponseEntity<PlatoResponseRest> buscarPorEstado(String estado) {
        PlatoResponseRest response = new PlatoResponseRest();
        try {
            List<Plato> platos = platoDao.findByEstadoStartsWith(estado);
            if(platos.isEmpty()){
                response.setMetadata("Respuesta ok", "01", "No existen platos");
            }else{
                response.getPlatoResponse().setPlatos(platos);
                response.setMetadata("Respuesta ok", "00", "Respuesta exitosa");
            }
        } catch (Exception e) {
            logger.error("Error al consultar todos los platos", e);
            response.setMetadata("Respuesta nok", "-1", "Error al consultar");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void actualizarDatosPlato(Plato source, Plato target) {
        target.setNombre(source.getNombre() != null ? source.getNombre() : target.getNombre());
        target.setPvp(source.getPvp() != null ? source.getPvp() : target.getPvp());
        target.setEstado(source.getEstado() != null ? source.getEstado() : target.getEstado());
    }
}