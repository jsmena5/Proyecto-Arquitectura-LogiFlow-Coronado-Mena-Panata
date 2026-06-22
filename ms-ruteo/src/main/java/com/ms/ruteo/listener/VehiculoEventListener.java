package com.ms.ruteo.listener;

import com.ms.ruteo.config.RabbitMQConfig;
import com.ms.ruteo.event.VehiculoDisponibleEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VehiculoEventListener {

    @RabbitListener(queues = RabbitMQConfig.VEHICULO_DISPONIBLE_QUEUE)
    public void recibirVehiculoDisponible(VehiculoDisponibleEvent event) {
        System.out.println("Vehículo disponible recibido en ms-ruteo: " + event.getVehiculoId());
        System.out.println("Placa: " + event.getPlaca());
        System.out.println("Ubicación actual: " + event.getUbicacionActual());
    }
}