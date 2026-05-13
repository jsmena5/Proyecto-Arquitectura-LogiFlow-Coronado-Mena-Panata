package ec.edu.espe.logiflow.flota.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "conductores")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Conductor {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 15)
    private String dni;

    @Column(name = "primer_nombre", nullable = false, length = 50)
    private String primerNombre;

    @Column(name = "apellido", nullable = false, length = 50)
    private String apellido;

    @Column(name = "numero_licencia", nullable = false, unique = true, length = 9)
    private String numeroLicencia;

    @Column(name = "tipo_licencia", nullable = false, length = 1)
    private String tipoLicencia;

    @Builder.Default
    @Column(nullable = false)
    private Boolean activo = true;

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
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}