package com.ms.ruteo.listener;

import com.ms.ruteo.config.RabbitMQConfig;
import com.ms.ruteo.event.PedidoCreadoEvent;
import com.ms.ruteo.service.RuteoService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PedidoEventListener {

    private final RuteoService ruteoService;

    @RabbitListener(queues = RabbitMQConfig.PEDIDO_CREADO_QUEUE)
    public void recibirPedidoCreado(PedidoCreadoEvent event) {
        ruteoService.asignarRutaDesdePedidoCreado(event);
        System.out.println("Evento PedidoCreado recibido en ms-ruteo. Pedido ID: " + event.getPedidoId());
    }
}