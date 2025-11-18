package com.example.OnlyKick.service;

import com.example.OnlyKick.model.Inventario;
import com.example.OnlyKick.model.Producto;
import com.example.OnlyKick.model.ProductosVenta;
import com.example.OnlyKick.model.Venta;
import com.example.OnlyKick.repository.InventarioRepository;
import com.example.OnlyKick.repository.ProductoRepository;
import com.example.OnlyKick.repository.ProductosVentaRepository;
import com.example.OnlyKick.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@SuppressWarnings("null")
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductosVentaRepository productosVentaRepository;
    
    @Autowired
    private InventarioRepository inventarioRepository;
    
    @Autowired
    private ProductoRepository productoRepository;

    public List<Venta> findAll() {
        return ventaRepository.findAll();
    }

    public Venta findById(Integer id) {
        return ventaRepository.findById(id).orElse(null);
    }

    public List<Venta> findByUsuarioId(Integer idUsuario) {
        return ventaRepository.findByUsuarioIdUsuario(idUsuario);
    }

    public Venta update(Venta venta) {
        return ventaRepository.save(venta);
    }

    public Venta partialUpdate(Integer id, Venta ventaDetails) {
        Venta existing = findById(id);
        if (existing != null) {
            if (ventaDetails.getEstadoVenta() != null) {
                existing.setEstadoVenta(ventaDetails.getEstadoVenta());
            }
            if (ventaDetails.getDireccion() != null) {
                existing.setDireccion(ventaDetails.getDireccion());
            }
            if (ventaDetails.getMetodoPago() != null) {
                existing.setMetodoPago(ventaDetails.getMetodoPago());
            }
            if (ventaDetails.getMetodoEnvio() != null) {
                existing.setMetodoEnvio(ventaDetails.getMetodoEnvio());
            }
            return ventaRepository.save(existing);
        }
        return null;
    }

    public Venta procesarVenta(Venta venta, Set<ProductosVenta> productos) throws Exception {
        BigDecimal totalCalculado = BigDecimal.ZERO;

        for (ProductosVenta pv : productos) {
            Optional<Inventario> stockOpt = inventarioRepository.findByProductoIdProductoAndTallaIdTallaAndColorIdColor(
                pv.getProducto().getId_producto(),
                pv.getTalla().getId_talla(),
                pv.getColor().getId_color()
            );

            if (!stockOpt.isPresent() || stockOpt.get().getStock() < pv.getCantidad()) {
                throw new Exception("No hay stock suficiente para el producto: " + pv.getProducto().getNombreProducto());
            }

            Producto p = productoRepository.findById(pv.getProducto().getId_producto()).orElseThrow();
            pv.setPrecioUnitario(p.getPrecioBase()); 
            
            totalCalculado = totalCalculado.add(
                p.getPrecioBase().multiply(new BigDecimal(pv.getCantidad()))
            );
        }

        venta.setTotalVenta(totalCalculado);
        Venta ventaGuardada = ventaRepository.save(venta);

        for (ProductosVenta pv : productos) {
            pv.setVenta(ventaGuardada);
            productosVentaRepository.save(pv);

            Inventario inventario = inventarioRepository.findByProductoIdProductoAndTallaIdTallaAndColorIdColor(
                pv.getProducto().getId_producto(),
                pv.getTalla().getId_talla(),
                pv.getColor().getId_color()
            ).get(); 

            inventario.setStock(inventario.getStock() - pv.getCantidad());
            inventarioRepository.save(inventario);
        }
        
        return ventaGuardada;
    }

    public void deleteById(Integer id) {
        List<ProductosVenta> productosVenta = productosVentaRepository.findByVentaIdVenta(id);
        if (productosVenta != null && !productosVenta.isEmpty()) {
            productosVentaRepository.deleteAll(productosVenta);
        }
        ventaRepository.deleteById(id);
    }
}