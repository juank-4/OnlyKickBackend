package com.example.OnlyKick.model;

import java.util.Set;

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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    @ToString.Exclude           // <--- FALTABA ESTO
    @EqualsAndHashCode.Exclude  // <--- FALTABA ESTO
    private Set<Inventario> inventario;

    @OneToMany(mappedBy = "talla", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude           
    @EqualsAndHashCode.Exclude  
    private Set<ProductosVenta> productosVenta;
}