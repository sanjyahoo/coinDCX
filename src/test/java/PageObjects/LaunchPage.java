package PageObjects;

import Utils.ActionHelper;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;

public class LaunchPage {

    public LaunchPage(AppiumDriver<MobileElement> tlDriver){

    }
    public By getStartedBtn = By.xpath("//a[@class='cta get-started-btn']");
    public By navBtns = By.cssSelector("a.nav__item");

    public void verifyLaunchPageVisibility(){
        ActionHelper.isElementPresentWithWait(getStartedBtn);
        ActionHelper.isElementPresent(navBtns);
    }

    public void clickOnProfileBtn(){
        ActionHelper.clickFromList(navBtns,"3");
    }
}
