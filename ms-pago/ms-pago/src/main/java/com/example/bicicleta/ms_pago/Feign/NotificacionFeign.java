package com.example.bicicleta.ms_pago.Feign;

import com.example.bicicleta.ms_pago.DTO.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-notificacion")
public interface NotificacionFeign {

    @PostMapping("/api/notificacion/enviar")
    void enviar(
            @RequestBody NotificationRequest request);
}
