package com.example.OnlyKick.controller;

import com.example.OnlyKick.model.Direcciones;
import com.example.OnlyKick.service.DireccionesService;
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
@RequestMapping("/api/direcciones")
public class DireccionesController {

    @Autowired
    private DireccionesService direccionesService;

    @GetMapping
    public ResponseEntity<List<Direcciones>> getAll() {
        List<Direcciones> list = direccionesService.findAll();
        if (list.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Direcciones>> getByUsuarioId(@PathVariable Integer idUsuario) {
        List<Direcciones> list = direccionesService.findByUsuarioId(idUsuario);
        if (list.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Direcciones> getById(@PathVariable Integer id) {
        Direcciones obj = direccionesService.findById(id);
        if (obj == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(obj);
    }

    @PostMapping
    public ResponseEntity<Direcciones> create(@RequestBody Direcciones direccion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(direccionesService.save(direccion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Direcciones> update(@PathVariable Integer id, @RequestBody Direcciones direccion) {
        direccion.setIdDireccion(id);
        return ResponseEntity.ok(direccionesService.save(direccion));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Direcciones> partialUpdate(@PathVariable Integer id, @RequestBody Direcciones direccion) {
        Direcciones existing = direccionesService.findById(id);
        if (existing == null) return ResponseEntity.notFound().build();

        if (direccion.getCalle() != null) existing.setCalle(direccion.getCalle());
        if (direccion.getNumero() != null) existing.setNumero(direccion.getNumero());
        if (direccion.getInfoAdicional() != null) existing.setInfoAdicional(direccion.getInfoAdicional());
        if (direccion.getComuna() != null) existing.setComuna(direccion.getComuna());
        
        return ResponseEntity.ok(direccionesService.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        direccionesService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}