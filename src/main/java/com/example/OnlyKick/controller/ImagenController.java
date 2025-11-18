package com.example.OnlyKick.controller;

import com.example.OnlyKick.model.Imagen;
import com.example.OnlyKick.service.ImagenService;
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
@RequestMapping("/api/imagenes")
public class ImagenController {

    @Autowired
    private ImagenService imagenService;

    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<List<Imagen>> getByProductoId(@PathVariable Integer idProducto) {
        List<Imagen> list = imagenService.findByProductoId(idProducto);
        if (list.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Imagen> getById(@PathVariable Integer id) {
        Imagen obj = imagenService.findById(id);
        if (obj == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(obj);
    }

    @PostMapping
    public ResponseEntity<Imagen> create(@RequestBody Imagen imagen) {
        return ResponseEntity.status(HttpStatus.CREATED).body(imagenService.save(imagen));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Imagen> update(@PathVariable Integer id, @RequestBody Imagen imagen) {
        imagen.setId_imagen(id);
        return ResponseEntity.ok(imagenService.save(imagen));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Imagen> partialUpdate(@PathVariable Integer id, @RequestBody Imagen imagen) {
        Imagen existing = imagenService.findById(id);
        if (existing == null) return ResponseEntity.notFound().build();

        if (imagen.getUrlImagen() != null) {
            existing.setUrlImagen(imagen.getUrlImagen());
        }
        if (imagen.getAltText() != null) {
            existing.setAltText(imagen.getAltText());
        }
        return ResponseEntity.ok(imagenService.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        imagenService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}