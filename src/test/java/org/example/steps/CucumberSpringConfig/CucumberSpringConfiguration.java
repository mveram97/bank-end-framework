package org.example.steps.CucumberSpringConfig;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.example.api.Application;

import static org.junit.Assert.assertEquals;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,classes = Application.class) // Aquí puedes especificar la clase de configuración si es necesario
public class CucumberSpringConfiguration {
}

