package com.romper.service;

import com.romper.dao.ICargoDao;
import com.romper.dao.IDepartamentoDao;
import com.romper.model.Cargo;
import com.romper.model.Departamento;
import com.romper.response.CargoResponseRest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class CargoServiceImpl implements ICargoService {

    private final ICargoDao cargoDao;

    private final IDepartamentoDao departamentoDao;

    private static final Logger logger = LoggerFactory.getLogger(CargoServiceImpl.class);

    public CargoServiceImpl(ICargoDao cargoDao, IDepartamentoDao departamentoDao) {
        this.cargoDao = cargoDao;
        this.departamentoDao = departamentoDao;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<CargoResponseRest> buscarTodos() {
        CargoResponseRest response = new CargoResponseRest();
        try {
            List<Cargo> cargos = (List<Cargo>) cargoDao.findAll();
            response.getCargoResponse().setCargos(cargos);
            response.setMetadata("Respuesta ok", "00", "Respuesta exitosa");
        } catch (Exception e) {
            logger.error("Error al consultar todos los cargos", e);
            response.setMetadata("Respuesta nok", "-1", "Error al consultar");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<CargoResponseRest> buscarPorId(Long id) {
        CargoResponseRest response = new CargoResponseRest();
        try {
            Cargo cargo = cargoDao.findById(id)
                    .orElseThrow(() -> new RuntimeException("Cargo no encontrado"));
            response.getCargoResponse().setCargos(List.of(cargo));
            response.setMetadata("Respuesta ok", "00", "Cargo encontrado");
        } catch (RuntimeException e) {
            logger.error("Error al consultar el cargo por id", e);
            response.setMetadata("Respuesta nok", "-1", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error al consultar el cargo por id", e);
            response.setMetadata("Respuesta nok", "-1", "Error al consultar por id");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<CargoResponseRest> buscarPorNombre(String nombre) {
        CargoResponseRest response = new CargoResponseRest();
        try {
            List<Cargo> cargos = cargoDao.findByDescripcionContainingIgnoreCase(nombre);
            if (!cargos.isEmpty()) {
                response.getCargoResponse().setCargos(cargos);
                response.setMetadata("Respuesta ok", "00", "Cargo encontrado");
            } else {
                response.setMetadata("Respuesta nok", "-1", "Cargo no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error al consultar el cargo por nombre", e);
            response.setMetadata("Respuesta nok", "-1", "Error al consultar por nombre");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<CargoResponseRest> crear(Cargo cargo) {
        CargoResponseRest response = new CargoResponseRest();
        try {
            if (existeCodigoCargo(cargo.getCodigo())) {
                response.setMetadata("Respuesta nok", "-1", "Cargo con código duplicado");
                return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
            }

            Departamento departamento = departamentoDao.findById(cargo.getDepartamentoId())
                    .orElseThrow(() -> new RuntimeException("Departamento no encontrado"));
            cargo.setDepartamento(departamento);

            Cargo cargoGuardado = cargoDao.save(cargo);
            response.getCargoResponse().setCargos(List.of(cargoGuardado));
            response.setMetadata("Respuesta ok", "00", "Cargo guardado");
        } catch (RuntimeException e) {
            logger.error("Error al crear el cargo", e);
            response.setMetadata("Respuesta nok", "-1", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error al crear el cargo", e);
            response.setMetadata("Respuesta nok", "-1", "Error al crear cargo");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    @Transactional
    public ResponseEntity<CargoResponseRest> actualizar(Cargo cargo, Long id) {
        CargoResponseRest response = new CargoResponseRest();
        try {
            Cargo cargoExistente = cargoDao.findById(id)
                    .orElseThrow(() -> new RuntimeException("Cargo no encontrado"));

            if (existeCodigoCargo(cargo.getCodigo()) && !cargo.getCodigo().equals(cargoExistente.getCodigo())) {
                response.setMetadata("Respuesta nok", "-1", "Cargo con código duplicado");
                return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
            }

            actualizarDatosCargo(cargo, cargoExistente);
            Cargo cargoActualizado = cargoDao.save(cargoExistente);
            response.getCargoResponse().setCargos(List.of(cargoActualizado));
            response.setMetadata("Respuesta ok", "00", "Cargo actualizado");
        } catch (RuntimeException e) {
            logger.error("Error al actualizar el cargo", e);
            response.setMetadata("Respuesta nok", "-1", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error al actualizar el cargo", e);
            response.setMetadata("Respuesta nok", "-1", "Error al actualizar cargo");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<CargoResponseRest> eliminar(Long id) {
        CargoResponseRest response = new CargoResponseRest();
        try {
            cargoDao.deleteById(id);
            response.setMetadata("Respuesta ok", "00", "Registro eliminado");
        } catch (Exception e) {
            logger.error("Error al eliminar el cargo", e);
            response.setMetadata("Respuesta nok", "-1", "Error al eliminar");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private boolean existeCodigoCargo(String codigo) {
        return cargoDao.findByCodigo(codigo).isPresent();
    }

    private void actualizarDatosCargo(Cargo source, Cargo target) {
        target.setDescripcion(source.getDescripcion());
        target.setCodigo(source.getCodigo());
        target.setSalario(source.getSalario());
    }
}