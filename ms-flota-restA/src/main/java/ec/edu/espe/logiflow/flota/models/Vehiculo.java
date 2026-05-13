package ec.edu.espe.logiflow.flota.models;

import ec.edu.espe.logiflow.flota.enums.EstadoVehiculo;
import ec.edu.espe.logiflow.flota.enums.TipoVehiculo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vehiculos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vehiculo {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 20)
    private String matricula;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoVehiculo tipo;

    @Column(name = "capacidad_kg", nullable = false)
    private Double capacidadKg;

    @Column(name = "autonomia_km", nullable = false)
    private Double autonomiaKm;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private EstadoVehiculo estado = EstadoVehiculo.DISPONIBLE;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        this.createdAt = LocalDateTime.now();
        this.matricula = this.matricula.toUpperCase();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.matricula = this.matricula.toUpperCase();
    }
}
