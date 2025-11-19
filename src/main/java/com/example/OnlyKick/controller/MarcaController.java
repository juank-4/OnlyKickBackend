package com.example.OnlyKick.controller;

import com.example.OnlyKick.model.Marca;
import com.example.OnlyKick.service.MarcaService;
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
@RequestMapping("/api/marcas")
public class MarcaController {

    @Autowired
    private MarcaService marcaService;

    @GetMapping
    public ResponseEntity<List<Marca>> getAll() {
        List<Marca> list = marcaService.findAll();
        if (list.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Marca> getById(@PathVariable Integer id) {
        Marca obj = marcaService.findById(id);
        if (obj == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(obj);
    }

    @PostMapping
    public ResponseEntity<Marca> create(@RequestBody Marca marca) {
        return ResponseEntity.status(HttpStatus.CREATED).body(marcaService.save(marca));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Marca> update(@PathVariable Integer id, @RequestBody Marca marca) {
        marca.setIdMarca(id);
        return ResponseEntity.ok(marcaService.save(marca));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Marca> partialUpdate(@PathVariable Integer id, @RequestBody Marca marca) {
        Marca existing = marcaService.findById(id);
        if (existing == null) return ResponseEntity.notFound().build();
        
        if (marca.getNombreMarca() != null) {
            existing.setNombreMarca(marca.getNombreMarca());
        }
        return ResponseEntity.ok(marcaService.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        marcaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}