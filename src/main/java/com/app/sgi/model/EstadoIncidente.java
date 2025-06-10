package com.app.sgi.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "estado_incidente")
@Getter 
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class EstadoIncidente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private Integer idEstado;

    private String nombre;
}