package com.romper.service;

import com.romper.dao.IDepartamentoDao;
import com.romper.dao.IEmpleadoDao;
import com.romper.model.Departamento;
import com.romper.model.Empleado;
import com.romper.response.DepartamentoResponseRest;
import com.romper.response.EmpleadoResponseRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DepartamentoServiceImpl implements IDepartamentoService{

    private static final Logger logger = LoggerFactory.getLogger(DepartamentoServiceImpl.class);

    private final IDepartamentoDao departamentoDao;

    @Autowired
    public DepartamentoServiceImpl(IDepartamentoDao departamentoDao)
    {
        this.departamentoDao = departamentoDao;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<DepartamentoResponseRest> search() {

        DepartamentoResponseRest response = new DepartamentoResponseRest();
        try {
            List<Departamento> departamentos = (List<Departamento>) departamentoDao.findAll();
            response.getDepartamentoResponse().setDepartamento(departamentos);
            response.setMetadata("Ok", "00", "Respuesta exitosa");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error en search: ", e);
            response.setMetadata("!Ok", "-1", "Error en respuesta");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<DepartamentoResponseRest> searchById(Long id) {
        DepartamentoResponseRest response = new DepartamentoResponseRest();
        try {
            Optional<Departamento> departamento = departamentoDao.findById(id);
            if (departamento.isPresent()) {
                response.getDepartamentoResponse().setDepartamento(List.of(departamento.get()));
                response.setMetadata("Ok", "00", "Empleado encontrado");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.setMetadata("!Ok", "-1", "No existe el id: " + id);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error en searchById: ", e);
            response.setMetadata("!Ok", "-1", "Error al consultar por id");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<DepartamentoResponseRest> save(Departamento departamento) {
            DepartamentoResponseRest response = new DepartamentoResponseRest();
            try {
                Departamento departamentoSaved = departamentoDao.save(departamento);
                if (departamentoSaved != null) {
                    response.getDepartamentoResponse().setDepartamento(List.of(departamentoSaved));
                    response.setMetadata("Ok", "00", "Departamento guardado");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    response.setMetadata("!Ok", "-1", "No se pudo guardar el departamento");
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
            } catch (Exception e) {
                logger.error("Error en save: ", e);
                response.setMetadata("!Ok", "-1", "Error al grabar el departamento");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

    @Override
    @Transactional
    public ResponseEntity<DepartamentoResponseRest> update(Departamento departamento, Long id) {
        DepartamentoResponseRest response = new DepartamentoResponseRest();
        try {
            Optional<Departamento> departamentFound = departamentoDao.findById(id);
            if (departamentFound.isPresent()) {
                setDepartamentData(departamento, departamentFound.get());
                Departamento departamentUpdated = departamentoDao.save(departamentFound.get());
                if (departamentUpdated != null) {
                    response.getDepartamentoResponse().setDepartamento(List.of(departamentUpdated));
                    response.setMetadata("Ok", "00", "Departamento actualizado");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    response.setMetadata("!Ok", "-1", "No se pudo actualizar el departamento");
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
            } else {
                response.setMetadata("!Ok", "-1", "No existe el departamento con id: " + id);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error en update: ", e);
            response.setMetadata("!Ok", "-1", "Error al actualizar el departamento");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<DepartamentoResponseRest> deleteById(Long id) {

        DepartamentoResponseRest response = new DepartamentoResponseRest();
        try {
            departamentoDao.deleteById(id);
            response.setMetadata("Ok", "00", "Departamento eliminado");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setMetadata("!Ok", "-1", "Error al consultar departamentos por id");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void setDepartamentData(Departamento source, Departamento target) {
        target.setNombre(source.getNombre());
        target.setEstado(source.getEstado());
    }
}
