package com.example.OnlyKick.controller;

import com.example.OnlyKick.model.Region;
import com.example.OnlyKick.service.RegionService;
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
@RequestMapping("/api/regiones")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @GetMapping
    public ResponseEntity<List<Region>> getAll() {
        List<Region> list = regionService.findAll();
        if (list.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Region> getById(@PathVariable Integer id) {
        Region obj = regionService.findById(id);
        if (obj == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(obj);
    }

    @PostMapping
    public ResponseEntity<Region> create(@RequestBody Region region) {
        return ResponseEntity.status(HttpStatus.CREATED).body(regionService.save(region));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Region> update(@PathVariable Integer id, @RequestBody Region region) {
        region.setIdRegion(id);
        return ResponseEntity.ok(regionService.save(region));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Region> partialUpdate(@PathVariable Integer id, @RequestBody Region region) {
        Region existing = regionService.findById(id);
        if (existing == null) return ResponseEntity.notFound().build();

        if (region.getNombreRegion() != null) {
            existing.setNombreRegion(region.getNombreRegion());
        }
        return ResponseEntity.ok(regionService.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        regionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}