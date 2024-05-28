package com.romper.service;

import com.romper.dao.ICargoDao;
import com.romper.dao.IDepartamentoDao;
import com.romper.model.Cargo;
import com.romper.model.Departamento;
import com.romper.response.CargoResponseRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CargoServiceImpl implements ICargoService{
    private static final Logger logger = LoggerFactory.getLogger(CargoServiceImpl.class);
    private final ICargoDao cargoDao;
    private final IDepartamentoDao departamentoDao;

    @Autowired
    public CargoServiceImpl(ICargoDao cargoDao, IDepartamentoDao departamentoDao) {
        this.cargoDao = cargoDao;
        this.departamentoDao = departamentoDao;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<CargoResponseRest> search() {
        CargoResponseRest response = new CargoResponseRest();
        try {
            List<Cargo> cargo = (List<Cargo>) cargoDao.findAll();
            response.getCargoResponse().setCargo(cargo);
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
    public ResponseEntity<CargoResponseRest> searchById(Long id) {
        CargoResponseRest response = new CargoResponseRest();
        try {
            Optional<Cargo> cargo = cargoDao.findById(id);
            if (cargo.isPresent()) {
                Optional<Departamento> departamento = departamentoDao.findById(id);
                response.getCargoResponse().setCargo(List.of(cargo.get()));
                response.setMetadata("Ok", "00", "Cargo encontrado");
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
    public ResponseEntity<CargoResponseRest> save(Cargo cargo) {
        CargoResponseRest response = new CargoResponseRest();
        List<Cargo> list = new ArrayList<>();
        try {
                Optional<Departamento> departamento = departamentoDao.findById(cargo.getDepartamento().getId());
                if (departamento.isPresent()){
                    cargo.setDepartamento(departamento.get());
                } else {
                    response.setMetadata("!Ok", "-1", "Departamento no encontrado");
                    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
                Cargo cargoSaved = cargoDao.save(cargo);
                if (cargoSaved != null){
                    list.add(cargoSaved);
                    response.getCargoResponse().setCargo(list);
                    response.setMetadata("Ok", "00", "Cargo guardado");
                }else {
                    response.setMetadata("!Ok", "-1", "No se pudo guardar el cargo");
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
            } catch (Exception e) {
                logger.error("Error en save: ", e);
                response.setMetadata("!Ok", "-1", "Error al grabar el departamento");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        return new ResponseEntity<CargoResponseRest>(response, HttpStatus.OK);
        }

    @Override
    @Transactional
    public ResponseEntity<CargoResponseRest> update(Cargo cargo, Long id) {
        CargoResponseRest response = new CargoResponseRest();
        try {
            Optional<Cargo> cargoFound = cargoDao.findById(id);
            if (cargoFound.isPresent()) {
                setCargoData(cargo, cargoFound.get());
                Cargo cargoUpdated = cargoDao.save(cargoFound.get());
                if (cargoUpdated != null) {
                    response.getCargoResponse().setCargo(List.of(cargoUpdated));
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
    public ResponseEntity<CargoResponseRest> deleteById(Long id) {
        CargoResponseRest response = new CargoResponseRest();
        try {
            cargoDao.deleteById(id);
            response.setMetadata("Ok", "00", "Cargo eliminado");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setMetadata("!Ok", "-1", "Error al consultar cargo por id");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void setCargoData(Cargo source, Cargo target) {
        target.setNombre(source.getNombre());
        target.setEstado(source.getEstado());
        target.setSalario(source.getSalario());
    }
}
