package ec.edu.espe.logiflow.taller.restdos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdenMantenimientoRequest {

    @NotBlank(message = "La matrícula es obligatoria")
    @Pattern(regexp = "^[A-Z]{3}-[0-9]{4}$", message = "La matrícula debe tener el formato AAA-####")
    private String matricula;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 10, max = 200, message = "La descripción debe tener entre 10 y 200 caracteres")
    @Pattern(
            regexp = "^(?!.*([a-záéíóúñ0-9\\s])\\1{2})(?!.*[.,\\-]{2})(?!.*\\b(\\w+)\\s+\\2\\b)[A-ZÁÉÍÓÚÑ][a-záéíóúñ0-9\\s.,\\-]+$",
            message = "La descripción debe iniciar con mayúscula y continuar solo con minúsculas. No se permiten signos de puntuación juntos (ej. .. o ,,), letras/espacios repetidos 3 veces, ni palabras repetidas consecutivamente."
    )
    private String descripcion;
}