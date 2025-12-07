package com.example.OnlyKick.dto;

import java.util.List;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Integer idUsuario;
    private String nombre;
    private String email;
    private String rol;
    
    // Lista de compras simplificada
    private List<CompraDTO> historialCompras;
}