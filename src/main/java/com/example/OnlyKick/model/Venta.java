package com.example.OnlyKick.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Venta")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Integer idVenta;

    @Column(name = "fecha_venta", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaVenta = LocalDateTime.now();

    @Column(name = "total_venta")
    private BigDecimal totalVenta;

    //Relaciones
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_direccion")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Direcciones direccion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_estado")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private EstadoVenta estadoVenta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_metodo_pago")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private MetodoPago metodoPago;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_metodo_envio")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private MetodoEnvio metodoEnvio;

    @OneToMany(mappedBy = "venta", fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<ProductosVenta> productosVenta;
}