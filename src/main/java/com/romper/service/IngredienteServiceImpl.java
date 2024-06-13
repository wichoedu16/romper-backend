package com.romper.service;

import com.romper.dao.IIngredienteDao;
import com.romper.dao.IProveedorDao;
import com.romper.model.Ingrediente;
import com.romper.model.Proveedor;
import com.romper.response.IngredienteResponseRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IngredienteServiceImpl implements IIngredienteService {
    private final IIngredienteDao ingredienteDao;
    private final IProveedorDao proveedorDao;
    private static final Logger logger = LoggerFactory.getLogger(IngredienteServiceImpl.class);

    @Autowired
    public IngredienteServiceImpl(IIngredienteDao ingredienteDao, IProveedorDao proveedorDao) {
        this.ingredienteDao = ingredienteDao;
        this.proveedorDao = proveedorDao;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<IngredienteResponseRest> buscarTodos() {
        IngredienteResponseRest response = new IngredienteResponseRest();
        try {
            List<Ingrediente> ingredientes = (List<Ingrediente>) ingredienteDao.findAll();
            if (ingredientes.isEmpty()) {
                response.setMetadata("Respuesta ok", "01", "No existen ingredientes");
            } else {
                response.getIngredienteResponse().setIngredientes(ingredientes);
                response.setMetadata("Respuesta ok", "00", "Respuesta exitosa");
            }
        } catch (Exception e) {
            response.setMetadata("Respuesta nok", "-1", "Error al consultar");
            logger.error("Error al consultar ingredientes: ", e);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<IngredienteResponseRest> buscarPorId(Long id) {
        IngredienteResponseRest response = new IngredienteResponseRest();
        try {
            Ingrediente ingrediente = ingredienteDao.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Ingrediente no encontrado"));
            response.getIngredienteResponse().setIngredientes(List.of(ingrediente));
            response.setMetadata("Respuesta ok", "00", "Ingrediente encontrado");
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
    public ResponseEntity<IngredienteResponseRest> buscarPorNombre(String nombre) {
        IngredienteResponseRest response = new IngredienteResponseRest();
        try {
            List<Ingrediente> ingredientes = ingredienteDao.findByNombreContainingIgnoreCase(nombre);
            if (!ingredientes.isEmpty()) {
                response.getIngredienteResponse().setIngredientes(ingredientes);
                response.setMetadata("Respuesta ok", "00", "Ingrediente encontrado");
            } else {
                response.setMetadata("Respuesta nok", "-1", "Ingrediente no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.setMetadata("Respuesta nok", "-1", "Error al consultar por nombre");
            logger.error("Error al consultar ingrediente por nombre: ", e);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<IngredienteResponseRest> crear(Ingrediente ingrediente) {
        IngredienteResponseRest response = new IngredienteResponseRest();
        try {
            Proveedor proveedor = proveedorDao.findById(ingrediente.getProveedorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado"));
            ingrediente.setProveedor(proveedor);

            Ingrediente ingredienteNuevo = ingredienteDao.save(ingrediente);
            response.getIngredienteResponse().setIngredientes(List.of(ingredienteNuevo));
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

    @Override
    @Transactional
    public ResponseEntity<IngredienteResponseRest> actualizar(Ingrediente ingrediente, Long id) {
        IngredienteResponseRest response = new IngredienteResponseRest();
        try {
            Ingrediente ingredienteEncontrado = ingredienteDao.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Ingrediente no encontrado"));
            actualizarIngrediente(ingrediente, ingredienteEncontrado);
            Ingrediente ingredienteActualizado = ingredienteDao.save(ingredienteEncontrado);
            response.getIngredienteResponse().setIngredientes(List.of(ingredienteActualizado));
            response.setMetadata("Respuesta ok", "00", "Ingrediente actualizado");
        } catch (ResourceNotFoundException e) {
            logger.error("Error al actualizar el ingrediente", e);
            response.setMetadata("Respuesta nok", "-1", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            response.setMetadata("Respuesta nok", "-1", "Error al actualizar ingrediente");
            logger.error("Error al actualizar ingrediente: ", e);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<IngredienteResponseRest> eliminar(Long id) {
        IngredienteResponseRest response = new IngredienteResponseRest();
        try {
            ingredienteDao.deleteById(id);
            response.setMetadata("Respuesta ok", "00", "Ingrediente eliminado");
        } catch (Exception e) {
            response.setMetadata("Respuesta nok", "-1", "Error al eliminar ingrediente");
            logger.error("Error al eliminar ingrediente: ", e);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void actualizarIngrediente(Ingrediente source, Ingrediente target) {
        if (source.getNombre() != null) {
            target.setNombre(source.getNombre());
        }
        if (source.getCantidad() != 0) {
            target.setCantidad(source.getCantidad());
        }
        if (source.getUnidad() != null) {
            target.setUnidad(source.getUnidad());
        }
        if (source.getProveedor() != null) {
            target.setProveedor(source.getProveedor());
        }
    }
}
