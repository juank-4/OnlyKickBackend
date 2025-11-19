package com.example.OnlyKick.controller;

import com.example.OnlyKick.model.MetodoEnvio;
import com.example.OnlyKick.service.MetodoEnvioService;
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
@RequestMapping("/api/metodos-envio")
public class MetodoEnvioController {

    @Autowired
    private MetodoEnvioService metodoEnvioService;

    @GetMapping
    public ResponseEntity<List<MetodoEnvio>> getAll() {
        List<MetodoEnvio> list = metodoEnvioService.findAll();
        if (list.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetodoEnvio> getById(@PathVariable Integer id) {
        MetodoEnvio obj = metodoEnvioService.findById(id);
        if (obj == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(obj);
    }

    @PostMapping
    public ResponseEntity<MetodoEnvio> create(@RequestBody MetodoEnvio metodo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(metodoEnvioService.save(metodo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MetodoEnvio> update(@PathVariable Integer id, @RequestBody MetodoEnvio metodo) {
        metodo.setIdMetodoEnvio(id);
        return ResponseEntity.ok(metodoEnvioService.save(metodo));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MetodoEnvio> partialUpdate(@PathVariable Integer id, @RequestBody MetodoEnvio metodo) {
        MetodoEnvio existing = metodoEnvioService.findById(id);
        if (existing == null) return ResponseEntity.notFound().build();
        if (metodo.getNombreMetodo() != null) existing.setNombreMetodo(metodo.getNombreMetodo());
        return ResponseEntity.ok(metodoEnvioService.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        metodoEnvioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}