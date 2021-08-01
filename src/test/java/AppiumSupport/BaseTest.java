package AppiumSupport;

import PageObjects.HomePage;
import PageObjects.LaunchPage;
import Utils.ActionHelper;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.zip.ZipFile;

public class BaseTest {

    static ExtentHtmlReporter htmlReporter;
    static ExtentReports extent;
    static ExtentTest test;
    static DateFormat dformat = new SimpleDateFormat("dd_MM_yyyy_HH-mm-ss");
    static String reportDate = dformat.format(System.currentTimeMillis());

    private static final ThreadLocal<LaunchPage> launchPage = new ThreadLocal<>();
    private static final ThreadLocal<HomePage> homePage = new ThreadLocal<>();

    @Parameters(value = {"udidAndroid","platformAndroid","systemPort","wdaLocalPort","deviceName"})
    @BeforeSuite(alwaysRun = true)
    public void setup(@Optional("udidAndroid")String udidAndroid, @Optional("platformAndroid")String platformAndroid, @Optional("systemPort")String systemPort, @Optional("wdaLocalPort")String wdaLocalPort, @Optional("deviceName")String deviceName) throws Exception {

        File classPathRoot = new File(System.getProperty("user.dir"));
        File outputDir = new File(classPathRoot,"/output");
        File extentPath = new File(outputDir,"extentTestReport.html");
        outputDir.mkdir();

        try{
            FileUtils.cleanDirectory(outputDir);
        } catch (Exception ee){}

        try{
            FileUtils.deleteQuietly(new File(classPathRoot,"testResults.zip"));
        } catch (Exception ex){}

        htmlReporter = new ExtentHtmlReporter(extentPath.getAbsolutePath());
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);

        htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setDocumentTitle("Test Report");
        htmlReporter.config().setReportName("coinDCX Test Execution Report");
        htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
        htmlReporter.config().setTheme(Theme.DARK);
        htmlReporter.config().setTimeStampFormat("EEE, MMM dd, yyyy, hh:mm a '('zzz')'");

        extent.setSystemInfo("Device",deviceName);
        extent.setSystemInfo("OS Version",platformAndroid);

        LaunchPage instanceLaunchPage = new LaunchPage(DriverFactory.getTLDriver());
        HomePage instanceHomePage = new HomePage();

