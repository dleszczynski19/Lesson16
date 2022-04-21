package com.helpers;

import com.configuration.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public class WebElementHelper extends BasePage {
    private static Logger log = LoggerFactory.getLogger(WebElementHelper.class);
    private Actions actions;

    public WebElementHelper(WebDriver driver) {
        super(driver);
        actions = new Actions(driver);
    }

    public void clickOnElement(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
        } catch (StaleElementReferenceException sere) {
            // simply retry finding the element in the refreshed DOM
            element.click();
        } catch (TimeoutException toe) {
            log.error("Element " + element.toString() + " was not clickable after 10 seconds");
        }
    }

    public void sendKeysToElement(WebElement element, String value) {
        waitForElement(element);
        element.clear();
        element.sendKeys(value);
    }

    public void addKeysToElement(WebElement element, String value) {
        waitForElement(element);
        element.sendKeys(value);
    }

    public void actionOnKeysToElement(WebElement element, Keys keys) {
        waitForElement(element);
        element.clear();
        element.sendKeys(keys);
    }

    public void waitForElement(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException toe) {
            log.error("Element " + element.toString() + " was not visible after 10 seconds");
            assert false;
        }
    }

    public boolean isElementVisible(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void scrollToElement(WebElement element) {
        int limiter = 0;
        while (!isElementVisible(element) && limiter < 10) {
            js.executeScript("window.scrollBy(0,250)", "");
            limiter++;
        }
        log.info("Scrolled to element: " + element);
    }

    public String getElementNameFromList(List<WebElement> list, int index) {
        waitForElement(list.get(0));
        return list.get(index).getText();
    }

    public String getElementNameFromList(List<WebElement> list, String elementName) {
        waitForElement(list.get(0));
        return findElementByName(list, elementName).getText();
    }

    public WebElement findElementByName(List<WebElement> elementList, String element) {
        return elementList.stream()
                .filter(el -> el.getText().equals(element))
                .reduce((f, s) -> s)
                .orElseThrow(() -> new RuntimeException("Can't find option"));
    }

    public Select getSelect(WebElement element) {
        return new Select(element);
    }

    public boolean isPageSwitched(WebElement element) {
        if (isElementVisible(element)) {
            log.info("The Page has been switched");
            return true;
        } else {
            log.error("The page has not been switched");
            return false;
        }
    }

    public void hoverElement(WebElement element) {
        waitForElement(element);
        actions.moveToElement(element).perform();
        log.info("Hover over element: " + element.toString());
    }

    public void dragAndDrop(WebElement elementDrag, WebElement elementDrop) {
        actions.dragAndDrop(elementDrag, elementDrop)
                .perform();
        log.info("Element " + elementDrag.toString() + " dropped to " + elementDrop.toString());
    }

    public Dimension getElementSize(WebElement element) {
        int height = Integer.parseInt(element.getCssValue("height")
                .replace("px", ""));
        int width = Integer.parseInt(element.getCssValue("width")
                .replace("px", ""));
        return new Dimension(width, height);
    }

    public void waitForSite(String siteName) {
        int limiter = 0;
        while (!driver.getTitle().equals(siteName) && limiter < 10) {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
            limiter++;
        }
    }

    public boolean isPageLoaded(String pagePath) {
        waitForElement(getPath());
        if (getPath().getText().equals(pagePath)) {
            log.info("Page: " + pagePath + " is properly loaded");
            return true;
        } else {
            log.error("Page: " + pagePath + " is not properly loaded");
            return false;
        }
    }

    public boolean isProductLoaded(String productName) {
        waitForElement(getPath());
        return getPath().getText().contains(productName);
    }

    public WebElement findElementInAnotherElement(WebElement mainElement, String elementToFind) {
        return mainElement.findElement(By.cssSelector(elementToFind));
    }

    public List<WebElement> findElementsInAnotherElement(WebElement mainElement, String elementToFind) {
        return mainElement.findElements(By.cssSelector(elementToFind));
    }
}