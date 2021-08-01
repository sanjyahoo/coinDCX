package AppiumSupport;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Optional;

import java.io.File;
import java.net.URL;

public class DriverFactory {

    public static final ThreadLocal<AppiumDriver<MobileElement>> tldriver = new ThreadLocal<AppiumDriver<MobileElement>>();

    public static void setTLDriver(@Optional("udidAndroid")String udidAndroid, @Optional("platformAndroid")String platformAndroid, @Optional("systemPort")String systemPort, @Optional("wdaLocalPort")String wdaLocalPort, @Optional("deviceName")String deviceName) throws Exception {
        System.out.println("Current Thread ID Driver Installation: "+Thread.currentThread().getName());

        String[] platformInfoAndroid = platformAndroid.split(" ");

        DesiredCapabilities capabilities = new DesiredCapabilities();
        AppiumDriver<MobileElement> driver;

        File classpathRoot = new File(System.getProperty("user.dir"));
        File appDir = new File(classpathRoot,"/app");
        File app = new File(appDir,"coinDCX.apk");

        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.ANDROID_UIAUTOMATOR2);
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT,"120");
        capabilities.setCapability("autoLaunch",true);
        capabilities.setCapability("app",app.getAbsolutePath());
        capabilities.setCapability("adbExecTimeout",600000);
        capabilities.setCapability("disableAndroidWatchers",true);
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,deviceName);
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME,platformInfoAndroid[0]);
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION,platformInfoAndroid[1]);
        capabilities.setCapability(MobileCapabilityType.UDID,udidAndroid);
        capabilities.setCapability(AndroidMobileCapabilityType.SYSTEM_PORT,systemPort);
        capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY,"com.coindcx.security.ScreenLock");
        capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE,"com.coindcx");
        driver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4733/wd/hub"),capabilities);
        System.out.println("Up and Running");

        tldriver.set(driver);
    }

    public static AppiumDriver<MobileElement> getTLDriver(){
        return tldriver.get();
    }

    public static void cleanUpTLDriver(){
        tldriver.get().quit();
        tldriver.remove();
    }

}
