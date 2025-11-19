package com.example.OnlyKick.controller;

import com.example.OnlyKick.model.Color;
import com.example.OnlyKick.service.ColorService;
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
@RequestMapping("/api/colores")
public class ColorController {

    @Autowired
    private ColorService colorService;

    @GetMapping
    public ResponseEntity<List<Color>> getAll() {
        List<Color> list = colorService.findAll();
        if (list.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Color> getById(@PathVariable Integer id) {
        Color obj = colorService.findById(id);
        if (obj == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(obj);
    }

    @PostMapping
    public ResponseEntity<Color> create(@RequestBody Color color) {
        return ResponseEntity.status(HttpStatus.CREATED).body(colorService.save(color));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Color> update(@PathVariable Integer id, @RequestBody Color color) {
        color.setIdColor(id);
        return ResponseEntity.ok(colorService.save(color));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Color> partialUpdate(@PathVariable Integer id, @RequestBody Color color) {
        Color existing = colorService.findById(id);
        if (existing == null) return ResponseEntity.notFound().build();

        if (color.getNombreColor() != null) {
            existing.setNombreColor(color.getNombreColor());
        }
        if (color.getHexColor() != null) {
            existing.setHexColor(color.getHexColor());
        }
        return ResponseEntity.ok(colorService.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        colorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}