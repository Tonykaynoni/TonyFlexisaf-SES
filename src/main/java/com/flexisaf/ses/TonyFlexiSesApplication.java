package com.flexisaf.ses;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableSwagger2
public class TonyFlexiSesApplication {

	public static void main(String[] args) {
		SpringApplication.run(TonyFlexiSesApplication.class, args);
	}
	
	   @Bean
	    public Docket api() {
	        return new Docket(DocumentationType.SWAGGER_2)
	                .apiInfo(apiInfo())
	                .enable(true)
	                .select()
	                .apis(RequestHandlerSelectors.any())
	                .paths(PathSelectors.any())
	                .build();
	    }
	   
	   private ApiInfo apiInfo(){
	        return new ApiInfoBuilder().title("Flexisaf SES TASK")
	                                    .description("Developed By Tonykay")
	                                    .version("1.0").build();
	    }

}
