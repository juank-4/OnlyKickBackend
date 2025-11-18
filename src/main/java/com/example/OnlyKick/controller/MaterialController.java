package com.example.OnlyKick.controller;

import com.example.OnlyKick.model.Material;
import com.example.OnlyKick.service.MaterialService;
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
@RequestMapping("/api/materiales")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @GetMapping
    public ResponseEntity<List<Material>> getAll() {
        List<Material> list = materialService.findAll();
        if (list.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Material> getById(@PathVariable Integer id) {
        Material obj = materialService.findById(id);
        if (obj == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(obj);
    }

    @PostMapping
    public ResponseEntity<Material> create(@RequestBody Material material) {
        return ResponseEntity.status(HttpStatus.CREATED).body(materialService.save(material));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Material> update(@PathVariable Integer id, @RequestBody Material material) {
        material.setId_material(id);
        return ResponseEntity.ok(materialService.save(material));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Material> partialUpdate(@PathVariable Integer id, @RequestBody Material material) {
        Material existing = materialService.findById(id);
        if (existing == null) return ResponseEntity.notFound().build();

        if (material.getNombreMaterial() != null) {
            existing.setNombreMaterial(material.getNombreMaterial());
        }
        return ResponseEntity.ok(materialService.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        materialService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}