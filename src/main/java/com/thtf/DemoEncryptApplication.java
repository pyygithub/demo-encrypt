package com.thtf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@SpringBootApplication
@RestController
public class DemoEncryptApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoEncryptApplication.class, args);
	}


	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/test")
	public JdbcTemplate getDatasource() {
		return jdbcTemplate;
	}
}
