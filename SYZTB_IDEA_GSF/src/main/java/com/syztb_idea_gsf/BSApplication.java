package com.syztb_idea_gsf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.syztb_idea_gsf.mapper")
@SpringBootApplication
public class BSApplication {

	public static void main(String[] args) {
		SpringApplication.run(BSApplication.class, args);
	}

}
