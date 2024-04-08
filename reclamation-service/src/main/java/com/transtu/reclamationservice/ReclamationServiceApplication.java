package com.transtu.reclamationservice;

import com.transtu.reclamationservice.clients.UserRestClient;
import com.transtu.reclamationservice.entities.Reclamation;
import com.transtu.reclamationservice.repository.ReclamationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootApplication
@EnableFeignClients
public class ReclamationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReclamationServiceApplication.class, args);
	}


	/*@Bean
	CommandLineRunner commandLineRunner(ReclamationRepository reclamationRepository, UserRestClient userRestClient){
		return args -> {
			userRestClient.allUsers().forEach(u->{
				Reclamation reclamation1 = Reclamation.builder()
						.id(1L)
						.createAt(LocalDate.now())
						.description("reclamation1_description")
						.userId(u.getId())
						.build();
				Reclamation reclamtion2 = Reclamation.builder()
						.id(2L)
						.createAt(LocalDate.now())
						.description("reclamation2_description")
						.userId(u.getId())
						.build();
				reclamationRepository.save(reclamation1);
				reclamationRepository.save(reclamtion2);
			});


		};
	}*/
}
