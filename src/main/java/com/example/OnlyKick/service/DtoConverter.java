package com.example.OnlyKick.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.OnlyKick.dto.CompraDTO;
import com.example.OnlyKick.dto.DetalleCompraDTO;
import com.example.OnlyKick.dto.UsuarioDTO;
import com.example.OnlyKick.model.ProductosVenta;
import com.example.OnlyKick.model.Usuario;
import com.example.OnlyKick.model.Venta;

@Component
public class DtoConverter {

    public UsuarioDTO convertToDto(Usuario usuario) {
        if (usuario == null) return null;

        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombreUsuario());
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getRol());

        // Convertir el historial de ventas si existe
        if (usuario.getVentas() != null) {
            List<CompraDTO> compras = usuario.getVentas().stream()
                .map(this::convertVentaToDto)
                .collect(Collectors.toList());
            dto.setHistorialCompras(compras);
        } else {
            dto.setHistorialCompras(new ArrayList<>());
        }

        return dto;
    }

    public CompraDTO convertVentaToDto(Venta venta) {
        CompraDTO dto = new CompraDTO();
        dto.setIdVenta(venta.getIdVenta());
        dto.setFecha(venta.getFechaVenta());
        dto.setTotal(venta.getTotalVenta());
        
        if (venta.getEstadoVenta() != null) {
            dto.setEstado(venta.getEstadoVenta().getNombreEstado());
            dto.setIdEstado(venta.getEstadoVenta().getIdEstado()); // <--- Mapeamos el ID
        }
            
        if (venta.getMetodoPago() != null)
            dto.setMetodoPago(venta.getMetodoPago().getNombreMetodo());

        // Mapear Cliente (Si existe)
        if (venta.getUsuario() != null) {
            dto.setClienteNombre(venta.getUsuario().getNombreUsuario());
            dto.setClienteEmail(venta.getUsuario().getEmail());
        } else {
            dto.setClienteNombre("Usuario Eliminado");
        }

        // Dirección (Código existente...)
        if (venta.getDireccion() != null) {
            String dirStr = venta.getDireccion().getCalle() + " " + venta.getDireccion().getNumero();
            if (venta.getDireccion().getComuna() != null) {
                dirStr += ", " + venta.getDireccion().getComuna().getNombreComuna();
            }
            dto.setDireccion(dirStr);
        }

        // Convertir los productos (Código existente...)
        if (venta.getProductosVenta() != null) {
            List<DetalleCompraDTO> items = venta.getProductosVenta().stream()
                .map(this::convertDetalleToDto)
                .collect(Collectors.toList());
            dto.setItems(items);
        }

        return dto;
    }

    private DetalleCompraDTO convertDetalleToDto(ProductosVenta pv) {
        DetalleCompraDTO dto = new DetalleCompraDTO();
        dto.setCantidad(pv.getCantidad());
        dto.setPrecioUnitario(pv.getPrecioUnitario());
        
        // Calcular subtotal (Precio * Cantidad)
        if (pv.getPrecioUnitario() != null) {
            dto.setSubtotal(pv.getPrecioUnitario().multiply(new BigDecimal(pv.getCantidad())));
        }

        if (pv.getProducto() != null) {
            dto.setNombreProducto(pv.getProducto().getNombreProducto());
            
            if (pv.getProducto().getMarca() != null)
                dto.setMarca(pv.getProducto().getMarca().getNombreMarca());

            // Intentar obtener la primera imagen si existe
            if (pv.getProducto().getImagenes() != null && !pv.getProducto().getImagenes().isEmpty()) {
                dto.setUrlImagen(pv.getProducto().getImagenes().iterator().next().getUrlImagen());
            }
        }
        
        if (pv.getTalla() != null) dto.setTalla(pv.getTalla().getValorTalla());
        if (pv.getColor() != null) dto.setColor(pv.getColor().getNombreColor());

        return dto;
    }
}