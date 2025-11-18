package com.example.OnlyKick.controller;

import com.example.OnlyKick.model.Genero;
import com.example.OnlyKick.service.GeneroService;
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
@RequestMapping("/api/generos")
public class GeneroController {

    @Autowired
    private GeneroService generoService;

    @GetMapping
    public ResponseEntity<List<Genero>> getAll() {
        List<Genero> list = generoService.findAll();
        if (list.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genero> getById(@PathVariable Integer id) {
        Genero obj = generoService.findById(id);
        if (obj == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(obj);
    }

    @PostMapping
    public ResponseEntity<Genero> create(@RequestBody Genero genero) {
        return ResponseEntity.status(HttpStatus.CREATED).body(generoService.save(genero));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Genero> update(@PathVariable Integer id, @RequestBody Genero genero) {
        genero.setId_genero(id);
        return ResponseEntity.ok(generoService.save(genero));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Genero> partialUpdate(@PathVariable Integer id, @RequestBody Genero genero) {
        Genero existing = generoService.findById(id);
        if (existing == null) return ResponseEntity.notFound().build();

        if (genero.getNombreGenero() != null) {
            existing.setNombreGenero(genero.getNombreGenero());
        }
        return ResponseEntity.ok(generoService.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        generoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}