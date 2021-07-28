package AppiumSupport;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class BaseTest {

    static ExtentHtmlReporter htmlReporter;
    static ExtentReports extent;
    static ExtentTest test;
    static DateFormat dformat = new SimpleDateFormat("dd_MM_yyyy_HH-mm-ss");
    static String reportDate = dformat.format(System.currentTimeMillis());


    @Parameters(value = {"udidAndroid","platformAndroid","systemPort","wdaLocalPort","deviceName"})
    @BeforeSuite
    public void setup(@Optional("udidAndroid")String udidAndroid, @Optional("udidIOS")String udidIOS, @Optional("platformAndroid")String platformAndroid, @Optional("platformIOS")String platformIOS, @Optional("systemPort")String systemPort, @Optional("wdaLocalPort")String wdaLocalPort, @Optional("deviceName")String deviceName) throws Exception {
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



    }

}
