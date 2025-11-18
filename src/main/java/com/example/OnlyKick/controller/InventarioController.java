package com.example.OnlyKick.controller;

import com.example.OnlyKick.model.Inventario;
import com.example.OnlyKick.service.InventarioService;
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
@RequestMapping("/api/inventario")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<List<Inventario>> getByProductoId(@PathVariable Integer idProducto) {
        List<Inventario> list = inventarioService.findByProductoId(idProducto);
        if (list.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventario> getById(@PathVariable Integer id) {
        Inventario obj = inventarioService.findById(id);
        if (obj == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(obj);
    }

    @PostMapping
    public ResponseEntity<Inventario> create(@RequestBody Inventario inventario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioService.save(inventario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventario> update(@PathVariable Integer id, @RequestBody Inventario inventario) {
        inventario.setId_inventario(id);
        return ResponseEntity.ok(inventarioService.save(inventario));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Inventario> partialUpdate(@PathVariable Integer id, @RequestBody Inventario inventario) {
        Inventario existing = inventarioService.findById(id);
        if (existing == null) return ResponseEntity.notFound().build();

        if (inventario.getStock() != null) {
            existing.setStock(inventario.getStock());
        }
        return ResponseEntity.ok(inventarioService.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        inventarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}