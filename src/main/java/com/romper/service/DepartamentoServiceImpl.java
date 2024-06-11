package com.romper.service;

import com.romper.dao.IDepartamentoDao;
import com.romper.model.Departamento;
import com.romper.response.DepartamentoResponseRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class DepartamentoServiceImpl implements IDepartamentoService {

    @Autowired
    private IDepartamentoDao departamentoDao;

    private static final Logger logger = LoggerFactory.getLogger(DepartamentoServiceImpl.class);

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<DepartamentoResponseRest> buscarTodos() {
        DepartamentoResponseRest response = new DepartamentoResponseRest();
        try {
            List<Departamento> departamentos = (List<Departamento>) departamentoDao.findAll();
            response.getDepartamentoResponse().setDepartamentos(departamentos);
            response.setMetadata("Respuesta ok", "00", "Respuesta exitosa");
        } catch (Exception e) {
            logger.error("Error al consultar todos los departamentos", e);
            response.setMetadata("Respuesta nok", "-1", "Error al consultar");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<DepartamentoResponseRest> buscarPorId(Long id) {
        DepartamentoResponseRest response = new DepartamentoResponseRest();
        try {
            Departamento departamento = departamentoDao.findById(id)
                    .orElseThrow(() -> new RuntimeException("Departamento no encontrado"));
            response.getDepartamentoResponse().setDepartamentos(List.of(departamento));
            response.setMetadata("Respuesta ok", "00", "Departamento encontrado");
        } catch (RuntimeException e) {
            logger.error("Error al consultar el departamento por id", e);
            response.setMetadata("Respuesta nok", "-1", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error al consultar el departamento por id", e);
            response.setMetadata("Respuesta nok", "-1", "Error al consultar por id");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<DepartamentoResponseRest> buscarPorNombre(String nombre) {
        DepartamentoResponseRest response = new DepartamentoResponseRest();
        try {
            List<Departamento> departamentos = departamentoDao.findByDescripcionContainingIgnoreCase(nombre);
            if (!departamentos.isEmpty()) {
                response.getDepartamentoResponse().setDepartamentos(departamentos);
                response.setMetadata("Respuesta ok", "00", "Departamento encontrado");
            } else {
                response.setMetadata("Respuesta nok", "-1", "Departamento no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error al consultar el departamento por nombre", e);
            response.setMetadata("Respuesta nok", "-1", "Error al consultar por nombre");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<DepartamentoResponseRest> crear(Departamento departamento) {
        DepartamentoResponseRest response = new DepartamentoResponseRest();
        try {
            if (existeCodigoDepartamento(departamento.getCodigo())) {
                response.setMetadata("Respuesta nok", "-1", "Departamento con código duplicado");
                return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
            }
            Departamento departamentoGuardado = departamentoDao.save(departamento);
            response.getDepartamentoResponse().setDepartamentos(List.of(departamentoGuardado));
            response.setMetadata("Respuesta ok", "00", "Departamento guardado");
        } catch (Exception e) {
            logger.error("Error al crear el departamento", e);
            response.setMetadata("Respuesta nok", "-1", "Error al crear departamento");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<DepartamentoResponseRest> actualizar(Departamento departamento, Long id) {
        DepartamentoResponseRest response = new DepartamentoResponseRest();
        try {
            Departamento departamentoExistente = departamentoDao.findById(id)
                    .orElseThrow(() -> new RuntimeException("Departamento no encontrado"));
            if (existeCodigoDepartamento(departamento.getCodigo()) && !departamento.getCodigo().equals(departamentoExistente.getCodigo())) {
                response.setMetadata("Respuesta nok", "-1", "Departamento con código duplicado");
                return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
            }
            actualizarDatosDepartamento(departamento, departamentoExistente);
            Departamento departamentoActualizado = departamentoDao.save(departamentoExistente);
            response.getDepartamentoResponse().setDepartamentos(List.of(departamentoActualizado));
            response.setMetadata("Respuesta ok", "00", "Departamento actualizado");
        } catch (RuntimeException e) {
            logger.error("Error al actualizar el departamento", e);
            response.setMetadata("Respuesta nok", "-1", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error al actualizar el departamento", e);
            response.setMetadata("Respuesta nok", "-1", "Error al actualizar departamento");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<DepartamentoResponseRest> eliminar(Long id) {
        DepartamentoResponseRest response = new DepartamentoResponseRest();
        try {
            departamentoDao.deleteById(id);
            response.setMetadata("respuesta ok", "00", "Registro eliminado");
        } catch (Exception e) {
            logger.error("Error al eliminar el departamento", e);
            response.setMetadata("Respuesta nok", "-1", "Error al eliminar");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private boolean existeCodigoDepartamento(String codigo) {
        return departamentoDao.findDepartamentoByCodigo(codigo).isPresent();
    }

    private void actualizarDatosDepartamento(Departamento source, Departamento target) {
        target.setDescripcion(source.getDescripcion());
        target.setCodigo(source.getCodigo());
        target.setEstado(source.getEstado());
    }
}