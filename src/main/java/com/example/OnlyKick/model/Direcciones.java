package com.example.OnlyKick.model;

import java.util.Set;

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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Direcciones")
public class Direcciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direccion")
    private Integer idDireccion;

    @Column
    private String calle;

    @Column
    private String numero;

    @Column(name = "info_adicional")
    private String infoAdicional;

    //Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    @ToString.Exclude 
    @EqualsAndHashCode.Exclude 
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_comuna")
    @ToString.Exclude 
    @EqualsAndHashCode.Exclude
    private Comuna comuna;

    @OneToMany(mappedBy = "direccion", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Venta> ventas;
}