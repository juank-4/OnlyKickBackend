package com.example.OnlyKick.controller;

import com.example.OnlyKick.model.Comuna;
import com.example.OnlyKick.service.ComunaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/comunas")
public class ComunaController {

    @Autowired
    private ComunaService comunaService;

    @GetMapping
    public ResponseEntity<List<Comuna>> getAll() {
        List<Comuna> list = comunaService.findAll();
        if (list.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/region/{idRegion}")
    public ResponseEntity<List<Comuna>> getByRegionId(@PathVariable Integer idRegion) {
        List<Comuna> list = comunaService.findByRegionId(idRegion);
        if (list.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comuna> getById(@PathVariable Integer id) {
        Comuna obj = comunaService.findById(id);
        if (obj == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(obj);
    }

    @PostMapping
    public ResponseEntity<Comuna> create(@RequestBody Comuna comuna) {
        return ResponseEntity.status(HttpStatus.CREATED).body(comunaService.save(comuna));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comuna> update(@PathVariable Integer id, @RequestBody Comuna comuna) {
        comuna.setId_comuna(id);
        return ResponseEntity.ok(comunaService.save(comuna));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Comuna> partialUpdate(@PathVariable Integer id, @RequestBody Comuna comuna) {
        Comuna existing = comunaService.findById(id);
        if (existing == null) return ResponseEntity.notFound().build();

        if (comuna.getNombreComuna() != null) {
            existing.setNombreComuna(comuna.getNombreComuna());
        }
        if (comuna.getRegion() != null) {
            existing.setRegion(comuna.getRegion());
        }
        return ResponseEntity.ok(comunaService.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        comunaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}