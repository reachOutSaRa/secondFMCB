package com.iscp.tests;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

import org.testng.annotations.Test;

@Test
@CucumberOptions(plugin = {"json:target/allure-results/cucumber.xml"},
        tags = "",
        features = "src/test/java/resources/features/Login.feature",
        glue = "com/iscp/stepDefinitions")
public class CucumberRunnerTests extends AbstractTestNGCucumberTests {
}
