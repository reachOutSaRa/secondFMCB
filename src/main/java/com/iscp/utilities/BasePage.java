package com.iscp.utilities;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Base Page
 * @author SaRa
 * @lastModifiedBy SaRa
 */
public class BasePage {

    private static final Logger log = LoggerFactory.getLogger(BasePage.class);
    public static Base64.Encoder encoder = Base64.getEncoder();
    public static WebDriver driver;
    protected static String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
    private static int waitTimeOut = 120;

    public BasePage() {
    }

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }
    /**
     * Wait for element to be clickable
     * @param element
     * @return
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public boolean waitForElementToBeClickable(By element) {

        Boolean flag = false;
        try {
            waitForAjax();
            WebDriverWait wait = new WebDriverWait(driver, waitTimeOut);
            wait.until(ExpectedConditions.elementToBeClickable(element));
            flag = true;
            log.info(element + " is waited till element is clickable");
        } catch (Exception e) {
            log.info(element + " Element is not clickable");
        }
        return flag;
    }

    /**
     * Function to wait till Element found
     * @param ElementTobeFound
     * @return
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public Boolean waitTillElementFound(By ElementTobeFound) {
        Boolean Found = false;
        try {
            waitForAjax();
            waitTillPageLoad();
            WebDriverWait wait = new WebDriverWait(driver, waitTimeOut);
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(ElementTobeFound)));
            Found = true;
            log.info(ElementTobeFound + " Element Found");

        } catch (Exception e) {
            log.error("Unable to find " + ElementTobeFound);
            Assert.fail("Unable to find " + ElementTobeFound);

        }
        return Found;
    }

    /**
     * Get Text of Web Element
     * @param element
     * @return
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public String getTextOfWebElement(By element){

        String Text = null;
        implicitlyWait();
        try {
            waitForAjax();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        waitTillPageLoad();
        if (waitTillElementFound(element)) {
            try {
                Text = driver.findElement(element).getText();
                log.info("Fetched text value from element as " + Text);
            } catch (NoSuchElementException e) {

                e.printStackTrace();
                log.error("Element not found");
                Assert.fail("Element not found");

            } catch (ElementNotVisibleException e) {

                e.printStackTrace();
                log.error("Element not visible");
                Assert.fail("Element not visible");
            } catch (Exception e) {
                log.error("Unable to get text from element");
                Assert.fail("Unable to get text from element");
            }
        }
        return Text;
    }

    /**
     * Click on Web Element
     * @param element
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void clickOnElement(By element) {
        try {
            waitForAjax();
            if (waitForElementToBeClickable(element)) {
                try {
                    driver.findElement(element).click();
                }
                catch (Exception e) {
                    driver.findElement(element);
                }
                log.info("Clicked on " + "'" +element + "'" + " button");

            } else {
                log.error("'" + element + "'" + " : Button is not loaded");
                Assert.fail("'" + element + "'" + " : Button is not loaded");
            }
        } catch (NoSuchElementException e) {

            e.printStackTrace();
            log.error(element + " Button not found");
            Assert.fail(element + " Button not found");

        } catch (ElementNotVisibleException e) {

            e.printStackTrace();
            log.error(element + " : Button not visible");
            Assert.fail(element + " : Button not visible");
        } catch (Exception e) {
            log.error("Unable to click on " + element + " button");
            e.printStackTrace();
            Assert.fail("Unable to click on " + element + " button");
        }
    }

    /**
     * Click on Web Element
     * @param element
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void clickOnElementJS(By element){
        try {
            waitForAjax();
            JavascriptExecutor executor = (JavascriptExecutor)driver;
            executor.executeScript("arguments[0].click();", driver.findElement(element));
            log.info("Clicked on element with JS executing");
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.info("Click on JS element failed");
        }

    }


    /**
     * Function to wait for Ajax call to complete
     * @throws InterruptedException
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void waitForAjax() throws InterruptedException{
        while (true)
        {
            try {
                Boolean ajaxIsComplete = (Boolean) ((JavascriptExecutor) driver)
                        .executeScript("return jQuery.active == 0");
                if (ajaxIsComplete){
                    break;
                }
            }
            catch (Exception e){
                log.info("Ajax not completed");

            }
        }
    }

    public void waitForAngularLoad() {
        while (true){
            try{
                Boolean angularIsComplete = (Boolean) ((JavascriptExecutor) driver)
                        .executeScript("return angular.element(document).injector().get('$http')" +
                                ".pendingRequests.length === 0");
                if(angularIsComplete){
                    break;
                }
            }
            catch (Exception e){
                log.info("Angular not completed");
            }
        }
    }

    /**
     * Enter Text in the Web element
     * @param element
     * @param value
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void enterTextInField(By element, String value) {
        try {
            if (waitTillElementFound(element)) {
                driver.findElement(element).clear();
                driver.findElement(element).sendKeys(value);
                log.info(value + " is entered in " + element);
            } else {
                log.error("'" + element + "'" + " : Field is not enabled");
                Assert.fail("'" + element + "'" + " : Field is not enabled");

            }
        } catch (NoSuchElementException e) {

            e.printStackTrace();
            log.error(element + " Field is not found");
            Assert.fail(element + " Field is not found");

        } catch (ElementNotVisibleException e) {

            e.printStackTrace();
            log.error(element + " : Field is not visible");
            Assert.fail(element + " : Field is not visible");
        } catch (Exception e) {
            log.error(value + " is not able to entered in " + element);
            Assert.fail(value + " is not able to entered in " + element);
        }
    }

    public void enterTextInFrameByTagName(By frame, By element, String value){


        driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
        driver.switchTo().frame(driver.findElement(frame));

        waitTillPageLoad();
        new WebDriverWait(driver, waitTimeOut)
                .until(ExpectedConditions.visibilityOf((driver.findElement(element))));

        WebElement we = driver.findElement(element);
        new Actions(driver).moveToElement(we).click().build().perform();
        new Actions(driver).moveToElement(we).click().sendKeys(value).build().perform();
        driver.switchTo().defaultContent();

    }

    /**
     * Select radiobutton web Element
     * @param radioXPath
     * @author SaRa
     * @lastModifiedBy SaRa */
    public void radiobuttonSelect(By radioXPath) {
        boolean checkstatus = false;
        try {
            checkstatus = driver.findElement(radioXPath).isSelected();
            if (checkstatus == true) {
                log.info("RadioButton is already checked for " + radioXPath);


            } else {
                driver.findElement(radioXPath).click();
                log.info("Selected the Radiobutton for " + radioXPath);
            }
        } catch (Exception e) {
            log.error("Unable to select the Radiobutton");
        }
    }

    /**
     * Function to verify if the button is enabled or disabled
     * @param element
     * @param attributeName
     * @param text
     * @return
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public Boolean verifyIsButtonEnabledOrDisabled(By element, String attributeName, String text) {

        Boolean isEnabled = false;
        try {
            if (waitTillElementFound(element)) {
                wait(2);
                String Value = driver.findElement(element).getAttribute(attributeName);
                if (!Value.contains(text)) {
                    isEnabled = true;
                    log.info(element + " : is Enabled");
                } else {
                    log.info(element + " : is Disabled");
                }
            } else {
                log.error("'" + element + "'" + " : is not loaded");
                Assert.fail("'" + element + "'" + " : is not loaded");
            }
        } catch (NoSuchElementException e) {

            e.printStackTrace();
            log.error(element + " is not found");
            Assert.fail(element + " : is not found");

        } catch (ElementNotVisibleException e) {

            e.printStackTrace();
            log.error(element + " : is not visible");
            Assert.fail(element + " : is not visible");
        } catch (Exception e) {
            log.error("Unable to find " + element);
            Assert.fail("Unable to find " + element);
        }
        return isEnabled;
    }

    /**
     * Function to take screenshot
     * @param Destination
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void takeScreenshotMethod(String Destination) {
        try {
            File f = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(f, new File(Destination));
            log.info(" Screenshot taken sucessfully");

        } catch (Exception e) {
            log.info("Unable to take screenshot");

        }
    }

    /**
     * Function to check if alert is present
     * @return
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public boolean isAlertPresent() {
        boolean foundAlert = false;
        WebDriverWait wait = new WebDriverWait(driver, waitTimeOut);
        try {
            if (wait.until(ExpectedConditions.alertIsPresent()) == null) {
                log.info("alert was not present");
            } else {

                log.info("alert was present");
                foundAlert = true;
            }
        } catch (TimeoutException eTO) {
            foundAlert = false;
        }
        return foundAlert;
    }

    /**
     * Function to wait till the page load
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void waitTillPageLoad() {
        try {
            driver.manage().timeouts().pageLoadTimeout(waitTimeOut, TimeUnit.SECONDS);

            new WebDriverWait(driver,waitTimeOut).until(
                    webDriver -> ((JavascriptExecutor) webDriver)
                            .executeScript("return document.readyState")
                            .equals("complete"));

            log.info("Pageloaded sucessfully");

        } catch (Exception e) {
            log.error("Unable load page in " + waitTimeOut + " Seconds");

        }
    }

    /**
     * Function to wait implicitly
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void implicitlyWait() {
        try {
            driver.manage().timeouts().implicitlyWait(waitTimeOut, TimeUnit.SECONDS);
            log.info("Page implicitly waited with specified time period");

        } catch (Exception e) {
            log.error("Page implicitly waited with specified time period");
        }


    }

    /**
     * Function to clear text field
     * @param element
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void clearTextField(By element) {
        try {
            if (waitTillElementFound(element)) {
                driver.findElement(element).clear();
                log.info(element + " is cleared");
            } else {
                log.error(element + " is cleared");
                Assert.fail(element + " is cleared");
            }
        } catch (Exception e) {
            log.error("Unable to cleared " + element);
            Assert.fail("Unable to cleared " + element);
        }
    }

    /**
     * Function to select element by visible text
     * @param element
     * @param visibleText
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void selectElementByVisibleText(By element, String visibleText) {
        try {
            if (waitTillElementFound(element)) {
                Select selectItem = new Select(driver.findElement(element));
                selectItem.selectByVisibleText(visibleText);
                log.info(visibleText + " is selected in " + element);
            } else {
                log.error("'" + element + "'" + " : is not loaded");
                Assert.fail("'" + element + "'" + " : is not loaded");
            }

        } catch (NoSuchElementException e) {

            e.printStackTrace();
            log.error(element + " elementName is not found");
            Assert.fail(element + " elementName is not found");

        } catch (ElementNotVisibleException e) {

            e.printStackTrace();
            log.error(element + " : elementName is not visible");
            Assert.fail(element + " : elementName is not visible");
        } catch (Exception e) {
            log.error(visibleText + " is not able to selected in " + element);
            Assert.fail(visibleText + " is not able to selected in " + element);
        }
    }

    /**
     * Function to select element by value
     * @param element
     * @param value
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void selectElementByValue(By element, String value) {
        try {
            if (waitTillElementFound(element)) {
                Select selectitem = new Select(driver.findElement(element));
                selectitem.selectByValue(value);
                log.info(value + " is selected in " + element);
            } else {
                log.error("'" + element + "'" + " : is not loaded");
                Assert.fail("'" + element + "'" + " : is not loaded");
            }

        } catch (NoSuchElementException e) {

            e.printStackTrace();
            log.error(element + " elementName is not found");
            Assert.fail(element + " elementName is not found");

        } catch (ElementNotVisibleException e) {

            e.printStackTrace();
            log.error(element + " : elementName is not visible");
            Assert.fail(element + " : elementName is not visible");
        } catch (Exception e) {
            log.error(value + " is not able to selected in " + element);
            Assert.fail(value + " is not able to selected in " + element);
        }

    }

    /**
     * Function to select element by Index
     * @param element
     * @param index
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void selectElementByIndex(By element, int index) {
        try {
            if (waitTillElementFound(element)) {
                Select selectitem = new Select(driver.findElement(element));
                selectitem.selectByIndex(index);
                log.info(index + " is selected in " + element);
            } else {
                log.error("'" + element + "'" + " : is not loaded");
                Assert.fail("'" + element + "'" + " : is not loaded");
            }

        } catch (NoSuchElementException e) {

            e.printStackTrace();
            log.error(element + " elementName is not found");
            Assert.fail(element + " elementName is not found");

        } catch (ElementNotVisibleException e) {

            e.printStackTrace();
            log.error(element + " : elementName is not visible");
            Assert.fail(element + " : elementName is not visible");
        } catch (Exception e) {
            log.error(index + " is not able to selected in " + element);
            Assert.fail(index + " is not able to selected in " + element);
        }

    }

    /**
     * Function to get inner text from element
     * @param element
     * @return
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public String getInnerTextFromElement(By element) {
        String Text = null;
        try {
            Text = driver.findElement(element).getAttribute("innerText");
            log.info("Fetched inneText value from element as :" + Text);
        } catch (NoSuchElementException e) {

            e.printStackTrace();
            log.error("Element not found");
            Assert.fail("Element not found");

        } catch (ElementNotVisibleException e) {

            e.printStackTrace();
            log.error("Element not visible");
            Assert.fail("Element not visible");
        } catch (Exception e) {
            log.error("Unable to get text from element");
            Assert.fail("Unable to get text from element");
        }
        return Text;
    }

    /**
     * Function to wait for element to disappear
     * @param element
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void waitForElementToDisappear(By element) {
        if (isElementDisplayed(element)) {
            log.info("Waited till Element " + element + " is displayed in screen");
            new WebDriverWait(driver, waitTimeOut).until(
                    ExpectedConditions.not(ExpectedConditions.visibilityOf(driver.findElement(element))));
        }
    }


    public String capture(WebDriver driver, String screenshotName) {
        String errflpath = "";
        try {
            String currDateTime = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            //File Dest = new File(System.getProperty("user.dir") + "/Result/" + screenshotName +"_"+ dateName + ".png");
            File Dest = new File(System.getProperty("user.dir") + "/Result/OysterplusAutomationResult_" + dateName + "/" + screenshotName + "_" + currDateTime + ".png");
            File Jenkins_Dest = new File(System.getProperty("user.dir") + "/Result/JenkinsReport/" + screenshotName + "_" + currDateTime + ".png");
            //File Jenkins_Dest = new File(System.getProperty("user.dir") + "/Result/JenkinsReport/" + screenshotName+ ".png");
            errflpath = Dest.getAbsolutePath();
            try {
                FileUtils.copyFile(scrFile, Dest);
                FileUtils.copyFile(scrFile, Jenkins_Dest);
                log.info("Screenshot captured");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            log.error("Unable to capture screenshot " + e);
        }
        return errflpath;
    }

    /**
     * Function to check if alert button is accepted
     * @return
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public boolean checkAlertAccepted() {
        try {
            Alert alert = driver.switchTo().alert();
            alert.accept();
            log.info("Cliked on ok button in Alert");
            return true;

        } catch (Exception e) {

            log.error("Unable to clik on ok button in Alert");
            return false;

        }
    }

    /**
     * Function to get text from error alert box
     * @return
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public String getTextFromErrorAlertBox() {
        String alertText = "";
        try {
            Alert alert = driver.switchTo().alert();
            wait(2);
            alertText = alert.getText();
            log.info("Fetched text from Alert as " + alertText);
        } catch (Exception e) {
            log.error("Unable to fetch text from Alert");
        }
        return alertText;
    }

    /**
     * Function to accept alert box
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void acceptAlertBox() {
        try {
            Alert alert = driver.switchTo().alert();
            wait(2);
            alert.accept();
            log.info("Clicked on OK button in Alert Box");

        } catch (Exception e) {

            log.error("Unable to click on OK button in Alert Box");
        }
    }

    /**
     * Funtion to check if alert is dismissed
     * @return
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public boolean checkAlert_Dismiss() {
        try {
            Alert alert = driver.switchTo().alert();
            alert.dismiss();
            log.info("Cliked on cancel button in Alert");
            return true;

        } catch (Exception e) {

            log.error("Unable to clik on cancel button in Alert");
            return false;

        }
    }

    /**
     * Function to select check box and check is already select or not
     * @param element
     * @param locators
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void clickCheckboxIfNotAlreadySelected(By element, By locators) {
        boolean checkstatus = false;
        try {
            try {
                driver.findElement(element).isSelected();
            } catch (Exception e) {
                checkstatus = driver.findElement(locators).isSelected();
            }
            if (checkstatus == true) {
                log.info("Checkbox is already checked " + element);

            } else {
                try {
                    driver.findElement(element).click();
                } catch (Exception e) {
                    driver.findElement(locators).click();
                }
                log.info("Checked the checkbox for " + element);
            }
        } catch (Exception e) {
            log.error("Unable to check the checkbox " + element);
        }
    }

    /**
     * Function to check if check box is unchecked
     * @param checkbox
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void checkboxUnchecking(By checkbox) {
        boolean checkstatus;
        try {
            checkstatus =  driver.findElement(checkbox).isSelected();
            if (checkstatus == true) {
                driver.findElement(checkbox).click();
                log.info("Checkbox is unchecked for " + checkbox);
            } else {
                log.info("Checkbox is already unchecked for " + checkbox);
            }
        } catch (Exception e) {
            log.error("Unable to select the Radiobutton");
        }
    }

    /**
     * Function to deselect radio button
     * @param radio
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void deselectRadioButton(By radio) {
        boolean checkstatus;
        checkstatus = driver.findElement(radio).isSelected();
        if (checkstatus == true) {
            driver.findElement(radio).click();
            log.info("Radio Button is deselected");
        } else {
            log.info("Radio Button was already Deselected");
        }
    }

    /**
     * Function to drag and dop
     * @param fromWebElement
     * @param toWebElement
     * @throws InterruptedException
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void dragAndDropByClickHoldAndReleaseAction(By fromWebElement,
                                                       By toWebElement) throws InterruptedException {
        Actions builder = new Actions(driver);
        builder.clickAndHold(driver.findElement(fromWebElement)).moveToElement(driver.findElement(toWebElement))
                .perform();
        Thread.sleep(2000);
        builder.release(driver.findElement(toWebElement)).build().perform();
    }

    /**
     * Function to move mouse and click
     * @param hoverToWebElement
     * @param subMenu
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void mouseMoveAndClick(By hoverToWebElement, By subMenu) {
        try {
            if (waitTillElementFound(hoverToWebElement)) {
                Actions builder = new Actions(driver);
                builder.moveToElement(driver.findElement(hoverToWebElement)).perform();
                wait(1);
                builder.moveToElement(driver.findElement(subMenu)).perform();
                //builder.click(subMenu);
                clickOnElement(subMenu);
                wait(5);
            } else {
                log.error("'" + hoverToWebElement + "'" + " : is not loaded");
                Assert.fail("'" + hoverToWebElement + "'" + " :  is not loaded");
            }
        } catch (NoSuchElementException e) {

            e.printStackTrace();
            log.error(hoverToWebElement + " is not found");
            Assert.fail(hoverToWebElement + " is not found");

        } catch (ElementNotVisibleException e) {

            e.printStackTrace();
            log.error(hoverToWebElement + " : is not visible");
            Assert.fail(hoverToWebElement + " : is not visible");
        } catch (Exception e) {
            log.error("Unable to mouse move on " + hoverToWebElement);
            e.printStackTrace();
            Assert.fail("Unable to  mouse move " + hoverToWebElement);
        }

    }

    /**
     * Function to double click web element
     * @param doubleClickWebElement
     * @throws InterruptedException
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void doubleClickWebElement(By doubleClickWebElement)
            throws InterruptedException {
        Actions builder = new Actions(driver);
        builder.doubleClick(driver.findElement(doubleClickWebElement)).perform();
        Thread.sleep(2000);

    }

    /**
     * Function to get tool tip
     * @param toolTipOfWebElement
     * @return
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public String getToolTip(By toolTipOfWebElement)
            throws InterruptedException {
        String tooltip = driver.findElement(toolTipOfWebElement).getAttribute("title");
        System.out.println("Tool text : " + tooltip);
        return tooltip;
    }

    /**
     * Function to click check box from list
     * @param elements
     * @param valueToSelect
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void clickCheckBoxFromList(By elements,
                                      String valueToSelect) {

        List<WebElement> lst = driver.findElements(elements);
        for (int i = 0; i < lst.size(); i++) {
            List<WebElement> dr = lst.get(i).findElements(By.tagName("label"));
            for (WebElement f : dr) {
                System.out.println("value in the list : " + f.getText());
                if (valueToSelect.equals(f.getText())) {
                    f.click();
                    break;
                }
            }
        }
    }

    /**
     * Function to download file
     * @param href
     * @param fileName
     * @throws Exception
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void downloadFile(String href, String fileName)
            throws Exception {
        URL url = null;
        URLConnection con = null;
        int i;
        url = new URL(href);
        con = url.openConnection();
        File file = new File(".//OutputData//" + fileName);
        BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(file));
        while ((i = bis.read()) != -1) {
            bos.write(i);
        }
        bos.flush();
        bis.close();
    }

    /**
     * Function to Navigate to every link in page
     * @throws InterruptedException
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void navigateToEveryLinkInPage() throws InterruptedException {

        List<WebElement> linksize = driver.findElements(By.tagName("a"));
        int linksCount = linksize.size();
        System.out.println("Total no of links Available: " + linksCount);
        String[] links = new String[linksCount];
        System.out.println("List of links Available: ");
        // print all the links from webpage
        for (int i = 0; i < linksCount; i++) {
            links[i] = linksize.get(i).getAttribute("href");
            System.out.println(linksize.get(i).getAttribute("href"));
        }
        // navigate to each Link on the webpage
        for (int i = 0; i < linksCount; i++) {
            driver.navigate().to(links[i]);
            Thread.sleep(3000);
            System.out.println(driver.getTitle());
        }
    }

    /**
     * Function to wait page
     * @param Time
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void wait(int Time) {
        try {
            Thread.sleep(Time * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function to check if element is displayed
     * @param element
     * @return
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public boolean isElementDisplayed(By element) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 1);
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(element)));
            log.info(element + " Element is displayed");
            return driver.findElement(element).isDisplayed();
        } catch (Exception e) {
            log.info(element + " Element not displayed");
            return false;
        }
    }

    /**
     * Function to take screenshot of Web Element
     * @param driver
     * @param element
     * @param Destination
     * @throws Exception
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void takeScreenshotOfWebElement(WebDriver driver, By element, String Destination)
            throws Exception {
        File v = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        BufferedImage bi = ImageIO.read(v);
        org.openqa.selenium.Point p = driver.findElement(element).getLocation();
        int n = driver.findElement(element).getSize().getWidth();
        int m = driver.findElement(element).getSize().getHeight();
        BufferedImage d = bi.getSubimage(p.getX(), p.getY(), n, m);
        ImageIO.write(d, "png", v);

        FileUtils.copyFile(v, new File(Destination));
    }


    /**
     * Function to set window size
     * @param Dimension1
     * @param dimension2
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void setWindowSize(int Dimension1, int dimension2) {
        driver.manage().window().setSize(new Dimension(Dimension1, dimension2));

    }

    /**
     *  Function to press key down
     * @param element
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void pressKeyDown(By element) {
        driver.findElement(element).sendKeys(Keys.DOWN);
    }

    /**
     * Function to press Enter key
     * @param element
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void pressKeyEnter(By element) {
        driver.findElement(element).sendKeys(Keys.ENTER);
    }

    /**
     * Function to press key up
     * @param element
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void pressKeyUp(By element) {
        driver.findElement(element).sendKeys(Keys.UP);
    }

    /**
     * Function to move to tab
     * @param element
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public  void moveToTab(By element) {
        driver.findElement(element).sendKeys(Keys.chord(Keys.ALT, Keys.TAB));
    }

    /**
     * Function to click all links in page
     * @param destinationOfScreenshot
     * @throws Exception
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void clickAllLinksInPage(String destinationOfScreenshot)
            throws Exception {

        List<WebElement> Links = driver.findElements(By.tagName("a"));
        System.out.println("Total number of links :" + Links.size());

        for (int p = 0; p < Links.size(); p++) {
            System.out.println("Elements present the body :"
                    + Links.get(p).getText());
            Links.get(p).click();
            Thread.sleep(3000);
            System.out.println("Url of the page " + p + ")"
                    + driver.getCurrentUrl());
            takeScreenshotMethod(destinationOfScreenshot + p);
            navigate_back();
            Thread.sleep(2000);
        }

    }

    /**
     * Function to perform keyboard events
     * @param webelement
     * @param key
     * @param alphabet
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void keyboardEvents(By webelement, Keys key,
                               String alphabet) {
        driver.findElement(webelement).sendKeys(Keys.chord(key, alphabet));

    }

    /**
     * Function to navigate forward
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void navigate_forward() {
        driver.navigate().forward();
    }

    /**
     * Function to navigate back
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void navigate_back() {
        driver.navigate().back();
    }

    /**
     * Function to refresh
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void refresh() {
        driver.navigate().refresh();
        log.info("Page Refresh initiated");
    }

    /**
     * Function to click multiple elements
     * @param someElement
     * @param someOtherElement
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void clickMultipleElements(By someElement,
                                      By someOtherElement) {
        Actions builder = new Actions(driver);
        builder.keyDown(Keys.CONTROL).click(driver.findElement(someElement))
                .click(driver.findElement(someOtherElement)).keyUp(Keys.CONTROL).build().perform();
    }

    /**
     * Function to scroll To element
     * @param scrollToThisElement
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void scrolltoElement(By scrollToThisElement) {
        Coordinates coordinate = ((Locatable) scrollToThisElement)
                .getCoordinates();
        coordinate.onPage();
        coordinate.inViewPort();
    }

    /**
     * Fucntion to zip folder
     * @param sourceFolderPath
     * @param zipPath
     * @throws Exception
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void zipFolder(final Path sourceFolderPath, Path zipPath) throws Exception {
        final ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath.toFile()));
        Files.walkFileTree(sourceFolderPath, new SimpleFileVisitor<Path>() {
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                zos.putNextEntry(new ZipEntry(sourceFolderPath.relativize(file).toString()));
                Files.copy(file, zos);
                zos.closeEntry();
                return FileVisitResult.CONTINUE;
            }
        });
        zos.close();
    }

    /**
     * Function to copy file
     * @param SrcPath
     * @param DestPath
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void copyFile(String SrcPath, File DestPath) {
        File SourcePath = new File(SrcPath);
        try {
            FileUtils.copyFile(SourcePath, DestPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Function to check is element is present
     * @param ElementXpathLocator
     * @return
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public boolean isElementPresent(By ElementXpathLocator) {
        boolean isElementFound = false;
        try {

            int ElementSize = driver.findElements(ElementXpathLocator).size();
            if (ElementSize > 0) {
                isElementFound = true;
            }
            return isElementFound;
        } catch (Exception e) {
            e.printStackTrace();
            return isElementFound;
        }

    }

    /**
     * Function to wait till text disappears from screen
     * @param elementLocator
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void waitTillTextDisappearedFromScreen(By elementLocator) {
        int waitTime = 1;
        while (waitTime <= waitTimeOut) {
            if (isElementPresent(elementLocator)) {
                wait(waitTime - (waitTime - 1));
            } else {
                log.info("Waited till " + elementLocator + " is disappeared from screen");
                break;
            }
        }
        waitTime++;
    }

    /**
     * Function to check if check box is checked
     * @param checkboxXpath
     * @param elementName
     * @return
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public boolean isChecked(String checkboxXpath, String elementName) {
        boolean checkstatus = false;
        try {
            checkstatus = driver.findElement(By.xpath(checkboxXpath)).isSelected();
            if (checkstatus == true) {
                System.out.println("Checkbox is checked for " + elementName);
                log.info("Checkbox is checked");

            } else {
                log.error("Checkbox is not checked " + elementName);
            }
        } catch (Exception e) {
            log.error("Unable to check the checkbox");
        }
        return checkstatus;
    }

    /**
     * Function to check if radio button is selected
     * @param radioButtonXpath
     * @param elementName
     * @return
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public boolean isRadioButtonSelected(String radioButtonXpath, String elementName) {
        boolean checkstatus = false;
        try {
            checkstatus = driver.findElement(By.xpath(radioButtonXpath)).isSelected();
            if (checkstatus == true) {
                log.info("RadioButton is selected");

            } else {
                log.error("RadioButton is not selected " + elementName);
            }
        } catch (Exception e) {
            log.error("Unable to select the RadioButton");
        }
        return checkstatus;
    }

    /**
     * Function to get current date
     * @param num
     * @return
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public String getCurrentDateWithAdd(int num) {
        final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");


        Date currentDate = new Date();
        // convert date to calendar
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);

        // manipulate date
        //c.add(Calendar.YEAR, 1);
        //c.add(Calendar.MONTH, 1);
        c.add(Calendar.DATE, num); //same with c.add(Calendar.DAY_OF_MONTH, 1);
        //c.add(Calendar.HOUR, 1);
        //c.add(Calendar.MINUTE, 1);
        //c.add(Calendar.SECOND, 1);

        // convert calendar to date
        Date currentDatePlusOne = c.getTime();
        String date = dateFormat.format(currentDatePlusOne);
        return date;
    }

    /**
     * Function to get date
     * @param dateinput
     * @return
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public String getDate(String dateinput) {
        String date = "";
        int num = Integer.parseInt(dateinput.split("\\+")[1]);
        if (dateinput.equalsIgnoreCase("currDate+" + num)) {
            date = getCurrentDateWithAdd(num);
        } else if (dateinput.equalsIgnoreCase("currDatePlusSubstract")) {

        }
        return date;
    }

    /**
     * Function to close driver
     * @param driver
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedOn 20220310
     */
    public void closeAllBrowser(WebDriver driver) {
        try{
            driver.quit();
            log.info("Browser is Closed");
        }
        catch(Exception e)
        {
            log.error("Browser is not Closed");
        }
    }
}
