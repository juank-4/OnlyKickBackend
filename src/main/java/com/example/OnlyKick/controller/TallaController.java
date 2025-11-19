package com.example.OnlyKick.controller;

import com.example.OnlyKick.model.Talla;
import com.example.OnlyKick.service.TallaService;
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
@RequestMapping("/api/tallas")
public class TallaController {

    @Autowired
    private TallaService tallaService;

    @GetMapping
    public ResponseEntity<List<Talla>> getAll() {
        List<Talla> list = tallaService.findAll();
        if (list.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Talla> getById(@PathVariable Integer id) {
        Talla obj = tallaService.findById(id);
        if (obj == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(obj);
    }

    @PostMapping
    public ResponseEntity<Talla> create(@RequestBody Talla talla) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tallaService.save(talla));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Talla> update(@PathVariable Integer id, @RequestBody Talla talla) {
        talla.setIdTalla(id);
        return ResponseEntity.ok(tallaService.save(talla));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Talla> partialUpdate(@PathVariable Integer id, @RequestBody Talla talla) {
        Talla existing = tallaService.findById(id);
        if (existing == null) return ResponseEntity.notFound().build();

        if (talla.getValorTalla() != null) {
            existing.setValorTalla(talla.getValorTalla());
        }
        return ResponseEntity.ok(tallaService.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        tallaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}