package PageObjects;

import AppiumSupport.BaseTest;
import Utils.ActionHelper;

public class LaunchPage extends BaseTest {

    public void verifyLaunchPageVisibility(){
        ActionHelper.isElementPresentWithShortWait();
    }
}
