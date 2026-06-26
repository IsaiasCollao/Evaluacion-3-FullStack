package com.bicicletas.ms_reserva;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsRentalApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsRentalApplication.class, args);
	}

}
