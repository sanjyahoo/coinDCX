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
    public By otherTitle = By.cssSelector("p.other-link__category-title");

    public void verifyLaunchPageVisibility(){
        ActionHelper.sleepForSeconds(20);
        ActionHelper.switchContext("WEBVIEW_com.coindcx");
        ActionHelper.isElementPresentWithWait(getStartedBtn);
        ActionHelper.isElementPresent(navBtns);
    }

    public void clickOnProfileBtn(){
        ActionHelper.clickFromList(navBtns,"3");
        ActionHelper.isElementPresentWithShortWait(otherTitle);
    }
}
