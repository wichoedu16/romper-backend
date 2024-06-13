package com.romper.service;


import com.romper.dao.IIngredienteDao;
import com.romper.dao.IProveedorDao;
import com.romper.model.Ingrediente;
import com.romper.model.Proveedor;
import com.romper.response.IngredienteResponseRest;
import com.romper.response.ProveedorResponseRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ProveedorServiceImpl implements IProveedorService {
    private static final Logger logger = LoggerFactory.getLogger(ProveedorServiceImpl.class);
    private final IProveedorDao proveedorDao;
    private final IIngredienteDao ingredienteDao;

    @Autowired
    public ProveedorServiceImpl(IProveedorDao proveedorDao, IIngredienteDao ingredienteDao) {
        this.proveedorDao = proveedorDao;
        this.ingredienteDao = ingredienteDao;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ProveedorResponseRest> buscarTodos() {
        ProveedorResponseRest response = new ProveedorResponseRest();
        try {
            List<Proveedor> proveedores = (List<Proveedor>) proveedorDao.findAll();
            if(proveedores.isEmpty()){
                response.setMetadata("Respuesta ok", "01", "No existen proveedores");
            }else{
                response.getProveedorResponse().setProveedores(proveedores);
                response.setMetadata("Respuesta ok", "00", "Respuesta exitosa");
            }

        } catch (Exception e) {
            logger.error("Error al consultar todos los Proveedores", e);
            response.setMetadata("Respuesta nok", "-1", "Error al consultar");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ProveedorResponseRest> buscarPorId(Long id) {
        ProveedorResponseRest response = new ProveedorResponseRest();
        try {
            Proveedor proveedor = proveedorDao.findById(id)
                    .orElseThrow(() -> new RuntimeException("proveedor no encontrado"));
            response.getProveedorResponse().setProveedores(List.of(proveedor));
            response.setMetadata("Respuesta ok", "00", "proveedor encontrado");
        } catch (RuntimeException e) {
            logger.error("Error al consultar el proveedor por id", e);
            response.setMetadata("Respuesta nok", "-1", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error al consultar el proveedor por id", e);
            response.setMetadata("Respuesta nok", "-1", "Error al consultar por id");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<ProveedorResponseRest> crear(Proveedor proveedor) {
        ProveedorResponseRest response = new ProveedorResponseRest();
        try {
            Proveedor proveedorGuardado = proveedorDao.save(proveedor);
            response.getProveedorResponse().setProveedores(List.of(proveedorGuardado));
            response.setMetadata("Respuesta ok", "00", "Proveedor guardado");
        } catch (Exception e) {
            logger.error("Error al crear el proveedor", e);
            response.setMetadata("Respuesta nok", "-1", "Error al crear Proveedor");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProveedorResponseRest> actualizar(Proveedor proveedor, Long id) {
        ProveedorResponseRest response = new ProveedorResponseRest();
        try {
            Proveedor proveedorBuscado = proveedorDao.findById(id)
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
            actualizarDatosProveedor(proveedor, proveedorBuscado);
            Proveedor proveedorActualizado = proveedorDao.save(proveedorBuscado);
            response.getProveedorResponse().setProveedores(List.of(proveedorActualizado));
            response.setMetadata("Respuesta ok", "00", "Proveedor actualizado");
        } catch (RuntimeException e) {
            logger.error("Error al actualizar el proveedor", e);
            response.setMetadata("Respuesta nok", "-1", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error al actualizar el proveedor", e);
            response.setMetadata("Respuesta nok", "-1", "Error al actualizar proveedor");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProveedorResponseRest> eliminar(Long id) {
        ProveedorResponseRest response = new ProveedorResponseRest();
        try {
            proveedorDao.deleteById(id);
            response.setMetadata("respuesta ok", "00", "Proveedor eliminado");
        } catch (Exception e) {
            logger.error("Error al eliminar el proveedor", e);
            response.setMetadata("Respuesta nok", "-1", "Error al eliminar");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<IngredienteResponseRest> buscarIngredientesPorId(Long id) {
        IngredienteResponseRest response = new IngredienteResponseRest();
        try{
            List<Ingrediente> ingredientes = ingredienteDao.findIngredienteByProveedorId(id);
            if(ingredientes.isEmpty()){
                response.setMetadata("Respuesta ok", "01", "No existen ingredientes");
            }else{
                response.getIngredienteResponse().setIngredientes(ingredientes);
                response.setMetadata("Respuesta ok", "00", "Respuesta exitosa");
            }
        } catch (Exception e){
            logger.error("Error al buscar los ingredientes", e);
            response.setMetadata("Respuesta nok", "-1", "Error al buscar los ingredientes");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void actualizarDatosProveedor(Proveedor source, Proveedor target) {
        target.setEmpresa(source.getEmpresa());
        target.setNombre(source.getNombre());
        target.setContacto(source.getContacto());
    }
}