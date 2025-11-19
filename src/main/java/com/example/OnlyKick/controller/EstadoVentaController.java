package com.example.OnlyKick.controller;

import com.example.OnlyKick.model.EstadoVenta;
import com.example.OnlyKick.service.EstadoVentaService;
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
@RequestMapping("/api/estados-venta")
public class EstadoVentaController {

    @Autowired
    private EstadoVentaService estadoVentaService;

    @GetMapping
    public ResponseEntity<List<EstadoVenta>> getAll() {
        List<EstadoVenta> list = estadoVentaService.findAll();
        if (list.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoVenta> getById(@PathVariable Integer id) {
        EstadoVenta obj = estadoVentaService.findById(id);
        if (obj == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(obj);
    }

    @PostMapping
    public ResponseEntity<EstadoVenta> create(@RequestBody EstadoVenta estado) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estadoVentaService.save(estado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoVenta> update(@PathVariable Integer id, @RequestBody EstadoVenta estado) {
        estado.setIdEstado(id);
        return ResponseEntity.ok(estadoVentaService.save(estado));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EstadoVenta> partialUpdate(@PathVariable Integer id, @RequestBody EstadoVenta estado) {
        EstadoVenta existing = estadoVentaService.findById(id);
        if (existing == null) return ResponseEntity.notFound().build();
        if (estado.getNombreEstado() != null) existing.setNombreEstado(estado.getNombreEstado());
        return ResponseEntity.ok(estadoVentaService.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        estadoVentaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}