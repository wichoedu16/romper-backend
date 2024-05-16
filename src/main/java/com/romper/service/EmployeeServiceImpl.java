package com.romper.service;

import com.romper.dao.IEmployeeDao;
import com.romper.model.Employee;
import com.romper.response.EmployeeResponseRest;
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
public class EmployeeServiceImpl implements IEmployeeService{

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final IEmployeeDao employeeDao;

    @Autowired
    public EmployeeServiceImpl(IEmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<EmployeeResponseRest> search() {
        EmployeeResponseRest response = new EmployeeResponseRest();
        try {
            List<Employee> employees = (List<Employee>) employeeDao.findAll();
            response.getEmployeeResponse().setEmployee(employees);
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
    public ResponseEntity<EmployeeResponseRest> searchById(Long id) {
        EmployeeResponseRest response = new EmployeeResponseRest();
        try {
            Optional<Employee> employee = employeeDao.findById(id);
            if (employee.isPresent()) {
                response.getEmployeeResponse().setEmployee(List.of(employee.get()));
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
    public ResponseEntity<EmployeeResponseRest> save(Employee employee) {
        EmployeeResponseRest response = new EmployeeResponseRest();
        try {
            Employee employeeSaved = employeeDao.save(employee);
            if (employeeSaved != null) {
                response.getEmployeeResponse().setEmployee(List.of(employeeSaved));
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
    public ResponseEntity<EmployeeResponseRest> update(Employee employee, Long id) {
        EmployeeResponseRest response = new EmployeeResponseRest();
        try {
            Optional<Employee> employeeFound = employeeDao.findById(id);
            if (employeeFound.isPresent()) {
                setEmployeeData(employee, employeeFound.get());
                Employee employeeUpdated = employeeDao.save(employeeFound.get());
                if (employeeUpdated != null) {
                    response.getEmployeeResponse().setEmployee(List.of(employeeUpdated));
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

    private void setEmployeeData(Employee source, Employee target) {
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
