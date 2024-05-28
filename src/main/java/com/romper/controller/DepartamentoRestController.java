package com.romper.controller;

import com.romper.model.Departamento;
import com.romper.response.DepartamentoResponseRest;
import com.romper.service.IDepartamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1")
public class DepartamentoRestController {
    private final IDepartamentoService departamentoService;

    public DepartamentoRestController(IDepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }

        /**
         * Get all departaments
         * @return List of Departaments
         */
        @GetMapping("/departaments")
        public ResponseEntity<DepartamentoResponseRest> searchAll() {
        return departamentoService.search();
    }

        /**
         * Get an departament by id
         * @param id departament ID
         * @return departament
         */
        @GetMapping("/departaments/{id}")
        public ResponseEntity<DepartamentoResponseRest> searchById(@PathVariable Long id) {
        return departamentoService.searchById(id);
    }

        /**
         * Save departament
         * @param departamento departament object
         * @return Saved departament
         */
        @PostMapping("/departaments")
        public ResponseEntity<DepartamentoResponseRest> save(@RequestBody Departamento departamento) {
        return departamentoService.save(departamento);
    }

        /**
         * Update departament
         * @param departamento departament object
         * @param id departament ID
         * @return Updated departament
         */
        @PutMapping("/departaments/{id}")
        public ResponseEntity<DepartamentoResponseRest> update(@RequestBody Departamento departamento, @PathVariable Long id) {
        return departamentoService.update(departamento, id);
    }

        /**
         * Delete departament by Id
         * @param id departament ID
         * @return Deleted departament
         */
        @DeleteMapping("/departament/{id}")
        public ResponseEntity<DepartamentoResponseRest> delete(@PathVariable Long id) {
        return departamentoService.deleteById(id);
    }
}

