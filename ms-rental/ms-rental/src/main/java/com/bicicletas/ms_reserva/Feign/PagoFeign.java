package com.bicicletas.ms_reserva.Feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.bicicletas.ms_reserva.Dto.CalculoRequest;

@FeignClient(name = "ms-pago")
public interface PagoFeign {


    @PostMapping("/api/pagos/calcular")
    CalculoRequest calcularPago(
            @RequestBody CalculoRequest request);


    
}
