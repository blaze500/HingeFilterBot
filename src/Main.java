public class Main {

    public static void main(String[] args) {
        System.out.println("Program Has Started");
        HingeBotAlgorithm hingeBot= new HingeBotAlgorithm();
        hingeBot.FilterAlgorithm();
        hingeBot.bot.driver.quit();
        System.out.println("Program Has Ended, Dont Forget To Shut Off Appium");
    }
}

