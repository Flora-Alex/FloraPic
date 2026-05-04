package com.flora.florapicturebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan("com.flora.florapicturebackend.mapper")
@EnableAspectJAutoProxy(exposeProxy = true)

public class FloraPictureBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FloraPictureBackendApplication.class, args);
	}

}
