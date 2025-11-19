package com.example.OnlyKick.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto; // CORREGIDO: id_producto -> idProducto

    @Column(name = "nombre_producto")
    private String nombreProducto;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "precio_base")
    private BigDecimal precioBase;

    //Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_marca")
    private Marca marca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_material")
    private Material material;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_genero")
    private Genero genero;

    @OneToMany(mappedBy = "producto", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Imagen> imagenes;

    @OneToMany(mappedBy = "producto", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Inventario> inventario;

    @OneToMany(mappedBy = "producto", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<ProductosVenta> productosVenta;
}