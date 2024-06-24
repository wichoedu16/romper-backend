package com.romper.service;

import com.romper.dao.IIngredienteDao;
import com.romper.dao.IInventarioDao;
import com.romper.model.Ingrediente;
import com.romper.model.Inventario;
import com.romper.response.InventarioResponseRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InventarioServiceImpl implements IInventarioService {
    private final IIngredienteDao ingredienteDao;
    private final IInventarioDao inventarioDao;
    private static final Logger logger = LoggerFactory.getLogger(InventarioServiceImpl.class);

    @Autowired
    public InventarioServiceImpl(IIngredienteDao ingredienteDao, IInventarioDao inventarioDao) {
        this.ingredienteDao = ingredienteDao;
        this.inventarioDao = inventarioDao;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<InventarioResponseRest> buscarTodos() {
        InventarioResponseRest response = new InventarioResponseRest();
        try {
            List<Inventario> inventarios = (List<Inventario>) inventarioDao.findAll();
            if (inventarios.isEmpty()) {
                response.setMetadata("Respuesta ok", "01", "No existen datos en inventario");
            } else {
                response.getInventarioResponse().setInventarios(inventarios);
                response.setMetadata("Respuesta ok", "00", "Respuesta exitosa");
            }
        } catch (Exception e) {
            response.setMetadata("Respuesta nok", "-1", "Error al consultar");
            logger.error("Error al consultar el inventario: ", e);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<InventarioResponseRest> buscarPorIngredienteId(Long id) {
        InventarioResponseRest response = new InventarioResponseRest();
        try {
            Ingrediente ingrediente = ingredienteDao.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Ingrediente no encontrado"));
            if (ingrediente!= null){
                List<Inventario> inventarios = inventarioDao.findInventarioByIngredienteId(id);
                response.getInventarioResponse().setInventarios(inventarios);
                response.setMetadata("Respuesta ok", "00", "Ingrediente encontrado");
            } else {
                response.setMetadata("Respuesta nok", "00", "Ingrediente no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (ResourceNotFoundException e) {
            logger.error("Error al consultar el ingrediente por id", e);
            response.setMetadata("Respuesta nok", "-1", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error al consultar el ingrediente por id", e);
            response.setMetadata("Respuesta nok", "-1", "Error al consultar por id");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<InventarioResponseRest> buscarPorTipo(String tipo) {
        InventarioResponseRest response = new InventarioResponseRest();
        try {
            List<Inventario> inventarios = inventarioDao.findByTipoContainingIgnoreCase(tipo);
            if (!inventarios.isEmpty()) {
                response.getInventarioResponse().setInventarios(inventarios);
                response.setMetadata("Respuesta ok", "00", "Inventario encontrado");
            } else {
                response.setMetadata("Respuesta nok", "-1", "Inventario no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.setMetadata("Respuesta nok", "-1", "Error al consultar por tipo de movimiento");
            logger.error("Error al consultar ingrediente por nombre: ", e);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<InventarioResponseRest> crear(Inventario inventario) {
        InventarioResponseRest response = new InventarioResponseRest();
        try {
            Ingrediente ingrediente = ingredienteDao.findById(inventario.getIngredienteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Ingrediente no encontrado"));
            inventario.setIngrediente(ingrediente);
            inventario.setFechaMovimiento(LocalDateTime.now());
            Integer total = calcularInventario(ingrediente, inventario);
            inventario.setTotal(total);
            Inventario inventarioNuevo = inventarioDao.save(inventario);
            actualizarIngrediente(inventarioNuevo);
            response.getInventarioResponse().setInventarios(List.of(inventarioNuevo));
            response.setMetadata("Respuesta ok", "00", "Ingrediente guardado");
        } catch (ResourceNotFoundException e) {
            logger.error("Error al crear el ingrediente", e);
            response.setMetadata("Respuesta nok", "-1", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error al crear el ingrediente", e);
            response.setMetadata("Respuesta nok", "-1", "Error al crear ingrediente");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void actualizarIngrediente(Inventario inventario) {
        Optional<Ingrediente> ingrediente = ingredienteDao.findById(inventario.getIngredienteId());
        if(ingrediente.isPresent()){
            ingrediente.get().setCantidad(inventario.getTotal());
            Ingrediente guardado = ingredienteDao.save(ingrediente.get());
        }
    }

    private Integer calcularInventario(Ingrediente ingrediente, Inventario inventario) {
        Integer total = 0;
        if ("I".equalsIgnoreCase(inventario.getTipo())){
            total = ingrediente.getCantidad() + inventario.getCantidad();
        } else if ("E".equalsIgnoreCase(inventario.getTipo())){
            total = ingrediente.getCantidad() - inventario.getCantidad();
        }
        return total;
    }

}
