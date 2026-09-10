package com.encurtaurl.qrcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"com.encurtaurl.qrcode",
		"com.nucleo.filter"
})
public class QRCodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(QRCodeApplication.class, args);
	}

}
