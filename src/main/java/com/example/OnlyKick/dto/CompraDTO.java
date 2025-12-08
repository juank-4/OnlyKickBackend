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
    
    private String estado;
    private Integer idEstado; 
    
    private String metodoPago;
    private String direccion;
    
    private String clienteNombre; 
    private String clienteEmail; 

    private List<DetalleCompraDTO> items; 
}