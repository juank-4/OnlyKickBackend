package com.example.OnlyKick.controller;

import com.example.OnlyKick.model.Venta;
import com.example.OnlyKick.service.VentaService;
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
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @GetMapping
    public ResponseEntity<List<Venta>> getAllVentas() {
        List<Venta> ventas = ventaService.findAll();
        if (ventas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Venta>> getVentasByUsuario(@PathVariable Integer idUsuario) {
        List<Venta> ventas = ventaService.findByUsuarioId(idUsuario);
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> getVentaById(@PathVariable Integer id) {
        Venta venta = ventaService.findById(id);
        if (venta == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(venta);
    }

    @PostMapping
    public ResponseEntity<?> crearVenta(@RequestBody Venta venta) {
        try {
            if (venta.getProductosVenta() == null || venta.getProductosVenta().isEmpty()) {
                return ResponseEntity.badRequest().body("El carrito no puede estar vacío");
            }

            Venta nuevaVenta = ventaService.procesarVenta(venta, venta.getProductosVenta());
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaVenta);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al procesar la venta: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Venta> updateVenta(@PathVariable Integer id, @RequestBody Venta venta) {
        venta.setId_venta(id);
        Venta updated = ventaService.update(venta);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Venta> partialUpdateVenta(@PathVariable Integer id, @RequestBody Venta venta) {
        Venta updated = ventaService.partialUpdate(id, venta);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenta(@PathVariable Integer id) {
        ventaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}