package testCases;

import org.testng.Assert;
import org.testng.annotations.*;
import pageObjects.AccountRegistrationPage;
import pageObjects.HomePage;
import testBase.BaseClass;


public class TC_001_AccountRegistrationTest extends BaseClass {

    @Test(groups = {"regression","master"})
    public void verify_account_registration(){
        logger.info("***** starting TC_001_AccountRegistrationTest ********");
        logger.debug("application logs started..........");
        HomePage hp = new HomePage(driver);
        hp.clickMyAccount();
        logger.info("Clicked on Myaccount link");
        hp.clickRegister();
        logger.info("Clicked on Register Link");
        logger.info("Entering Customer Details");
        AccountRegistrationPage regPage = new AccountRegistrationPage(driver);
        regPage.setFirstName(randomeString().toUpperCase());
        regPage.setLastName(randomeString().toUpperCase());
        regPage.setEmail(randomeString()+"@gmail.com");
        regPage.setTelephone(randomeNumber());

        String password = randomAlphaNumeric();
        regPage.setPassword(password);
        regPage.setConfirmPassword(password);
        regPage.setPrivacyPolicy();
        regPage.clickContinue();
        logger.info("Clicked on continue");
        String confmsg = regPage.getConfirmationMsg();
        Assert.assertEquals(confmsg,"Your Account Has Been Created!");
        logger.info("test passed");

    }


}
