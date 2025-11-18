package com.example.OnlyKick.controller;

import com.example.OnlyKick.model.MetodoPago;
import com.example.OnlyKick.service.MetodoPagoService;
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
@RequestMapping("/api/metodos-pago")
public class MetodoPagoController {

    @Autowired
    private MetodoPagoService metodoPagoService;

    @GetMapping
    public ResponseEntity<List<MetodoPago>> getAll() {
        List<MetodoPago> list = metodoPagoService.findAll();
        if (list.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetodoPago> getById(@PathVariable Integer id) {
        MetodoPago obj = metodoPagoService.findById(id);
        if (obj == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(obj);
    }

    @PostMapping
    public ResponseEntity<MetodoPago> create(@RequestBody MetodoPago metodo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(metodoPagoService.save(metodo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MetodoPago> update(@PathVariable Integer id, @RequestBody MetodoPago metodo) {
        metodo.setId_metodo_pago(id);
        return ResponseEntity.ok(metodoPagoService.save(metodo));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MetodoPago> partialUpdate(@PathVariable Integer id, @RequestBody MetodoPago metodo) {
        MetodoPago existing = metodoPagoService.findById(id);
        if (existing == null) return ResponseEntity.notFound().build();
        if (metodo.getNombreMetodo() != null) existing.setNombreMetodo(metodo.getNombreMetodo());
        return ResponseEntity.ok(metodoPagoService.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        metodoPagoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}