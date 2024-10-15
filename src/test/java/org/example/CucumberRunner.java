package org.example;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.example.api.Application;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


//@CucumberContextConfiguration
//@SpringBootTest(classes = Application.class)
@RunWith(Cucumber.class)
//@ActiveProfiles("test")
@CucumberOptions(
    features = "src/test/resources/features", // Path to the feature files
    glue = "org.example.steps", // Path to step definitions
    plugin = {"pretty", "html:target/cucumber-reports.html"} // Reporting options
    )


public class CucumberRunner {}
