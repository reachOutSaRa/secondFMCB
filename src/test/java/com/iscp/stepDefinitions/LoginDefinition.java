package com.iscp.stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.testng.Assert;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

public class LoginDefinition {
    @Given("User is on Home page")
    public void userOnHomePage() {
        CommonDefinitions.driver.get("https://opensource-demo.orangehrmlive.com/");
    }
    @When("User enters username as {string}")
    public void entersUsername(String userName) throws InterruptedException {
        System.out.println("Username Entered");
        CommonDefinitions.driver.findElement(By.name("txtUsername")).sendKeys(userName);
    }
    @When("User enters password as {string}")
    public void entersPassword(String passWord) throws InterruptedException {
        System.out.println("Password Entered");
        CommonDefinitions.driver.findElement(By.name("txtPassword")).sendKeys(passWord);
        CommonDefinitions.driver.findElement(By.id("btnLogin")).submit();
    }
    @Then("User should be able to login sucessfully")
    public void sucessfulLogin() throws InterruptedException {
        String newPageText = CommonDefinitions.driver.findElement(By.id("welcome")).getText();
        System.out.println("newPageText :" + newPageText);
        assertThat(newPageText, containsString("Welcome"));
    }
    @Then("Error message {string} should be displayed")
    public void unsucessfulLogin(String message) throws InterruptedException {
        String errorMessage = CommonDefinitions.driver.findElement(By.id("spanMessage")).getText();
        System.out.println("Error Message :" + errorMessage);
        Assert.assertEquals(errorMessage, message);
    }
}
