package com.example.oopsmajorassignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class OopsmajorassignmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(OopsmajorassignmentApplication.class, args);
		RestTemplate restTemplate = new RestTemplate();

		
		String url = "http://localhost:8080/";

		// Make the GET request to retrieve all posts
		String response = restTemplate.getForObject(url, String.class);

		// Print the response
		System.out.println("Response from server:");
		System.out.println(response);



	}

}
