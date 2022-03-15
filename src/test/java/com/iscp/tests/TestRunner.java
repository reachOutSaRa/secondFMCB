package com.iscp.tests;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/java/resources/features",
        glue = "com/iscp/stepDefinitions",
        plugin = {"json:target/allure-results/cucumber.xml"}
)
public class TestRunner extends AbstractTestNGCucumberTests {
}
