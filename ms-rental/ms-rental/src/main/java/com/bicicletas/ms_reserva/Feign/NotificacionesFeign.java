package com.bicicletas.ms_reserva.Feign;

import com.bicicletas.ms_reserva.Dto.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-notificacion")
public interface NotificacionesFeign {

    @PostMapping("/api/notificacion/enviar")
    void enviar(
            @RequestBody NotificationRequest request);
}
