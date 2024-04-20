package testBase;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;

import org.apache.logging.log4j.Logger; //log4j
import org.apache.logging.log4j.LogManager; //log4j

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

public class BaseClass {
    static public WebDriver driver;
    public Logger logger;
    public Properties p;

    @BeforeClass(groups= {"sanity","regression","master"})
    @Parameters({"os","browser"})
    public void setup(String os, String br) throws IOException {
        // Loading properties file
        FileReader file = new FileReader(".//src/test/resources/config.properties");
        p = new Properties();
        p.load(file);
      //  p.getProperty("appURL"); // this will read prop file and get the value for appURL

        //Loading log4j2.xml file
       logger =  LogManager.getLogger(this.getClass()); // this.getClass - will dynamically get the Test class.

        // Launching OS and  Browser based on condition - Remote/Grid
        if(p.getProperty("execution_env").equalsIgnoreCase("remote")){
            DesiredCapabilities cap = new DesiredCapabilities();
            //os
            if(os.equalsIgnoreCase("windows")){
                cap.setPlatform(Platform.WINDOWS);
            } else if (os.equalsIgnoreCase("mac")) {
                cap.setPlatform(Platform.MAC);
            } else {
                System.out.println("No Matching OS");
                return;
            }

            //broswer
            switch (br.toLowerCase()){
                case "chrome" : cap.setBrowserName("chrome");
                    break;
                case "edge" : cap.setBrowserName("MicrosoftEdge");
                    break;
                default:
                    System.out.println("No Matching browser");
                    return;   /// this will exit from entire script.
            }
            driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),cap);

        } else if(p.getProperty("execution_env").equalsIgnoreCase("local")){
            // Launching Browser based on condition - Locally --> without using remote grid
            switch (br.toLowerCase()){
                case "chrome" : driver = new ChromeDriver();
                    break;
                case "edge" : driver = new EdgeDriver();
                    break;
                default:
                    System.out.println("No Matching browser");
                    return;   /// this will exit from entire script.
            }
        }

        driver.manage().deleteAllCookies(); // delete cookies for credentials.
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(p.getProperty("appURL"));
        driver.manage().window().maximize();
    }

    @AfterClass(groups= {"sanity","regression","master"})
    public void tearDown(){
        driver.close();
    }

    // from commons-lang3 dependencies
    public String randomeString(){
        String generatedString = RandomStringUtils.randomAlphabetic(5);
        return generatedString;
    }
    public String randomeNumber(){
        String generatedString = RandomStringUtils.randomNumeric(10);
        return generatedString;
    }
    public String randomAlphaNumeric(){
        String str = RandomStringUtils.randomAlphabetic(3);
        String num = RandomStringUtils.randomNumeric(3);
        return (str+"@"+num);
    }
    public String captureScreen(String tname) throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());

        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);

        String targetFilePath=System.getProperty("user.dir")+"\\screenshots\\" + tname + "_" + timeStamp + ".png";
        File targetFile=new File(targetFilePath);

        sourceFile.renameTo(targetFile);

        return targetFilePath;

    }
}
