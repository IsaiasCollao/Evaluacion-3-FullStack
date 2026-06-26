package com.example.bicicleta.ms_notificacion.Service;

import com.example.bicicleta.ms_notificacion.DTO.NotificacionRequest;
import org.springframework.stereotype.Service;

@Service
public class NotificacionService {

    public void enviar(
            NotificacionRequest request){

        System.out.println();
        System.out.println("==================================");
        System.out.println("NOTIFICACION GENERADA");
        System.out.println("Usuario : " + request.getUsuarioId());
        System.out.println("Tipo    : " + request.getTipo());
        System.out.println("Asunto  : " + request.getAsunto());
        System.out.println("Mensaje : " + request.getMensaje());
        System.out.println("==================================");
        System.out.println();
    }
}
