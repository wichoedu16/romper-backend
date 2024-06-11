package com.romper.controller;

import com.romper.model.Cargo;
import com.romper.response.CargoResponseRest;
import com.romper.service.ICargoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1/cargos")
public class CargoRestController {
    private final ICargoService cargoService;

    public CargoRestController(ICargoService cargoService){
        this.cargoService = cargoService;
    }

    /**
     * Obtener todos los cargos
     * @return lista de cargos
     */
    @GetMapping()
    public ResponseEntity<CargoResponseRest> buscarTodos() {
        return cargoService.buscarTodos();
    }

    /**
     * Obtener cargo id
     * @return cargo
     */
    @GetMapping("/{id}")
    public ResponseEntity<CargoResponseRest> buscarPorId(@PathVariable Long id) {
        return cargoService.buscarPorId(id);
    }

    /**
     * Obtener lista de cargos por nombre
     * @return lista de cargos
     */
    @GetMapping("/filter/{nombre}")
    public ResponseEntity<CargoResponseRest> buscarPorNombre(@PathVariable String nombre) {
        return cargoService.buscarPorNombre(nombre);
    }

    /**
     * Guardar cargo
     * @return cargo guardado
     */
    @PostMapping()
    public ResponseEntity<CargoResponseRest> crear(@RequestBody Cargo cargo){
        return cargoService.crear(cargo);
    }


    /**
     * actualizar cargos
     */
    @PutMapping("/{id}")
    public ResponseEntity<CargoResponseRest> actualizar(@RequestBody Cargo category, @PathVariable Long id) {
        return cargoService.actualizar(category, id);
    }

    /**
     * eliminar un cargos
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<CargoResponseRest> eliminar(@PathVariable Long id) {
        return cargoService.eliminar(id);
    }
}
