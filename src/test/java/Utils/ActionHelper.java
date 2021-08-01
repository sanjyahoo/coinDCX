package Utils;

import AppiumSupport.DriverFactory;
import com.google.common.collect.ImmutableMap;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ActionHelper extends DriverFactory {
    public static String signedUpUser = "automation-tests"+"+"+String.valueOf(System.currentTimeMillis())+"1@healthtap.com";

    private static By permissionAllowBtn = MobileBy.id("com.android.packageinstaller:id/permission_allow_button");
    private static By permissionDenyBtn = MobileBy.id("com.android.packageinstaller:id/permission_deny_button");
    private static By permissionDntAskAgainCheckbox = MobileBy.id("com.android.packageinstaller:id/do_not_ask_checkbox");
    private static By androidPermissionDenyBtn = MobileBy.id("com.android.permissioncontroller:id/permission_deny_button");
    private static By androidPermissionAllowAlwaysBtn = MobileBy.id("com.android.permissioncontroller:id/permission_allow_always_button");
    private static By androidPermissionAllowWhileUsingBtn = MobileBy.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button");
    private static By androidPermissionDenyAndDntAskBtn = MobileBy.id("com.android.permissioncontroller:id/permission_deny_and_dont_ask_again_button");
    private static By androidAllowPermissionBtn = MobileBy.id("com.android.permissioncontroller:id/permission_allow_button"); //Android 10
    private static final String CHAR_LIST = "abcdefghijklmnopqrstuvwxyz";

    public static void hideKeyboard() {
        try {
            DriverFactory.getTLDriver().hideKeyboard();
//            DriverFactory.getTLDriver().getKeyboard().sendKeys(Keys.ENTER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideKeyboardByTappingOutside() {
        tapToHideKeyBoard();
    }

    public static void hideKeyboardByScroll() {
        scrollToHideKeyboard();
    }


    public static void sleep(int numOfMiliSeconds) {
        try {
            Thread.sleep(numOfMiliSeconds * 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepForSeconds(int numOfSeconds) {
        try {
            Thread.sleep(numOfSeconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //--------------

    public static void setImplicitWait(int seconds) {
//        AppiumDriver<MobileElement> driver = AppiumController.instance.driver;
        DriverFactory.getTLDriver().manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
    }

    public static boolean isAlertPresent() {
//        AppiumDriver<MobileElement> driver = AppiumController.instance.driver;
        boolean foundAlert = false;
        FluentWait<WebDriver> wait = new WebDriverWait(DriverFactory.getTLDriver(), Constants.SHORT_ELEMENT_TIMEOUT);
        try {
            wait.withTimeout(Duration.ofSeconds(Constants.SHORT_ELEMENT_TIMEOUT)).pollingEvery(Duration.ofSeconds(1)).ignoring(NoSuchElementException.class);
            wait.withMessage("APP ERROR :: Waited " + Constants.SHORT_ELEMENT_TIMEOUT + " secs ");
            wait.until(ExpectedConditions.alertIsPresent());
            foundAlert = true;

        } catch (TimeoutException eTO) {
            foundAlert = false;
        }
        return foundAlert;
    }

    public static void dismissAlert() {
//        AppiumDriver<MobileElement> driver = AppiumController.instance.driver;
        try {
            if(isAlertPresent()==true) {
                Alert alert = DriverFactory.getTLDriver().switchTo().alert();
                alert.dismiss();
            }
        } catch (NoSuchElementException e) {
        }
    }

    public static void acceptAlert() {
//        AppiumDriver<MobileElement> driver = AppiumController.instance.driver;
        try {
            if(isAlertPresent()==true) {
                Alert alert = DriverFactory.getTLDriver().switchTo().alert();
                alert.accept();
            }
        } catch (NoSuchElementException e) {
        }
    }

    public static void dismissRatingAlert() {
        for (int i = 0; i < 2; i++) {
            dismissAlert();
        }
    }

    public static void pressEnter() {
//        AppiumDriver<MobileElement> driver = AppiumController.instance.driver;
        ((AndroidDriver)DriverFactory.getTLDriver()).pressKey(new KeyEvent(AndroidKey.ENTER));
    }

    public static void pressHomeButtonOfAndroidDevice() {
        ((AndroidDriver)DriverFactory.getTLDriver()).pressKey(new KeyEvent(AndroidKey.HOME));
    }

    public static void pressSendKeyOfAndroid() {
        ActionHelper.sleepForSeconds(2);
        DriverFactory.getTLDriver().executeScript("mobile: performEditorAction", ImmutableMap.of("action", "send"));
    }

    public static void pressSearchBtnOfAndroid() {
//        ((AndroidDriver)DriverFactory.getTLDriver()).pressKey(new KeyEvent(AndroidKey.SEARCH));
        ActionHelper.sleepForSeconds(2);
        DriverFactory.getTLDriver().executeScript("mobile: performEditorAction", ImmutableMap.of("action", "search"));
    }

    public static void pressSearchBtnOfIOSKeyboard() {
//        AppiumDriver<MobileElement> driver = AppiumController.instance.driver;
        DriverFactory.getTLDriver().findElement(MobileBy.AccessibilityId("Search")).click();
    }

    public static void pressSendKeyOfIOSKeyboard() {
        DriverFactory.getTLDriver().findElement(MobileBy.AccessibilityId("Send")).click();
    }

    public static void scrollTillElement(By by, int noOfScrolls) {
        if (isPresent(by))
            return;

        int iCounter = 0;
        while (!isPresent(by) && iCounter <= noOfScrolls) {
            scrollPageDown();
            iCounter++;
        }
    }

    public static void scrollTillElementSlowly(By by, int noOfScrolls) {
        if (isPresent(by))
            return;

        int iCounter = 0;
        while (!isPresent(by) && iCounter < noOfScrolls) {
            scrollPageDownSlowly();
            iCounter++;
        }
    }

    public static void scrollTillElementSlowly(By by) {
        if (isPresent(by))
            return;

        int iCounter = 0;
        while (!isPresent(by) && iCounter <= 3) {
            scrollPageDownSlowly();
            iCounter++;
        }
    }

    public static void scrollUpTillElementSlowly(By by) {
        if (isPresent(by))
            return;

        int iCounter = 0;
        while (!isPresent(by) && iCounter <= 3) {
            scrollPageUpSlowly();
            iCounter++;
        }
    }

    public static void scrollPageDown() {
//        AppiumDriver<MobileElement> driver = AppiumController.instance.driver;
        Dimension dimensions = DriverFactory.getTLDriver().manage().window().getSize();
        Double screenHeightStart = dimensions.getHeight() * 0.85;
        int scrollStart = screenHeightStart.intValue();
        Double screenHeightEnd = dimensions.getHeight() * 0.15;
        int scrollEnd = screenHeightEnd.intValue();
        TouchAction touchAction = new TouchAction(DriverFactory.getTLDriver());
        touchAction.press(PointOption.point(200, scrollStart)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
                .moveTo(PointOption.point(200, scrollEnd)).release().perform();
        sleep(1000);
    }

    public static void scrollPageUp() {
//        AppiumDriver<MobileElement> driver = AppiumController.instance.driver;
        Dimension dimensions = DriverFactory.getTLDriver().manage().window().getSize();
        Double screenHeightStart = dimensions.getHeight() * 0.85;
        int scrollStart = screenHeightStart.intValue();
        Double screenHeightEnd = dimensions.getHeight() * 0.15;
        int scrollEnd = screenHeightEnd.intValue();
        TouchAction touchAction = new TouchAction(DriverFactory.getTLDriver());
        touchAction.press(PointOption.point(200, scrollEnd)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
                .moveTo(PointOption.point(200, scrollStart)).release().perform();
        sleep(1000);
    }

    public static void scrollPageUpSlowly() {
//        AppiumDriver<MobileElement> driver = AppiumController.instance.driver;
        Dimension dimensions = DriverFactory.getTLDriver().manage().window().getSize();
        Double screenHeightStart = dimensions.getHeight() * 0.70;
        int scrollStart = screenHeightStart.intValue();
        Double screenHeightEnd = dimensions.getHeight() * 0.30;
        int scrollEnd = screenHeightEnd.intValue();
        TouchAction touchAction = new TouchAction(DriverFactory.getTLDriver());
        touchAction.press(PointOption.point(200, scrollEnd)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
                .moveTo(PointOption.point(200, scrollStart)).release().perform();
        sleep(1000);
    }

    public static void scrollPageDownSlowly() {
//        AppiumDriver<MobileElement> driver = AppiumController.instance.driver;
        Dimension dimensions = DriverFactory.getTLDriver().manage().window().getSize();
        Double screenHeightStart = dimensions.getHeight() * 0.65;
        int scrollStart = screenHeightStart.intValue();
        Double screenHeightEnd = dimensions.getHeight() * 0.30;
        int scrollEnd = screenHeightEnd.intValue();
        TouchAction touchAction = new TouchAction(DriverFactory.getTLDriver());
        touchAction.press(PointOption.point(200, scrollStart)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                .moveTo(PointOption.point(200, scrollEnd)).release().perform();
        sleep(1000);
    }

    public static void scrollPageUpVerySlowly() {
//        AppiumDriver<MobileElement> driver = AppiumController.instance.driver;
        Dimension dimensions = DriverFactory.getTLDriver().manage().window().getSize();
        Double screenHeightStart = dimensions.getHeight() * 0.60;
        int scrollStart = screenHeightStart.intValue();
        Double screenHeightEnd = dimensions.getHeight() * 0.50;
        int scrollEnd = screenHeightEnd.intValue();
        TouchAction touchAction = new TouchAction(DriverFactory.getTLDriver());
        touchAction.press(PointOption.point(200, scrollEnd)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
                .moveTo(PointOption.point(200, scrollStart)).release().perform();
        sleep(1000);
    }

    public static void scrollPageDownVerySlowly() {
//        AppiumDriver<MobileElement> driver = AppiumController.instance.driver;
        Dimension dimensions = DriverFactory.getTLDriver().manage().window().getSize();
        Double screenHeightStart = dimensions.getHeight() * 0.60;
        int scrollStart = screenHeightStart.intValue();
        Double screenHeightEnd = dimensions.getHeight() * 0.50;
        int scrollEnd = screenHeightEnd.intValue();
        TouchAction touchAction = new TouchAction(DriverFactory.getTLDriver());
        touchAction.press(PointOption.point(200, scrollStart)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                .moveTo(PointOption.point(200, scrollEnd)).release().perform();
        sleep(1000);
    }

    public static void scrollFromElementToElement(By byFrom, By byTo) {
        int yFrom = findElement(byFrom).getLocation().y;
        int yToStart = findElement(byTo).getLocation().y;
        int heightTo = findElement(byTo).getSize().height;
        int yTo = yToStart + heightTo;
//        int yTo = findElement(byTo).getCenter().getY();
        int x = DriverFactory.getTLDriver().manage().window().getSize().getWidth()/2;
        TouchAction touchAction = new TouchAction(DriverFactory.getTLDriver());
        touchAction.press(PointOption.point(x, yFrom)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(3)))
                .moveTo(PointOption.point(x, yTo)).release().perform();
        sleep(1000);
    }

    public static void scrollToHideKeyboard() {
//        AppiumDriver<MobileElement> driver = AppiumController.instance.driver;
        Dimension dimensions = DriverFactory.getTLDriver().manage().window().getSize();
        Double screenHeightStart = dimensions.getHeight() * 0.30;
        int scrollStart = screenHeightStart.intValue();
        Double screenHeightEnd = dimensions.getHeight() * 0.25;
        int scrollEnd = screenHeightEnd.intValue();
        Double screenWidthEnd = dimensions.getWidth() * 0.50;
        int scrollX = screenWidthEnd.intValue();
        TouchAction touchAction = new TouchAction(DriverFactory.getTLDriver());
        touchAction.press(PointOption.point(scrollX, scrollStart)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                .moveTo(PointOption.point(scrollX, scrollEnd)).release().perform();
        sleep(1000);
    }

    public static void scrollSectionHorizontally(By by) {
//        AppiumDriver<MobileElement> driver = AppiumController.instance.driver;
        int y = findElement(by).getCenter().y;
        int width = DriverFactory.getTLDriver().manage().window().getSize().getWidth();
        double startX = width * 0.80;
        double endX = width * 0.20;
        TouchAction action = new TouchAction(DriverFactory.getTLDriver());
        int i = 0;
        while (i < 2) {
            action.press(PointOption.point((int) startX, y)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
                    .moveTo(PointOption.point((int) endX, y)).release().perform();
            i++;
        }
    }

    public static void scrollSectionHorizontallySlowly(By by) {
//        AppiumDriver<MobileElement> driver = AppiumController.instance.driver;
        int y = findElement(by).getCenter().y;
        int width = DriverFactory.getTLDriver().manage().window().getSize().getWidth();
        double startX = width * 0.85;
        double endX = width * 0.15;
        TouchAction action = new TouchAction(DriverFactory.getTLDriver());
//        int i = 0;
//        while (i < 2) {
        action.press(PointOption.point((int) startX, y)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
                .moveTo(PointOption.point((int) endX, y)).release().perform();
//            i++;
//        }
    }

    public static void scrollSectionHorizontallySlowlyLeftToRight(By by) {
        int y = findElement(by).getCenter().y;
        int width = DriverFactory.getTLDriver().manage().window().getSize().getWidth();
        double startX = width * 0.15;
        double endX = width * 0.85;
        TouchAction action = new TouchAction(DriverFactory.getTLDriver());
        action.press(PointOption.point((int) startX, y)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
                .moveTo(PointOption.point((int) endX, y)).release().perform();
    }

    public static void scrollSectionHorizontallyLeftToRightFromElementToElement(By byE,By by) {
        int y = findElement(byE).getCenter().y;
        int width = DriverFactory.getTLDriver().manage().window().getSize().getWidth();
        double startX = width * 0.15;
        double endX = width * 0.85;
        TouchAction action = new TouchAction(DriverFactory.getTLDriver());
        int iCounter = 0;
        while (!isPresent(by) && iCounter <= 3) {
            if (isPresent(by))
                break;
            action.press(PointOption.point((int) startX, y)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
                    .moveTo(PointOption.point((int) endX, y)).release().perform();
            iCounter++;
        }
    }

    public static void swipeElementFromRightToLeft(By by) {
        int y = findElement(by).getCenter().getY();
        int width = DriverFactory.getTLDriver().manage().window().getSize().getWidth();
        double startX = width * 0.90;
        double endX = width * 0.10;
        TouchAction action = new TouchAction(DriverFactory.getTLDriver());
        action.press(PointOption.point((int) startX, y)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                .moveTo(PointOption.point((int) endX, y)).release().perform();
    }

    public static void swipeElementFromRightToLeft(By by, int n) {
        int y = findElements(by).get(n).getCenter().getY();
        int width = DriverFactory.getTLDriver().manage().window().getSize().getWidth();
        double startX = width * 0.90;
        double endX = width * 0.10;
        TouchAction action = new TouchAction(DriverFactory.getTLDriver());
        action.press(PointOption.point((int) startX, y)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                .moveTo(PointOption.point((int) endX, y)).release().perform();
    }

    public static void scrollTillElementHorizontally(By by, By allElements) {
//        AppiumDriver<MobileElement> driver = AppiumController.instance.driver;
        if (isPresent(by))
            return;
        int y = findElement(allElements).getCenter().y;
        int width = DriverFactory.getTLDriver().manage().window().getSize().getWidth();
        double startX = width * 0.80;
        double endX = width * 0.20;
        TouchAction action = new TouchAction(DriverFactory.getTLDriver());
        int i = 0;
        while (i < 5) {
            if (isPresent(by))
                break;
            action.press(PointOption.point((int) startX, y)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
                    .moveTo(PointOption.point((int) endX, y)).release().perform();
            i++;
        }
    }

    public static String generateRandomString(int RANDOM_STRING_LENGTH) {
        StringBuffer randStr = new StringBuffer();
        for (int i = 0; i < RANDOM_STRING_LENGTH; i++) {
            int number = getRandomNumber();
            char ch = CHAR_LIST.charAt(number);
            randStr.append(ch);
        }
        return randStr.toString();
    }

    public static int getRandomNumber() {
        int randomInt = -1;
        Random randomGenerator = new Random();
        randomInt = randomGenerator.nextInt(CHAR_LIST.length());
        if (randomInt - 1 == -1) {
            return randomInt;
        } else {
            return randomInt - 1;
        }
    }

    public static int getRandomNumber(int range) {
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(range);
        return randomInt;
    }

    public static String getRandomMobileNumber() {
        String randomNumbers = RandomString.make(9);
        String phNo = "7" + randomNumbers;
        return phNo;
    }

    public static int generateRandomIntBetweenRange(int min, int max) {

        if (min > max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        if (min == max) {
            return min;
        }
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public static boolean notPresent(By by) {
//        AppiumDriver<MobileElement> driver = AppiumController.instance.driver;
        boolean flag = false;
        setImplicitWait(0);
        try {
            if (!DriverFactory.getTLDriver().findElement(by).isDisplayed()) {
                flag = true;
            }
        } catch (Exception e) {
            flag = false;
        }
        setImplicitWait(Constants.DEFAULT_IMPLICITWAIT);
        return flag;
    }

    public static boolean isPresent(By by) {
//        AppiumDriver<MobileElement> driver = AppiumController.instance.driver;
        boolean flag = false;
        setImplicitWait(0);
        if (DriverFactory.getTLDriver().findElements(by).size() > 0) {
            try {
                if (DriverFactory.getTLDriver().findElement(by).isDisplayed()) {
                    flag = true;
                }
            } catch (Exception e) {
                flag = false;
            }
        }
        setImplicitWait(Constants.DEFAULT_IMPLICITWAIT);
        return flag;
    }

    public static boolean isPresentWithWait(By by) {
        boolean flag = true;
        try {
            waitUntilElementVisible(by);
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static void isElementPresent(By by) {
        Assert.assertTrue(isPresent(by));
    }

    public static void isElementPresentWithWait(By by) {
        Assert.assertTrue(isPresentWithWait(by));
    }

    public static void isElementPresentWithShortWait(By by) {
        Assert.assertTrue(isPresentWithShortWait(by));
    }

    public static void isElementPresentWithMidWait(By by) {
        Assert.assertTrue(isPresentWithMidWait(by));
    }

    public static void elementNotPresent(By by) {
        Assert.assertFalse(isPresent(by));
    }

    public static void elementNotPresentWithWait(By by) {
        Assert.assertFalse(isPresentWithWait(by));
    }

    public static void elementNotPresentWithShortWait(By by) {
        Assert.assertFalse(isPresentWithShortWait(by));
    }

    public static void elementNotPresentWithMidWait(By by) {
        Assert.assertFalse(isPresentWithMidWait(by));
    }

    public static boolean isElementPresentByText(String expectedText) {
        boolean flag = true;
        try {
            isPresent(findElementByText(expectedText));
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static boolean isPresentWithShortWait(By by) {
        boolean flag = true;
        try {
            shortWaitUntilElementVisible(by);
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static boolean isPresentWithMidWait(By by) {
        boolean flag = true;
        try {
            midWaitUntilElementVisible(by);
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static void navigateBack() {
//        AppiumDriver<MobileElement> driver = AppiumController.instance.driver;
        DriverFactory.getTLDriver().navigate().back();
    }

    public static void launchApp() {
//    AppiumDriver<MobileElement> driver = AppiumController.instance.driver;
        DriverFactory.getTLDriver().launchApp();
    }

    public static void relaunchApp() {
        DriverFactory.getTLDriver().closeApp();
        DriverFactory.getTLDriver().launchApp();
    }

    public static MobileElement findElement(By by) {
        waitUntilElementVisible(by);
//        shortWaitUntilElementVisible(by);
        return (MobileElement) DriverFactory.getTLDriver().findElement(by);
    }

    public static List<MobileElement> findElements(By by) {
        waitUntilElementVisible(by);
        return (List<MobileElement>) DriverFactory.getTLDriver().findElements(by);
    }

    public static List<MobileElement> findElementsWithoutWait(By by) {
//        AppiumDriver<MobileElement> driver = AppiumController.instance.driver;
        return (List<MobileElement>) DriverFactory.getTLDriver().findElements(by);
    }

    public static By findElementByText(String expectedText) {
        By by = By.xpath("//*[contains(@text, '"+expectedText+"')]");
        return by;
    }

    public static String getText(By by) {
        String text = findElement(by).getText();
//        Logger.getLogger("ElementText", "Text of Element" + getCallingMethodName());
        return text;
    }

    public static String getTextFromList(By by, int n) {
        return findElements(by).get(n).getText();
    }

    public static void matchText(By by, String expectedText) {
        String elText = getText(by).trim();
        Assert.assertEquals(elText, expectedText, "Actual Text Value -- "+elText);
//        Logger.getLogger("ElementTextMatch", "Text of Element" + getCallingMethodName() + "not matching with expected value - "+expectedText);
//        return true;
    }

    public static boolean matchTextFromIndex(By by, int n, String expectedText) {
        String elText = findElements(by).get(n).getText().trim();
        Assert.assertEquals(elText, expectedText);
//        Logger.getLogger("ElementTextMatch", "Text of Element" + getCallingMethodName() + "not matching with expected value - "+expectedText);
        return true;
    }

    public static boolean assertTextContains(By by, String expectedText) {
        waitUntilElementVisible(by);
        String elText = getText(by);
//        Assert.assertTrue(elText.contains(expectedText));
        Assert.assertTrue(StringUtils.containsIgnoreCase(elText, expectedText), "Actual Text Value -- "+elText);
//        Logger.getLogger("ElementTextMatch", "Text of Element" + getCallingMethodName() + "not matching with expected value - "+expectedText);
        return true;
    }

    public static boolean assertTextOrTextContains(By by, String expectedText,String orExpectedText) {
        waitUntilElementVisible(by);
        String elText = getText(by);
        Assert.assertTrue(StringUtils.containsIgnoreCase(elText,expectedText) | StringUtils.containsIgnoreCase(elText,orExpectedText),"Actual Text Value -- "+elText);
        return true;
    }

    public static boolean assertTextContainsFromIndex(By by, int n, String expectedText) {
        waitUntilElementVisible(by);
        String elText = findElements(by).get(n).getText();
//        Assert.assertTrue(elText.contains(expectedText));
        Assert.assertTrue(StringUtils.containsIgnoreCase(elText, expectedText), "Actual Text Value -- "+elText);
//        Logger.getLogger("ElementTextMatch", "Text of Element" + getCallingMethodName() + "not matching with expected value - "+expectedText);
        return true;
    }

    public static void clickFromList(By by, String index) {
        waitUntilElementVisible(by);
        findElements(by).get(Integer.parseInt(index)).click();
    }

    public static void selectfromList(By by, String Name) {
        List<MobileElement> elementList = findElements(by);
        elementList.stream()
                .filter(p -> p.getText().equalsIgnoreCase(Name))
                .findFirst()
                .get()
                .click();
    }

    public static void selectLastFromList(By by, String Name) {
        List<MobileElement> elementList = findElements(by);
        long count = elementList.stream().filter(p -> p.getText().equalsIgnoreCase(Name)).count();
        elementList.stream()
                .filter(p -> p.getText().equalsIgnoreCase(Name))
                .skip(count-1)
                .findFirst()
                .get()
                .click();
    }

    public static void uncheckCheckbox(By by) {
        MobileElement me = findElement(by);
        if (me.getAttribute("checked").equalsIgnoreCase("true")) {
            me.click();
        }
        Logger.getLogger("Uncheck", "UnChecking Checkbox " + getCallingMethodName());
    }

    public static void checkCheckbox(By by) {
        MobileElement me = findElement(by);
        if (me.getAttribute("checked").equalsIgnoreCase("false")) {
            me.click();
        }
//        Logger.getLogger("Check", "Checking Checkbox " + getCallingMethodName());
    }

    public static void checkCheckboxesFromList(By by, int index) {
        waitUntilElementVisible(by);
        findElements(by).get(index).click();
    }

    public static boolean validateCheckbox(By by) {
        MobileElement me = findElement(by);
        boolean checked = false;
        if (me.getAttribute("checked").equalsIgnoreCase("true")) {
            checked = true;
        }
        return checked;
    }

    public static void longpress(By by) {
//        AppiumDriver<MobileElement> driver = AppiumController.instance.driver;
        TouchAction action = new TouchAction(DriverFactory.getTLDriver());
        MobileElement me = findElement(by);
        action.longPress(PointOption.point(me.getCenter().getX(), me.getCenter().getY())).perform();
    }

    public static void fill(By by, String input) {
        if (input.length() < 1)
            return;
        findElement(by).click();
        findElement(by).setValue(input);
        hideKeyboard();
    }

    public static void fillWithoutHideKeyboard(By by, String input) {
        if (input.length() < 1)
            return;
        findElement(by).click();
        findElement(by).setValue(input);
    }

    public static void sendKeys(By by, String input) {
        if (input.length() < 1)
            return;
        findElement(by).click();
        findElement(by).sendKeys(input);
        hideKeyboard();
    }

    public static void sendKeysWithClear(By by, String input) {
        if (input.length() < 1)
            return;
        MobileElement me = findElement(by);
        me.click();
        me.clear();
        me.sendKeys(input);
        hideKeyboard();
    }

    public static void sendKeysWithClearWithoutHideKeyboard(By by, String input) {
        if (input.length() < 1)
            return;
        MobileElement me = findElement(by);
        me.click();
        me.clear();
        me.sendKeys(input);
    }

    public static void sendKeysWithoutHideKeyboard(By by, String input) {
        if (input.length() < 1)
            return;
        MobileElement me = findElement(by);
        me.click();
        me.sendKeys(input);
    }

    public static void sendKeysWithClearOnList(By by, int n, String input) {
        if (input.length() < 1)
            return;
        MobileElement me = findElements(by).get(n);
        me.click();
        me.clear();
        me.sendKeys(input);
    }

    public static void fillWithClear(By by, String input) {
        if (input.length() < 1)
            return;
        MobileElement me = findElement(by);
        me.click();
        me.clear();
        me.setValue(input);
        hideKeyboard();
    }

    public static void fillWithClearOnList(By by, int index, String input) {
        if (input.length() < 1)
            return;
        MobileElement me = findElements(by).get(index);
        me.click();
        me.clear();
        me.setValue(input);
        hideKeyboard();
    }

    public static void fillWithClear_NoHideKeyboard(By by, String input) {
        if (input.length() < 1)
            return;
        MobileElement me = findElement(by);
        me.clear();
        me.setValue(input);
    }

    public static void sendKeysWithScroll(By by, String input) {
        scrollTillElementSlowly(by);
        MobileElement me = findElement(by);
        me.click();
        me.clear();
        me.sendKeys(input);
        hideKeyboard();
    }

    public static void clear(By by) {
        findElement(by).clear();
    }

    public static void click(By by) {
        waitUntilElementEnabled(by);
        findElement(by).click();
//        Logger.getLogger("Clicked", "Clicked on "+getCallingMethodName());
    }

    public static void clickWithScroll(By by) {
        scrollTillElementSlowly(by);
//        scrollTillElement(by, 3);
        findElement(by).click();
//        Logger.getLogger("Clicked", "Clicked on " + getCallingMethodName());
    }

    public static void clickWithScrollUp(By by) {
        scrollUpTillElementSlowly(by);
        findElement(by).click();
    }

    public static void waitUntilElementEnabled(By by) {
//        AppiumDriver<MobileElement> driver = AppiumController.instance.driver;
        setImplicitWait(0);
        try {
            FluentWait<WebDriver> wait = new WebDriverWait(DriverFactory.getTLDriver(), Constants.ELEMENT_TIMEOUT);
            wait.withTimeout(Duration.ofSeconds(Constants.ELEMENT_TIMEOUT)).pollingEvery(Duration.ofSeconds(1)).ignoring(NoSuchElementException.class);
            wait.withMessage("APP ERROR :: Waited " + Constants.ELEMENT_TIMEOUT + " secs ");
            wait.until(ExpectedConditions.elementToBeClickable(by));
        } catch (Exception e) {
            throw new ElementNotVisibleException("Not able to find element", e.getCause());
        }
        finally {
            setImplicitWait(Constants.DEFAULT_IMPLICITWAIT);
        }
    }

    public static void waitUntilElementVisible(By by) {
//        AppiumDriver<MobileElement> driver = AppiumController.instance.driver;
        setImplicitWait(0);
        try {
            FluentWait<WebDriver> wait = new WebDriverWait(DriverFactory.getTLDriver(), Constants.ELEMENT_TIMEOUT);
            wait.withTimeout(Duration.ofSeconds(Constants.ELEMENT_TIMEOUT)).pollingEvery(Duration.ofSeconds(1)).ignoring(NoSuchElementException.class);
            wait.withMessage("APP ERROR :: Waited " + Constants.ELEMENT_TIMEOUT + " secs ");
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            throw new ElementNotVisibleException("Not able to find element", e.getCause());
        }
        finally {
            setImplicitWait(Constants.DEFAULT_IMPLICITWAIT);
        }
    }

    public static void shortWaitUntilElementVisible(By by) {
//        AppiumDriver<MobileElement> driver = AppiumController.instance.driver;
        setImplicitWait(0);
        try {
            FluentWait<WebDriver> wait = new WebDriverWait(DriverFactory.getTLDriver(), Constants.SHORT_ELEMENT_TIMEOUT);
            wait.withTimeout(Duration.ofSeconds(Constants.SHORT_ELEMENT_TIMEOUT)).pollingEvery(Duration.ofSeconds(1)).ignoring(NoSuchElementException.class);
            wait.withMessage("APP ERROR :: Waited " + Constants.SHORT_ELEMENT_TIMEOUT + " secs ");
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            throw new ElementNotVisibleException("Not able to find element", e.getCause());
        }
        finally {
            setImplicitWait(Constants.DEFAULT_IMPLICITWAIT);
        }
    }

    public static void midWaitUntilElementVisible(By by) {
//        AppiumDriver<MobileElement> driver = AppiumController.instance.driver;
        setImplicitWait(0);
        try {
            FluentWait<WebDriver> wait = new WebDriverWait(DriverFactory.getTLDriver(), Constants.MID_ELEMENT_WAIT);
            wait.withTimeout(Duration.ofSeconds(Constants.MID_ELEMENT_WAIT)).pollingEvery(Duration.ofSeconds(1)).ignoring(NoSuchElementException.class);
            wait.withMessage("APP ERROR :: Waited " + Constants.MID_ELEMENT_WAIT + " secs ");
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            throw new ElementNotVisibleException("Not able to find element", e.getCause());
        }
        finally {
            setImplicitWait(Constants.DEFAULT_IMPLICITWAIT);
        }
    }

    public static String getCallingMethodName() {
        String methodName;
        try {
            String[] str = Thread.currentThread().getStackTrace()[3].getMethodName().split("_");
            String newStr = "";
            for (int i = 0; i < str.length; i++) {
                if (i == 0 || i == str.length - 1)
                    continue;
                else
                    newStr = newStr + str[i] + "_";
            }

            methodName = newStr.substring(0, newStr.length() - 1);
        } catch (Exception e) {
            methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        }
        return methodName;
    }

    public static void tapOnPosition(int x, int y) {
//        AppiumDriver<MobileElement> driver = AppiumController.instance.driver;
        TouchAction touchAction = new TouchAction(DriverFactory.getTLDriver());
        touchAction.press(PointOption.point(x, y)).release().perform();
    }

    public static void tapOnCenterOfAnElement(By by) {
        int x = findElement(by).getCenter().getX();
        int y = findElement(by).getCenter().getY();
        TouchAction touchAction = new TouchAction(DriverFactory.getTLDriver());
        touchAction.press(PointOption.point(x, y)).perform();
    }

    public static void tapToHideKeyBoard() {
//        AppiumDriver<MobileElement> driver = AppiumController.instance.driver;
        Dimension dimensions = DriverFactory.getTLDriver().manage().window().getSize();
        Double screenHeightStart = dimensions.getHeight() * 0.50;
        Double screenWidthStart = dimensions.getWidth() * 0.80;
        int tapX = screenWidthStart.intValue();
        int tapY = screenHeightStart.intValue();
        TouchAction touchAction = new TouchAction(DriverFactory.getTLDriver());
        touchAction.press(PointOption.point(tapX, tapY)).perform();
    }

    public static String getElementAttribute(By by, String attribute) {
        String attributeValue = DriverFactory.getTLDriver().findElement(by).getAttribute(attribute);
        return attributeValue;
    }

    public static void clickClearAndFill(MobileElement element, String text) {
        element.click();
        element.clear();
        element.sendKeys(text);
    }

    public static void tapOnAndroidCalenderYearPicker() {
        click(By.id("android:id/date_picker_header_year"));
    }

    public static void selectDobYearFromAndroidCalender(String year) {
        tapOnAndroidCalenderYearPicker();
        scrollUpAndClickInsideElement(By.id("android:id/date_picker_year_picker"), By.id("android:id/text1"), year);
    }

    public static void scrollUpInsideElement(By by) {
        int centerPointX = DriverFactory.getTLDriver().findElement(by).getCenter().getX();
        int centerPointY = DriverFactory.getTLDriver().findElement(by).getCenter().getY();
        int scrollStart = centerPointY+250;
        int scrollEnd = centerPointY-250;
        TouchAction touchAction = new TouchAction(DriverFactory.getTLDriver());
        touchAction.press(PointOption.point(centerPointX, scrollEnd)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
                .moveTo(PointOption.point(centerPointX, scrollStart)).release().perform();
        sleep(500);
    }

    public static void scrollDownInsideElement(By by) {
        int centerPointX = DriverFactory.getTLDriver().findElement(by).getCenter().getX();
        int centerPointY = DriverFactory.getTLDriver().findElement(by).getCenter().getY();
        int scrollStart = centerPointY-250;
        int scrollEnd = centerPointY+250;
        TouchAction touchAction = new TouchAction(DriverFactory.getTLDriver());
        touchAction.press(PointOption.point(centerPointX, centerPointY)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
                .moveTo(PointOption.point(centerPointX, scrollStart)).release().perform();
        sleep(500);
    }

    public static void scrollDownInsideElementSwiftly(By by,int noOfScrolls) {
        int centerPointX = DriverFactory.getTLDriver().findElement(by).getCenter().getX();
        int centerPointY = DriverFactory.getTLDriver().findElement(by).getCenter().getY();
        int scrollStart = centerPointY-800;
        int scrollEnd = centerPointY+200;
        TouchAction touchAction = new TouchAction(DriverFactory.getTLDriver());
        for(int i = 1; i <= noOfScrolls; i++){
            touchAction.press(PointOption.point(centerPointX, scrollEnd)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(200)))
                    .moveTo(PointOption.point(centerPointX, scrollStart)).release().perform();
            sleep(10);
        }
    }

    public static void scrollDownInsideElementFromList(By by,int n) {
        int centerPointX = DriverFactory.getTLDriver().findElements(by).get(n).getCenter().getX();
        int centerPointY = DriverFactory.getTLDriver().findElements(by).get(n).getCenter().getY();
        int scrollStart = centerPointY-250;
        int scrollEnd = centerPointY+250;
        TouchAction touchAction = new TouchAction(DriverFactory.getTLDriver());
        touchAction.press(PointOption.point(centerPointX, centerPointY)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
                .moveTo(PointOption.point(centerPointX, scrollStart)).release().perform();
        sleep(500);
    }

    public static void scrollUpInsideElementVerySlowly(By by,int noOfScrolls) {
        int centerPointX = DriverFactory.getTLDriver().findElement(by).getCenter().getX();
        int centerPointY = DriverFactory.getTLDriver().findElement(by).getCenter().getY();
        int scrollStart = centerPointY-75;
        int scrollEnd = centerPointY;
        TouchAction touchAction = new TouchAction(DriverFactory.getTLDriver());
        for(int i = 1; i <= noOfScrolls; i++){
            touchAction.press(PointOption.point(centerPointX, scrollStart)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(400)))
                    .moveTo(PointOption.point(centerPointX, scrollEnd)).release().perform();
            sleep(100);
        }
    }

    public static void scrollUpInsideElementFromListVerySlowly(By by,int n,int noOfScrolls) {
        int centerPointX = DriverFactory.getTLDriver().findElements(by).get(n).getCenter().getX();
        int centerPointY = DriverFactory.getTLDriver().findElements(by).get(n).getCenter().getY();
        int scrollStart = centerPointY-40;
        int scrollEnd = centerPointY;
        TouchAction touchAction = new TouchAction(DriverFactory.getTLDriver());
        for(int i = 1; i <= noOfScrolls; i++){
            touchAction.press(PointOption.point(centerPointX, scrollStart)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(400)))
                    .moveTo(PointOption.point(centerPointX, scrollEnd)).release().perform();
            sleep(100);
        }
    }

    public static void autoCompleteClick(By by, String input) {
        MobileElement me = findElement(by);
        me.sendKeys(input);
        sleep(2000);
        int centerPointX = DriverFactory.getTLDriver().findElement(by).getCenter().getX();
        int centerPointY = DriverFactory.getTLDriver().findElement(by).getCenter().getY();
        TouchAction touchAction = new TouchAction(DriverFactory.getTLDriver());
        touchAction.tap(PointOption.point(centerPointX+60, centerPointY+140)).release().perform();
        sleep(500);
    }

    public static void scrollUpAndClickInsideElement(By byE, By by, String Name) {
        if(findVisibilityFromList(by, Name) == true) {
            selectfromList(by, Name);
        } else {
            do {
                scrollUpInsideElement(byE);
            } while (findVisibilityFromList(by, Name) == false);
            selectfromList(by, Name);
        }
    }

    public static void scrollDownAndClickInsideElement(By byE, By by, String Name) {
        do{
            scrollDownInsideElement(byE);
        } while (findVisibilityFromList(by, Name)==false);
        selectfromList(by, Name);
    }

    public static void scrollUpOnlyOnceAndClickInsideElement(By byE, By by, String Name) {
        if(findVisibilityFromList(by, Name) == true) {
            selectfromList(by, Name);
        } else {
            for (int i = 0; i < 1; i++) {
                scrollUpInsideElement(byE);
                if(findVisibilityFromList(by, Name) == true) {
                    selectfromList(by, Name);
                    break;
                } else {
                    clickFromList(by, "1");
                }
            }
        }
    }

    public static boolean findVisibilityFromList(By by, String Name) {
        boolean flag = true;
        try {
            List<MobileElement> elementList = findElements(by);
            elementList.stream()
                    .filter(p -> p.getText().equalsIgnoreCase(Name))
                    .findFirst()
                    .get();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static void selectDateMonthFromAndroidCalender(String year, String dateMonthYear) {
        selectDobYearFromAndroidCalender(year);
        By by = By.xpath("//android.view.View[@content-desc=\""+dateMonthYear+"\"]");
        sleepForSeconds(2);
        if (isPresent(by)) {
            click(by);
        } else {
            do {
                click(By.id("android:id/prev"));
            } while (!isPresent(by));
            click(by);
        }
        click(By.id("android:id/button1"));
        sleepForSeconds(1);
    }

    public static void selectTimeFromAndroidClock(int hr, int min) {
        String hrStr = String.valueOf(hr);
        String minStr = String.valueOf(min);
        By by1 = MobileBy.AccessibilityId(hrStr);
        click(by1);
        sleep(500);
        By by2 = MobileBy.AccessibilityId(minStr);
        click(by2);
        sleep(500);
        click(By.id("android:id/button1"));
        sleepForSeconds(1);
    }

    public static void selectValueForElement(By by, String value) {
        findElement(by).setValue(value);
    }

    public static void selectValueForElementList(By by, int index, String value) {
        findElements(by).get(index).setValue(value);
    }

    public static String generateUniqueEmail() {
        System.out.println(signedUpUser+" Generated unique email");
        return signedUpUser;
    }
    public static void openRecentFromFilesOfAndroidDevice() {
        findElements(By.id("android:id/title"));
        for (MobileElement fileTabs: findElements(By.id("android:id/title"))) {
            if(fileTabs.getText().equalsIgnoreCase("Recent")) {
                fileTabs.click();
                break;
            }
        }
    }

    public static void openPhotosFromFilesOfAndroidDevice() {
        findElements(By.id("android:id/title"));
        for (MobileElement fileTabs: findElements(By.id("android:id/title"))) {
            if(fileTabs.getText().equalsIgnoreCase("Photos")) {
                fileTabs.click();
                break;
            }
        }
    }
    public static void allowPermissionWhileUsing() {
        if(isPresentWithShortWait(androidPermissionAllowWhileUsingBtn)) {
            click(androidPermissionAllowWhileUsingBtn);
        }
    }

    public static void allowPermission() {
        if(isPresentWithShortWait(androidPermissionAllowAlwaysBtn)) {
            click(androidPermissionAllowAlwaysBtn);
        } else if(isPresent(permissionAllowBtn)) {
            click(permissionAllowBtn);
        }
        else if(isPresent(androidAllowPermissionBtn)){
            click(androidAllowPermissionBtn);
        }
    }

    public static void denyPermission() {
        if(isPresentWithShortWait(androidPermissionDenyBtn)) {
            click(androidPermissionDenyBtn);
        } else if(isPresent(permissionDenyBtn)) {
            click(permissionDenyBtn);
        }
    }

    public static void denyAndDntAskPermission() {
        if(isPresentWithShortWait(androidPermissionDenyAndDntAskBtn)) {
            click(androidPermissionDenyAndDntAskBtn);
        } else if(isPresent(permissionDntAskAgainCheckbox)) {
            click(permissionDntAskAgainCheckbox);
            click(permissionDenyBtn);
        }
    }

    private static By doNotEnableLocationsServiceBtn = MobileBy.xpath("//*[@text='NO THANKS']");
    private static By enableLocationsServiceBtn = MobileBy.xpath("//*[@text='OK']");

    public static void tapOkOnGPSPopup() {
        if(isPresentWithMidWait(enableLocationsServiceBtn)) {
            ActionHelper.click(enableLocationsServiceBtn);
        }
    }

    public static void tapNoThanksOnGPSPopup() {
        if(ActionHelper.isPresentWithShortWait(doNotEnableLocationsServiceBtn)) {
            ActionHelper.click(doNotEnableLocationsServiceBtn);
        }
    }

    public static Properties prop;
    public static void readPropertyFiles() throws IOException {
        prop = new Properties();
        String androidPropPath = System.getProperty("user.dir")+"/src/test/resources/bitrise.properties";
//        String genericPropPath = System.getProperty("user.dir")+"/src/test/resources/generic.properties";
        try {
            InputStream input = new FileInputStream(androidPropPath);
//            InputStream input2 = new FileInputStream(genericPropPath);
            // load a properties file
            prop.load(input);
//            prop.load(input2);
            // get the property value and print it out
            /*System.out.println(prop.getProperty("Android_BRANCH"));
            System.out.println(prop.getProperty("Android_WORKFLOW"));
            System.out.println(prop.getProperty("iOS_BRANCH"));
            System.out.println(prop.getProperty("iOS_WORKFLOW"));
            System.out.println(prop.getProperty("GENERIC_VARIABLE"));*/
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void switchContext(String context)
    {
        System.out.println("Before Switching : "+DriverFactory.getTLDriver().getContext());
        Set<String> con = DriverFactory.getTLDriver().getContextHandles();
        for(String c : con)
        {
            System.out.println("Available Context : "+c);
            if(c.contains(context))
            {
                DriverFactory.getTLDriver().context(c);
                break;
            }
        }
        System.out.println("After Switching : "+DriverFactory.getTLDriver().getContext());
    }
}
