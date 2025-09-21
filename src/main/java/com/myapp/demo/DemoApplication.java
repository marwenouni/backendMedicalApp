package com.myapp.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.myapp.demo.service.FileStorageService;

import jakarta.annotation.Resource;




@SpringBootApplication
public class DemoApplication {

	@Resource
	  FileStorageService storageService;
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
//	@Override
//	  public void run(String... arg) throws Exception {
////	    storageService.deleteAll();
//	    storageService.init();
//	  }
	
//	@Bean
//	public CommandLineRunner demo(UserRepository repo) {
//		return(args) -> {
//			//repo.save(new User());
//			//Log.info("user created");
//		};
//	}

}