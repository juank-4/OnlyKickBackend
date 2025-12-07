package com.example.OnlyKick.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DetalleCompraDTO {
    private String nombreProducto;
    private String marca;
    private String talla;
    private String color;
    private String urlImagen; // Para mostrar la foto en el historial
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}