package com.app.sgi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "incidente")
@Getter 
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class Incidente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_incidente")
    private Integer idIncidente;

    @NotBlank(message = "El titulo es obligatorio")
    private String titulo;
    
    @NotBlank(message = "La descripción es obligatorio")
    private String descripcion;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @NotBlank(message = "Debe seleccionar una prioridad")
    @Column(name = "prioridad")
    private String prioridad;

    @ManyToOne
    @JoinColumn(name = "id_estado")
    private EstadoIncidente estado;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "id_tecnico")
    private Usuario tecnico;
    
    @Column(name = "solucion")
    private String solucion;

    @Column(name = "fecha_solucion")
    private LocalDateTime fechaSolucion;
}