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
@Table(name = "Estado_Venta")
public class EstadoVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private Integer idEstado;

    @Column(name = "nombre_estado")
    private String nombreEstado;

    //Relaciones
    @OneToMany(mappedBy = "estadoVenta", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Venta> ventas;
}