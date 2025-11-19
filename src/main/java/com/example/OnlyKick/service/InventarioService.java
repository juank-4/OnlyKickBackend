package com.example.OnlyKick.service;

import com.example.OnlyKick.model.Inventario;
import com.example.OnlyKick.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@SuppressWarnings("null")
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    //Metodo para buscar el stock de una variante especifica de un producto
    public Optional<Inventario> findStockByVariante(Integer idProducto, Integer idTalla, Integer idColor) {
        return inventarioRepository.findByProductoIdProductoAndTallaIdTallaAndColorIdColor(idProducto, idTalla, idColor);
    }

    //Metodo para buscar todo el inventario de un producto
    public List<Inventario> findByProductoId(Integer idProducto) {
        return inventarioRepository.findByProductoIdProducto(idProducto);
    }

    //Metodo para guardar o actualizar una entrada de inventario
    public Inventario save(Inventario inventario) {
        //Busca si ya existe la variante
        Optional<Inventario> existenteOpt = findStockByVariante(
            inventario.getProducto().getIdProducto(),
            inventario.getTalla().getIdTalla(),
            inventario.getColor().getIdColor()
        );

        //Si existe, solo actualizamos el stock de esa entrada
        if (existenteOpt.isPresent()) {
            Inventario existente = existenteOpt.get();
            existente.setStock(inventario.getStock());
            return inventarioRepository.save(existente);
        }
        
        // Si no existe, la creamos, asegurandose de que el ID sea null para evitar conflictos
        inventario.setId_inventario(null);
        return inventarioRepository.save(inventario);
    }

    public Inventario findById(Integer id) {
        return inventarioRepository.findById(id).orElse(null);
    }

    public void deleteById(Integer id) {
        inventarioRepository.deleteById(id);
    }
}