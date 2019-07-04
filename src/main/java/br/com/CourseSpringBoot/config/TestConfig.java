package br.com.CourseSpringBoot.config;

import br.com.CourseSpringBoot.service.DbService;
import br.com.CourseSpringBoot.service.EmailService;
import br.com.CourseSpringBoot.service.MockEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.text.ParseException;

/**
 * @author fabricio
 */
@Configuration
@Profile("test")
public class TestConfig {

    @Autowired
    private DbService dbService;

    @Bean
    public boolean instantiateDatabase() throws ParseException {


        dbService.instantiateDatabase();

        return true;
    }

    @Bean
    public EmailService emailService(){
        return new MockEmailService();
    }
}
