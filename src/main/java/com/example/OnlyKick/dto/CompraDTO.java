package com.example.OnlyKick.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class CompraDTO {
    private Integer idVenta;
    private LocalDateTime fecha;
    private BigDecimal total;
    private String estado;       // Ej: "Entregado"
    private String metodoPago;   // Ej: "Tarjeta"
    private String direccion;    // Dirección formateada
    
    // Aquí está la magia: usamos el DTO hijo, no la Entidad
    private List<DetalleCompraDTO> items; 
}