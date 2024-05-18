import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.Wait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class HingeBotFunctions {
    AppiumDriver driver;
    //Initiates Appium Driver
    public void appiumDriver() {
        UiAutomator2Options clientOptions = new UiAutomator2Options();
        clientOptions.setCapability("deviceName", "");
        clientOptions.setCapability("udid", "");
        clientOptions.setCapability("platformName", "");
        clientOptions.setCapability("platformVersion", "");

        clientOptions.setCapability("appPackage", "co.hinge.app");
        clientOptions.setCapability("appActivity", "co.hinge.app.ui.AppActivity");

        try {
            driver = new AppiumDriver(new URL("http://127.0.0.1:4723/"), clientOptions);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    //Logs Into Hinge Via Google Account
    public void LogInToAccountGoogle() {
        WaitTime(5);
        driver.findElement(By.xpath("//android.widget.ScrollView/android.view.View[2]/android.widget.Button")).click();

        WaitTime(5);
        driver.findElement(By.xpath("//android.widget.TextView[@text=\"Sign in with Google\"]")).click();

        WaitTime(5);
        driver.findElement(By.xpath("(//android.widget.LinearLayout[@resource-id=\"com.google.android.gms:id/container\"])[1]")).click();

        WaitTime(10);
        driver.findElement(By.xpath("//android.widget.Button[@resource-id=\"co.hinge.app:id/dialog_primary_button\"]")).click();
    }

    //Scrapes Horizontal Axis.
    private String[] ScrapeSliderCategories(String[] profileCategories, int yAxis, int AppWidth) {
        String[] profileIDs = {"co.hinge.app:id/gender", "co.hinge.app:id/sexuality", "co.hinge.app:id/height", "co.hinge.app:id/location", "co.hinge.app:id/ethnicity", "co.hinge.app:id/kids", "co.hinge.app:id/familyPlans", "co.hinge.app:id/covidVax", "co.hinge.app:id/pets", "co.hinge.app:id/zodiacSign", "co.hinge.app:id/drinking", "co.hinge.app:id/smoking", "co.hinge.app:id/marijuana", "co.hinge.app:id/drugs"};
        int place=0;
        System.out.println("\nScraping Left To Right");
        while(place<profileIDs.length){
            try{WebElement age = driver.findElement(By.id(profileIDs[place]));
                profileCategories[place+15]=age.getText();
            }
            catch(org.openqa.selenium.NoSuchElementException notPrinted) {
                ScrollLeftOrRight(yAxis, (int)(AppWidth*.85), (int)(AppWidth*.15));
                try{
                    WebElement age = driver.findElement(By.id(profileIDs[place]));
                    profileCategories[place+15]=age.getText();
                }
                catch(org.openqa.selenium.NoSuchElementException e){
                    ScrollLeftOrRight(yAxis, (int)(AppWidth*.15), (int)(AppWidth*.85));
                }
            }
            place+=1;
            WaitTime(2);
        }
        System.out.println(Arrays.toString(profileCategories)+"\n");
        return profileCategories;
    }

    //Modified Version Of ScrapeSliderCategories That Is More Efficent.
    private String[] ScrapeSliderCategoriesV2(String[] profileCategories, int yAxis, int AppWidth) {
        String[] profileIDs = {"co.hinge.app:id/gender", "co.hinge.app:id/sexuality", "co.hinge.app:id/height", "co.hinge.app:id/location", "co.hinge.app:id/ethnicity", "co.hinge.app:id/kids", "co.hinge.app:id/familyPlans", "co.hinge.app:id/covidVax", "co.hinge.app:id/pets", "co.hinge.app:id/zodiacSign", "co.hinge.app:id/drinking", "co.hinge.app:id/smoking", "co.hinge.app:id/marijuana", "co.hinge.app:id/drugs"};
        System.out.println("\nScraping Left To Right");
        int place =0;
        int lastPlace=-1;
        while(place!=lastPlace) {
            lastPlace = place;
            for (int i = 0; i < profileIDs.length; i++) {
                try {
                    WebElement age = driver.findElement(By.id(profileIDs[i]));
                    profileCategories[i + 15] = age.getText();
                    place = i;
                } catch (org.openqa.selenium.NoSuchElementException notPrinted) {System.out.println("Did Not Find "+profileIDs[i]);}
                System.out.println("Current Place="+Integer.toString(place));
                System.out.println("Last Place="+Integer.toString(lastPlace));
            }
            System.out.println("Scrolling Right");
            ScrollLeftOrRight(yAxis, (int)(AppWidth*.8), (int)(AppWidth*.3));
        }
        Boolean noAgeFound=true;
        while(noAgeFound){
            try{
                WebElement age =driver.findElement(By.id("co.hinge.app:id/age"));
                System.out.println("Left Right Scrolling Reset");
                noAgeFound=false;
            }
            catch(org.openqa.selenium.NoSuchElementException e){
                System.out.println("Scrolling Left");
                ScrollLeftOrRight(yAxis, (int)(AppWidth*.3), (int)(AppWidth*.8));
            }
        }
        return profileCategories;
    }

    //Scrapes The Hinge Profile On The Vertical Access. When It Detects The Horizontal Scrollable Categoires, It Will Scrape That As Well.
    public String[] ScrapePage() {
        String[] profileCategories= new String[29];
        Boolean notScrapedLeftOrRight = true;
        int AppWidth = driver.manage().window().getSize().getWidth();
        int AppHeight = driver.manage().window().getSize().getHeight();

        System.out.println(AppWidth);
        System.out.println(AppHeight);
        System.out.println("Starting To Scrape Page");

        while(Objects.equals(profileCategories[3], null)){
            try {profileCategories[0]=driver.findElement(By.xpath("//android.view.ViewGroup[@resource-id=\"co.hinge.app:id/profile_layout_container\"]/android.view.View/android.widget.TextView[contains(@text,'')]")).getText();} catch (org.openqa.selenium.NoSuchElementException notPrinted) {}

            try{
                Boolean dontSkipAnswer=true;
                WebElement question = driver.findElement(By.id("co.hinge.app:id/question"));
                WebElement answer = driver.findElement(By.id("co.hinge.app:id/answer"));

                try {
                    if (Objects.equals(question, driver.findElement(By.xpath("//android.view.ViewGroup[@resource-id=\"co.hinge.app:id/pollBubble\"]//android.widget.TextView[@resource-id=\"co.hinge.app:id/question\"]")))) {
                        dontSkipAnswer = false;
                        System.out.println("Poll Prompt Found But Skipped");
                    }
                } catch(org.openqa.selenium.NoSuchElementException e){System.out.println("No Poll Found");}

                try {
                    if (Objects.equals(question, driver.findElement(By.xpath("//android.view.ViewGroup[@content-desc=\"Voice prompt\"]//android.widget.TextView[@resource-id=\"co.hinge.app:id/question\" and @text=\"I wind down by\"]")))) {
                        dontSkipAnswer = false;
                        System.out.println("Voice Prompt Found But Skipped");
                    }
                } catch(org.openqa.selenium.NoSuchElementException e){System.out.println("No Voice Prompt Found");}


                if(dontSkipAnswer) {
                    for (int i = 1; i < 4; i++) {
                        if (Objects.equals(profileCategories[i], question.getText() + " " + answer.getText())) {
                            break;
                        }
                        if (Objects.equals(profileCategories[i], null)) {
                            profileCategories[i] = driver.findElement(By.id("co.hinge.app:id/question")).getText() + " " + driver.findElement(By.id("co.hinge.app:id/answer")).getText();
                            break;
                        }
                    }
                }
            }
            catch(org.openqa.selenium.NoSuchElementException e){}

            try {if(profileCategories[4] == null){ profileCategories[4]=driver.findElement(By.id("co.hinge.app:id/jobTitle")).getText(); WaitTime(1);}} catch (org.openqa.selenium.NoSuchElementException e) {}

            try {if(profileCategories[5] == null){profileCategories[5]=driver.findElement(By.id("co.hinge.app:id/schools")).getText(); WaitTime(1);}} catch (org.openqa.selenium.NoSuchElementException e) {}

            try {if(profileCategories[6] == null){profileCategories[6]=driver.findElement(By.id("co.hinge.app:id/religions")).getText(); WaitTime(1);}} catch (org.openqa.selenium.NoSuchElementException e) {}

            try {if(profileCategories[7] == null){profileCategories[7]=driver.findElement(By.id("co.hinge.app:id/politics")).getText(); WaitTime(1);}} catch (org.openqa.selenium.NoSuchElementException e) {}

            try {if(profileCategories[8] == null){profileCategories[8]=driver.findElement(By.id("co.hinge.app:id/hometown")).getText(); WaitTime(1);}} catch (org.openqa.selenium.NoSuchElementException e) {}

            try {if(profileCategories[9] == null){profileCategories[9]=driver.findElement(By.id("co.hinge.app:id/languagesSpoken")).getText(); WaitTime(1);}} catch (org.openqa.selenium.NoSuchElementException e) {}

            try {if(profileCategories[10] == null){profileCategories[10]=driver.findElement(By.id("co.hinge.app:id/datingIntention")).getText(); WaitTime(1);}} catch (org.openqa.selenium.NoSuchElementException e) {}

            try {if(profileCategories[11] == null){profileCategories[11]=driver.findElement(By.id("co.hinge.app:id/datingIntentionBackstory")).getText(); WaitTime(1);}} catch (org.openqa.selenium.NoSuchElementException e) {}

            try {if(profileCategories[12] == null){profileCategories[12]=driver.findElement(By.id("co.hinge.app:id/relationshipType")).getText(); WaitTime(1);}} catch (org.openqa.selenium.NoSuchElementException e) {}

            try {if(profileCategories[13] == null){profileCategories[13]=driver.findElement(By.id("co.hinge.app:id/relationshipTypeBackstory")).getText(); WaitTime(1);}} catch (org.openqa.selenium.NoSuchElementException e) {}

            if(notScrapedLeftOrRight){
                try {
                    WaitTime(2);
                    WebElement age =driver.findElement(By.id("co.hinge.app:id/age"));
                    WaitTime(2);
                    profileCategories[14]=driver.findElement(By.id("co.hinge.app:id/age")).getText();
                    WaitTime(2);
                    ScrapeSliderCategoriesV2(profileCategories, age.getLocation().y, AppWidth);
                    System.out.println(Arrays.toString(profileCategories));
                    notScrapedLeftOrRight = false;
                }
                catch (org.openqa.selenium.NoSuchElementException e) {}
            }

            ScrollDownOrUP((int)(AppWidth/2), (int)(AppHeight*.7), (int)(AppHeight*.3));
            WaitTime(1);
            System.out.println(profileCategories[1]);
            System.out.println(profileCategories[2]);
            System.out.println(profileCategories[3]);
        }
        System.out.println(Arrays.toString(profileCategories));
        notScrapedLeftOrRight = true;
        return profileCategories;
    }

    //Makes Bot Exit Profile Page
    public Boolean ExitPage(){
        try{
            WaitTime(1);
            driver.findElement(By.id("co.hinge.app:id/no_button")).click();
            WaitTime(3);
            System.out.println("Was Able To Exit Page");
            return true;
        }
        catch(org.openqa.selenium.NoSuchElementException e){
            System.out.println("Was Unable To Exit Page");
            return false;
        }
    }

    //Makes Bot Remove Profile Page
    public Boolean RemoveMatch(){
        try{
            WaitTime(1);
            driver.findElement(By.xpath("//android.view.ViewGroup[@resource-id=\"co.hinge.app:id/profile_layout_container\"]/android.view.View/android.view.View[2]/android.widget.Button")).click();
            WaitTime(1);
            driver.findElement(By.xpath("//android.widget.ScrollView/android.view.View[1]")).click();
            WaitTime(1);
            driver.findElement(By.xpath("//android.widget.TextView[@resource-id=\"co.hinge.app:id/multi_select_button\" and @text=\"I'm not interested in this person\"]")).click();
            WaitTime(2);
            System.out.println("Removed Match");
            return true;
        }
        catch(org.openqa.selenium.NoSuchElementException e){
            try{
                WaitTime(1);
                driver.findElement(By.xpath("//android.widget.Button")).click();
                WaitTime(1);
                driver.findElement(By.xpath("//android.widget.ScrollView/android.view.View[1]")).click();
                WaitTime(1);
                driver.findElement(By.xpath("//android.widget.TextView[@resource-id=\"co.hinge.app:id/multi_select_button\" and @text=\"I'm not interested in this person\"]")).click();
                WaitTime(2);
                System.out.println("Removed Match");
                return true;
            }
            catch(org.openqa.selenium.NoSuchElementException d){
                System.out.println("Was Unable To Remove Match");
                return false;
            }
        }
    }
    public void findAndRespondToPromptAfterScraping(String promptText, String personsName, String response){
        int AppWidth = driver.manage().window().getSize().getWidth();
        int AppHeight = driver.manage().window().getSize().getHeight();
        while(true){
            try{
                driver.findElement(By.xpath("//android.view.ViewGroup[@resource-id=\"co.hinge.app:id/prompt_bubble\"]//android.widget.TextView[@resource-id=\"co.hinge.app:id/answer\" and @text=\""+promptText+"\"]")).getText();
                driver.findElement(By.xpath("//android.view.ViewGroup[@resource-id=\"co.hinge.app:id/prompt_bubble\"]//android.widget.ImageButton[@content-desc=\"Like "+personsName+"'s prompt\"]")).click();
                driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"co.hinge.app:id/comment_composition_view\"]")).sendKeys(response);
                driver.findElement(By.xpath("//android.view.ViewGroup[@resource-id=\"co.hinge.app:id/buttons_layout\"]/android.view.View/android.view.View[2]/android.widget.Button")).click();
            }
            catch(org.openqa.selenium.NoSuchElementException e){}
            try{
                driver.findElement(By.xpath("(//android.view.ViewGroup[@resource-id=\"co.hinge.app:id/prompt_bubble\"])[1]//android.widget.TextView[@resource-id=\"co.hinge.app:id/answer\" and @text=\""+promptText+"\"]"));
                driver.findElement(By.xpath("(//android.view.ViewGroup[@resource-id=\"co.hinge.app:id/prompt_bubble\"])[1](//android.widget.ImageButton[@content-desc=\"Like "+personsName+"'\s prompt\"])[1]")).click();
                driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"co.hinge.app:id/comment_composition_view\"]")).sendKeys(response);
                driver.findElement(By.xpath("//android.view.ViewGroup[@resource-id=\"co.hinge.app:id/buttons_layout\"]/android.view.View/android.view.View[2]/android.widget.Button")).click();
            }
            catch(org.openqa.selenium.NoSuchElementException e){}
            try{
                driver.findElement(By.xpath("(//android.view.ViewGroup[@resource-id=\"co.hinge.app:id/prompt_bubble\"])[2]//android.widget.TextView[@resource-id=\"co.hinge.app:id/answer\" and @text=\""+promptText+"\"]"));
                driver.findElement(By.xpath("(//android.view.ViewGroup[@resource-id=\"co.hinge.app:id/prompt_bubble\"])[2](//android.widget.ImageButton[@content-desc=\"Like "+personsName+"'s prompt\"])[2]")).click();
                driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"co.hinge.app:id/comment_composition_view\"]")).sendKeys(response);
                driver.findElement(By.xpath("//android.view.ViewGroup[@resource-id=\"co.hinge.app:id/buttons_layout\"]/android.view.View/android.view.View[2]/android.widget.Button")).click();
            }
            catch(org.openqa.selenium.NoSuchElementException e){}

            ScrollDownOrUP((int)(AppWidth/2), (int)(AppHeight*.25), (int)(AppHeight*.75));
        }
    }

    public void CheckMatches() {
        WaitTime(5);
        driver.findElement(By.xpath("//android.widget.ImageView[@resource-id=\"co.hinge.app:id/matchesIcon\"]")).click();

        WaitTime(5);
        driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Expand\"]")).click();

        WaitTime(5);
        try {
            driver.findElement(By.xpath("//androidx.compose.ui.viewinterop.ViewFactoryHolder/androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View")).click();
        } catch (NullPointerException e) {
            int place = 1;
            while (true) {
                try {
                    driver.findElement(By.xpath("//androidx.compose.ui.platform.ComposeView[@resource-id=\"co.hinge.app:id/match_list_composable\"]/android.view.View/android.view.View/androidx.compose.ui.viewinterop.ViewFactoryHolder[" + Integer.toString(place) + "]/androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View")).click();
                } catch (NullPointerException c) {
                    break;
                }
                place += 1;
            }
        }

    }



    //Reusable Thread.Sleep Class
    private void WaitTime(Integer seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //Makes Bot Scroll Up Or Down
    private void ScrollDownOrUP(int xCord, int yStart, int yEnd) {
        PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        Sequence sequence = new Sequence(finger1, 1)
                .addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), xCord, yStart))
                .addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger1.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), xCord, yEnd))
                .addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(sequence));
        WaitTime(2);
    }
    //Makes Bot Scroll Left Or Right
    private void ScrollLeftOrRight(int yCord, int xStart, int xEnd) {
        PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        Sequence sequence = new Sequence(finger1, 1)
                .addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), xStart, yCord))
                .addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger1.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), xEnd, yCord))
                .addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(sequence));
        WaitTime(2);
    }


}