        launchPage.set(instanceLaunchPage);
        homePage.set(instanceHomePage);
    }

    protected static LaunchPage launchPageInstance(){
        return launchPage.get();
    }

    protected static HomePage homePageInstance(){
        return homePage.get();
    }

    @Parameters(value = {"udidAndroid","platformAndroid","systemPort","wdaLocalPort","deviceName"})
    @BeforeTest(alwaysRun = true)
    public void serverSetup(@Optional("udidAndroid")String udidAndroid, @Optional("platformAndroid")String platformAndroid, @Optional("systemPort")String systemPort, @Optional("wdaLocalPort")String wdaLocalPort, @Optional("deviceName")String deviceName) throws Exception{
        stopAppiumServer("4733");
        stopAppiumServer(wdaLocalPort);
        startAppiumServer("4733");

        DriverFactory.setTLDriver(udidAndroid,platformAndroid,systemPort,wdaLocalPort,deviceName);
        ActionHelper.switchContext("WEBVIEW");
    }



    void startAppiumServer(String p){
        CommandLine command = new CommandLine("/usr/local/bin/node");
        command.addArgument("/usr/local/bin/appium",false);
        command.addArgument("--address", false);
        command.addArgument("127.0.0.1");
        command.addArgument("--port", false);
        command.addArgument(p);
        command.addArgument("--log-level", false);
        command.addArgument("error");
        command.addArgument("--no-reset", true);
        command.addArgument("--session-override", true);
        command.addArgument("--relaxed-security", true);

        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
        DefaultExecutor executor = new DefaultExecutor();
        executor.setExitValue(1);
        try {executor.execute(command, resultHandler);
            Thread.sleep(5000);System.out.println("Appium server started.");}
        catch (IOException e)
        {e.printStackTrace();}
        catch (InterruptedException e)
        {e.printStackTrace();}
    }


    void stopAppiumServer(String p) throws IOException, InterruptedException{
        String[] command1 = {"/bin/bash", "-c", "lsof -i :'"+p+"' | awk '{print $2; }' | head -n 2 | grep -v PID | xargs kill -9"};
        try {Runtime.getRuntime().exec(command1);
            System.out.println("Appium server stopped. Port("+p+")" + "lsof -i :'"+p+"' | awk '{print $2; }' | head -n 2 | grep -v PID | xargs kill -9");}
        catch (IOException e) {e.printStackTrace();}
        Thread.sleep(2000);
    }

    @Parameters(value = {"udidAndroid","platformAndroid","systemPort","wdaLocalPort","deviceName"})
    @AfterTest(alwaysRun = true)
    public void sessionStop(@Optional("udidAndroid")String udidAndroid, @Optional("platformAndroid")String platformAndroid, @Optional("systemPort")String systemPort, @Optional("wdaLocalPort")String wdaLocalPort, @Optional("deviceName")String deviceName) throws Exception{
        DriverFactory.getTLDriver().closeApp();
        DriverFactory.getTLDriver().resetApp();
        System.out.println("Reset app after test");
        DriverFactory.cleanUpTLDriver();
    }

    @Parameters(value = {"udidAndroid","platformAndroid","systemPort","wdaLocalPort","deviceName"})
    @AfterSuite(alwaysRun = true)
    public void tearDown(@Optional("udidAndroid")String udidAndroid, @Optional("platformAndroid")String platformAndroid, @Optional("systemPort")String systemPort, @Optional("wdaLocalPort")String wdaLocalPort, @Optional("deviceName")String deviceName)throws Exception{
        try {
            extent.flush();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void BeforeEveryTestMethod(ITestResult result){
        test = extent.createTest(result.getMethod().getDescription(), result.getMethod().getMethodName());
    }

    @AfterMethod(alwaysRun = true)
    public void AfterEveryTestMethod(ITestResult result) throws IOException{
        String screenShotPath = getScreenShotPath(result);

        Long duration = (result.getEndMillis() - result.getStartMillis());
        Long testDuration = duration/1000;
        System.out.println("Test Duration -"+testDuration);
        String testCase = result.getMethod().getMethodName().replace("<br/>","");
        System.out.println(testCase);

        if(result.getStatus()== ITestResult.FAILURE){
            System.out.println("Test Status = FAILED \n\n");
            captureScreenShot(result.getMethod().getMethodName(),screenShotPath);
            test.log(Status.FAIL, MarkupHelper.createLabel(result.getMethod().getMethodName()+" FAILED", ExtentColor.RED)).addScreenCaptureFromPath(result.getMethod().getMethodName()+".jpg","Screenshot path - "+screenShotPath);
            test.fail(result.getThrowable());
        } else if(result.getStatus()== ITestResult.SUCCESS){
            System.out.println("Test Status = PASSED \n\n");
            test.log(Status.PASS,MarkupHelper.createLabel(result.getMethod().getMethodName()+" PASSED",ExtentColor.GREEN));
        } else {
            System.out.println("Test Status = SKIPPED \n\n");
            test.log(Status.SKIP,MarkupHelper.createLabel(result.getMethod().getMethodName()+" SKIPPED",ExtentColor.ORANGE));
            test.skip(result.getThrowable());

        }
    }

    String getScreenShotPath (ITestResult result){
        String screenShotPath = System.getProperty("user.dir")+"/output/"+result.getMethod().getMethodName()+".jpg";
        return screenShotPath;
    }

    void captureScreenShot(String methodname,String screenShotFileName){
        try {
            File snapShot = new File(screenShotFileName);
            File tempsnapShot = ((TakesScreenshot)DriverFactory.getTLDriver()).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(tempsnapShot,snapShot);
        } catch (Exception e){
            System.out.println("Error on taking screenshot");
        }
    }
}
