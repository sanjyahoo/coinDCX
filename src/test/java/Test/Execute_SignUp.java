package Test;

import AppiumSupport.BaseTest;
import Utils.ActionHelper;
import org.testng.annotations.Test;

public class Execute_SignUp extends BaseTest {

    @Test(enabled = true, groups = {"Sanity", "Regression"}, description = "Test to launch the app and verify launch screen")
    public void tc_executeSignUp_001_verifyProfileBtnNav() throws Exception{
        ActionHelper.launchApp();
        launchPageInstance().verifyLaunchPageVisibility();
        launchPageInstance().clickOnProfileBtn();
    }
}
