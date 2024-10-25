package com.bgauction.auctionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AuctionserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuctionserviceApplication.class, args);
	}

}
