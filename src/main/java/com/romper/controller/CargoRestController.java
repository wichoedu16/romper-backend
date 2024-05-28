package com.romper.controller;

import com.romper.model.Cargo;
import com.romper.response.CargoResponseRest;
import com.romper.service.ICargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1")
public class CargoRestController {
    private final ICargoService cargoService;

    public CargoRestController(ICargoService cargoService){
        this.cargoService = cargoService;
    }

    /**
     * Get all cargos
     * @return List of Cargos
     */
    @GetMapping("/cargos")
    public ResponseEntity<CargoResponseRest> searchAll() {
        return cargoService.search();
    }

    /**
     * Get an cargo by id
     * @param id cargo ID
     * @return cargo
     */
    @GetMapping("/cargos/{id}")
    public ResponseEntity<CargoResponseRest> searchById(@PathVariable Long id) {
        return cargoService.searchById(id);
    }

    /**
     * Save cargo
     * @param cargo cargo object
     * @return Saved departament
     */
    @PostMapping("/cargos")
    public ResponseEntity<CargoResponseRest> save(@RequestBody Cargo cargo){
        return cargoService.save(cargo);
    }
}
