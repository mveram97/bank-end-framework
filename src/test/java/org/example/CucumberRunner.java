package org.example;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features", // Path to the feature files
    glue = "org.example.steps", // Path to step definitions
    plugin = {"pretty", "html:target/cucumber-reports.html"} // Reporting options
    )
public class CucumberRunner {}
