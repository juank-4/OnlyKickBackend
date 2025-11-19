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
@Table(name = "Metodo_Envio")
public class MetodoEnvio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_metodo_envio")
    private Integer idMetodoEnvio;

    @Column(name = "nombre_metodo")
    private String nombreMetodo;

    //Relaciones
    @OneToMany(mappedBy = "metodoEnvio", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Venta> ventas;
}