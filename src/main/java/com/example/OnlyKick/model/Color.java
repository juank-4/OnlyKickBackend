package com.example.OnlyKick.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Color")
public class Color {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_color")
    private Integer idColor;

    @Column(name = "nombre_color")
    private String nombreColor;

    @Column(name = "hex_color")
    private String hexColor;

    //Relaciones
    @OneToMany(mappedBy = "color", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Inventario> inventario;

    @OneToMany(mappedBy = "color", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<ProductosVenta> productosVenta;
}