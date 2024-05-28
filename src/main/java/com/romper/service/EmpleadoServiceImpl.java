package com.romper.service;

import com.romper.dao.IEmpleadoDao;
import com.romper.model.Empleado;
import com.romper.response.EmpleadoResponseRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoServiceImpl implements IEmpleadoService {

    private static final Logger logger = LoggerFactory.getLogger(EmpleadoServiceImpl.class);

    private final IEmpleadoDao employeeDao;

    @Autowired
    public EmpleadoServiceImpl(IEmpleadoDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<EmpleadoResponseRest> search() {
        EmpleadoResponseRest response = new EmpleadoResponseRest();
        try {
            List<Empleado> employees = (List<Empleado>) employeeDao.findAll();
            response.getEmpleadoResponse().setEmpleado(employees);
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
    public ResponseEntity<EmpleadoResponseRest> searchById(Long id) {
        EmpleadoResponseRest response = new EmpleadoResponseRest();
        try {
            Optional<Empleado> employee = employeeDao.findById(id);
            if (employee.isPresent()) {
                response.getEmpleadoResponse().setEmpleado(List.of(employee.get()));
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
    public ResponseEntity<EmpleadoResponseRest> save(Empleado employee) {
        EmpleadoResponseRest response = new EmpleadoResponseRest();
        try {
            Empleado employeeSaved = employeeDao.save(employee);
            if (employeeSaved != null) {
                response.getEmpleadoResponse().setEmpleado(List.of(employeeSaved));
                response.setMetadata("Ok", "00", "Empleado guardado");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.setMetadata("!Ok", "-1", "No se pudo guardar el empleado");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            logger.error("Error en save: ", e);
            response.setMetadata("!Ok", "-1", "Error al grabar el empleado");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<EmpleadoResponseRest> update(Empleado employee, Long id) {
        EmpleadoResponseRest response = new EmpleadoResponseRest();
        try {
            Optional<Empleado> employeeFound = employeeDao.findById(id);
            if (employeeFound.isPresent()) {
                setEmployeeData(employee, employeeFound.get());
                Empleado employeeUpdated = employeeDao.save(employeeFound.get());
                if (employeeUpdated != null) {
                    response.getEmpleadoResponse().setEmpleado(List.of(employeeUpdated));
                    response.setMetadata("Ok", "00", "Empleado actualizado");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    response.setMetadata("!Ok", "-1", "No se pudo actualizar el empleado");
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
            } else {
                response.setMetadata("!Ok", "-1", "No existe el empleado con id: " + id);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error en update: ", e);
            response.setMetadata("!Ok", "-1", "Error al actualizar el empleado");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<EmpleadoResponseRest> deleteById(Long id) {
        EmpleadoResponseRest response = new EmpleadoResponseRest();
        try {
            employeeDao.deleteById(id);
            response.setMetadata("Ok", "00", "Empleado eliminado");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setMetadata("!Ok", "-1", "Error al consultar empleado por id");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void setEmployeeData(Empleado source, Empleado target) {
        target.setTipoIdentificacion(source.getTipoIdentificacion());
        target.setCedula(source.getCedula());
        target.setEstado(source.getEstado());
        target.setNombre(source.getNombre());
        target.setApellidoPaterno(source.getApellidoPaterno());
        target.setApellidoMaterno(source.getApellidoMaterno());
        target.setFechaNacimiento(source.getFechaNacimiento());
        target.setSexo(source.getSexo());
        target.setNacionalidad(source.getNacionalidad());
        target.setEstadoCivil(source.getEstadoCivil());
        target.setFechaSalida(source.getFechaSalida());
        target.setGradoAcademico(source.getGradoAcademico());
        target.setTitulo(source.getTitulo());
        target.setSalario(source.getSalario());
        target.setTelefono(source.getTelefono());
        target.setCelular(source.getCelular());
        target.setCallePrincipal(source.getCallePrincipal());
        target.setCalleSecundaria(source.getCalleSecundaria());
        target.setCorreoPersonal(source.getCorreoPersonal());
        target.setCorreoInstitucional(source.getCorreoInstitucional());
    }
}
