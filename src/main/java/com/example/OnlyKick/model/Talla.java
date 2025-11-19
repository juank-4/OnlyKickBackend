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
@Table(name = "Talla")
public class Talla {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_talla")
    private Integer idTalla;

    @Column(name = "valor_talla")
    private String valorTalla;

    //Relaciones
    @OneToMany(mappedBy = "talla", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Inventario> inventario;

    @OneToMany(mappedBy = "talla", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<ProductosVenta> productosVenta;
}