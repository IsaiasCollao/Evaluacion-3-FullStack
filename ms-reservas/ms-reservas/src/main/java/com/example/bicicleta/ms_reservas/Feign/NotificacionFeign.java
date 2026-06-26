package com.example.bicicleta.ms_reservas.Feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.bicicleta.ms_reservas.DTO.NotificationRequest;

@FeignClient(name = "ms-notificacion")
public interface NotificacionFeign {

    @PostMapping("/api/notificacion/enviar")
    void enviar(
            @RequestBody NotificationRequest request);
}
