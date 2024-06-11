package com.romper.service;

import com.romper.dao.ICargoDao;
import com.romper.dao.IEmpleadoDao;
import com.romper.model.Cargo;
import com.romper.model.Empleado;
import com.romper.response.CargoResponseRest;
import com.romper.response.EmpleadoResponseRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.EmptyStackException;
import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoServiceImpl implements IEmpleadoService {

    private static final Logger logger = LoggerFactory.getLogger(EmpleadoServiceImpl.class);

    private final IEmpleadoDao empleadoDao;
    private final ICargoDao cargoDao;

    @Autowired
    public EmpleadoServiceImpl(IEmpleadoDao empleadoDao, ICargoDao cargoDao) {
        this.empleadoDao = empleadoDao;
        this.cargoDao = cargoDao;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<EmpleadoResponseRest> buscarTodos() {
        EmpleadoResponseRest response = new EmpleadoResponseRest();
        try {
            List<Empleado> empleados = (List<Empleado>) empleadoDao.findAll();
            response.getEmpleadoResponse().setEmpleados(empleados);
            response.setMetadata("Respuesta ok", "00", "Respuesta exitosa");
        } catch (Exception e) {
            logger.error("Error al consultar todos los empleados", e);
            response.setMetadata("Respuesta nok", "-1", "Error al consultar");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<EmpleadoResponseRest> buscarPorId(Long id) {
        EmpleadoResponseRest response = new EmpleadoResponseRest();
        try {
            Empleado empleado = empleadoDao.findById(id)
                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
            response.getEmpleadoResponse().setEmpleados(List.of(empleado));
            response.setMetadata("Respuesta ok", "00", "Empleado encontrado");
        } catch (RuntimeException e) {
            logger.error("Error al consultar el cargo por id", e);
            response.setMetadata("Respuesta nok", "-1", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error al consultar el empleado por id", e);
            response.setMetadata("Respuesta nok", "-1", "Error al consultar por id");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<EmpleadoResponseRest> buscarPorNombre(String nombre) {
        EmpleadoResponseRest response = new EmpleadoResponseRest();
        try {
            List<Empleado> empleados = empleadoDao.findByNombreContainingOrApellidoPaternoContainingOrApellidoMaternoContaining(nombre, nombre, nombre);
            if (!empleados.isEmpty()) {
                response.getEmpleadoResponse().setEmpleados(empleados);
                response.setMetadata("Respuesta ok", "00", "Empleado encontrado");
            } else {
                response.setMetadata("Respuesta nok", "-1", "Empleado no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error al consultar el empleado por nombre", e);
            response.setMetadata("Respuesta nok", "-1", "Error al consultar por nombre");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<EmpleadoResponseRest> crear(Empleado empleado) {
        EmpleadoResponseRest response = new EmpleadoResponseRest();
        try {
            Cargo cargo = cargoDao.findById(empleado.getCargoId())
                    .orElseThrow(() -> new RuntimeException("Cargo no encontrado"));
            empleado.setCargo(cargo);
            Empleado empleadoGuardado = empleadoDao.save(empleado);
            response.getEmpleadoResponse().setEmpleados(List.of(empleadoGuardado));
            response.setMetadata("Respuesta ok", "00", "Empleado guardado");
        }catch (RuntimeException e){
            logger.error("Error al crear el empleado", e);
            response.setMetadata("Respuesta nok", "-1", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }catch (Exception e){
            logger.error("Error al crear el empleado", e);
            response.setMetadata("Respuesta nok", "-1", "Error al crear cargo");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<EmpleadoResponseRest> actualizar(Empleado empleado, Long id) {
        EmpleadoResponseRest response = new EmpleadoResponseRest();
        try {
            Empleado empleadoExistente = empleadoDao.findById(id)
                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

            actualizarDatosEmpleado(empleado, empleadoExistente);
            Empleado empleadoActualizado = empleadoDao.save(empleadoExistente);
            response.getEmpleadoResponse().setEmpleados(List.of(empleadoActualizado));
            response.setMetadata("Respuesta ok", "00", "Empleado guardado");
        }catch (RuntimeException e){
            logger.error("Error al crear el empleado", e);
            response.setMetadata("Respuesta nok", "-1", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }catch (Exception e){
            logger.error("Error al crear el empleado", e);
            response.setMetadata("Respuesta nok", "-1", "Error al crear cargo");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<EmpleadoResponseRest> eliminar(Long id) {
        EmpleadoResponseRest response = new EmpleadoResponseRest();
        try {
            empleadoDao.deleteById(id);
            response.setMetadata("Ok", "00", "Registro eliminado");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error al eliminar el empleado", e);
            response.setMetadata("!Ok", "-1", "Error al eliminar");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void actualizarDatosEmpleado(Empleado source, Empleado target) {
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
