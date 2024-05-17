import java.lang.String;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class HingeBotAlgorithm {
    HingeBotFunctions bot;
    String[] filters;
    public HingeBotAlgorithm() {
        bot= new HingeBotFunctions();
        filters = GrabFilters();
    }

    //Removes all matches that don't fit your criteria. Has no user interaction.
    public void FilterAlgorithm(){
        bot.appiumDriver();
        bot.LogInToAccountGoogle();

        String[] profileInformation=RunAlgorithmWithProfileRemoval();
        while(profileInformation!=null){
            System.out.println("Exiting Page");
            bot.ExitPage();
            profileInformation=RunAlgorithmWithProfileRemoval();
        }
        System.out.println("Filter Algorithm Has Finished");
    }

    //Removes all matches that don't fit your criteria. The user is alerted via text when a profile matches so they can do what they need to.
    public void TestAlgorithm(){
        bot.appiumDriver();
        bot.LogInToAccountGoogle();
        bot.RemoveMatch();
    }

    //Removes all matches that don't fit your criteria. The user is alerted via text when a profile matches so they can do what they need to.
    public void AlertAlgorithm(){
        bot.appiumDriver();
        bot.LogInToAccountGoogle();
        String[] profileInformation=RunAlgorithmWithProfileRemoval();
        int MatchesPerDay=8;
        while(profileInformation!=null && MatchesPerDay>0){
            //sendAlert
            //WaitForUserInput
            MatchesPerDay-=1;
            profileInformation=RunAlgorithmWithProfileRemoval();
        }
    }

    //Removes all matches that don't fit your criteria. It also sends a response to a prompt using ChatGPT3.5
    public void RespondWithAiAlgorithm(){
        bot.appiumDriver();
        bot.LogInToAccountGoogle();
        String[] profileInformation=RunAlgorithmWithProfileRemoval();
        int MatchesPerDay=8;
        while(!profileInformation.equals(null) && MatchesPerDay>0){
            String prompt1= profileInformation[1];
            String prompt2= profileInformation[2];
            String prompt3= profileInformation[3];
            //Have AI rate the best response for the job
            //Have AI respond to best one
            MatchesPerDay-=1;
            profileInformation=RunAlgorithmWithProfileRemoval();
        }

    }

    //Scrapes Profile Page And If Their Filters Dont Fit Yours, They Are Removed.
    private String[] RunAlgorithmWithProfileRemoval() {
        boolean NotOutOfMatches=true;
        while(NotOutOfMatches){
            String[] ProfileInformation = bot.ScrapePage();
            if(isMatch(ProfileInformation)){return ProfileInformation;}
            NotOutOfMatches=bot.RemoveMatch();
        }
        return null;
    }

    //Grabs Hinge Filters From Local File
    private static String[] GrabFilters() {
        String[] filters= new String[15];
        System.out.println("Grabbing Filters");
        try {
            File myObj = new File("C:\\Users\\jaden\\IdeaProjects\\untitled1\\src\\filters.txt");
            Scanner myReader = new Scanner(myObj);
            int place =0;
            while (myReader.hasNextLine()) {
                filters[place] = myReader.nextLine();
                place+=1;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();}
        System.out.println(Arrays.toString(filters));
        System.out.println("Finished Grabbing Filters");
        return filters;
    }

    //Checks To See If Scrapped Data Matches Your Filters
    private Boolean isMatch(String[] profileCategories) {
        System.out.println("Checking If Profile Matches");
        System.out.println(Arrays.toString(profileCategories));

        if(profileCategories==null){System.out.println("No Filters To Be Found"); return false;}

        //politics
        if(!this.filters[0].equals("") && profileCategories[7] != null){
            if(!Objects.equals(this.filters[7], profileCategories[7])){ System.out.println("Did Not Agree On Politics, Match Failed"); return false;}
        }

        //datingIntention
        if(!this.filters[1].equals("") && profileCategories[10] != null ){
            try{
                if(!Objects.equals(this.filters[1], profileCategories[10]) && !profileCategories[10].equals("Figuring out my dating goals") && !(profileCategories[10].contains("open to") && !this.filters[1].contains("Life partner"))  ){System.out.println("Did Not Agree On Dating Intentions, Match Failed"); return false;}
            }catch(NullPointerException e){
                if(!Objects.equals(this.filters[1], profileCategories[10])){System.out.println("Did Not Agree On Dating Intentions, Match Failed"); return false;}
            }
        }

        //gender
        if(!this.filters[2].equals("") && profileCategories[15] != null){
            if(!Objects.equals(this.filters[2], profileCategories[15])){System.out.println("Did Not Agree On Gender, Match Failed");return false;}
        }

        //sexuality
        if(!filters[3].equals("") && profileCategories[16] != null){
            if(!Objects.equals(this.filters[3], profileCategories[16])){System.out.println("Did Not Agree On Sexuality, Match Failed");return false;}
        }

        //height
        if(!this.filters[4].equals("") && profileCategories[17] != null){
            String[] filterHeightInfo = this.filters[4].replace("'", "").split(" ");
            String[] profileHeightInfo = profileCategories[17].replace("'", "").split(" ");

            int filterHeightInInches=Integer.parseInt(filterHeightInfo[0])*12+Integer.parseInt(filterHeightInfo[1]);
            int profileHeightInInches=Integer.parseInt(profileHeightInfo[0])*12+Integer.parseInt(profileHeightInfo[1]);

            if(Objects.equals(profileHeightInfo[2], "less")){if(filterHeightInInches<profileHeightInInches){System.out.println("Did Not Agree On Height, Match Failed"); return false;}}
            if(Objects.equals(profileHeightInfo[2], "more")){if(filterHeightInInches>profileHeightInInches){System.out.println("Did Not Agree On Height, Match Failed"); return false;}}

        }

        //ethnicity
        if(!this.filters[5].equals("") && profileCategories[18] != null){
            Boolean hasEthnicity=false;
            for(String ethnicity:this.filters[5].split(",")){
                if(profileCategories[18].contains(ethnicity)){
                    hasEthnicity=true;
                    break;
                }
            }
            if(!hasEthnicity){System.out.println("Did Not Agree On Ethnicity, Match Failed");return false;}
        }

        //kids
        if(!this.filters[6].equals("") && profileCategories[20] != null){
            if(!Objects.equals(this.filters[6], profileCategories[20])){System.out.println("Did Not Agree On Kids, Match Failed"); return false;}
        }

        //familyPlans
        if(!this.filters[7].equals("") && profileCategories[21] != null){
            if(!Objects.equals(this.filters[7], profileCategories[21])){System.out.println("Did Not Agree On Family Plans, Match Failed"); return false;}
        }

        //covidVax
        if(!this.filters[8].equals("") && profileCategories[22] != null){
            if(!Objects.equals(this.filters[8], profileCategories[22])){System.out.println("Did Not Agree On covidVax, Match Failed"); return false;}
        }

        //pets
        if(!this.filters[9].equals("") && profileCategories[23] != null){
            if(!Objects.equals(this.filters[9], profileCategories[23])){System.out.println("Did Not Agree On Pets, Match Failed"); return false;}
        }

        //zodiacSign
        if(!this.filters[10].equals("") && profileCategories[24] != null){
            if(!Objects.equals(this.filters[10], profileCategories[14])){System.out.println("Did Not Agree On Zodiac Signs, Match Failed"); return false;}
        }

        //drinking
        if(!this.filters[11].equals("") && profileCategories[25] != null){
            if(Objects.equals(this.filters[11], "No")){
                if(!Objects.equals(profileCategories[25], "No")){System.out.println("Did Not Agree On Drinking, Match Failed"); return false;}
            }
            else{
                if(Objects.equals(profileCategories[25], "No")){System.out.println("Did Not Agree On Drinking, Match Failed"); return false;}
            }
        }

        //smoking
        if(!this.filters[12].equals("") && profileCategories[26] != null){
            if(Objects.equals(this.filters[12], "No")){
                if(!Objects.equals(profileCategories[26], "No")){System.out.println("Did Not Agree On Smoking, Match Failed"); return false;}
            }
            else{
                if(Objects.equals(profileCategories[26], "No")){System.out.println("Did Not Agree On Smoking, Match Failed"); return false;}
            }
        }

        //marijuana
        if(!this.filters[13].equals("") && profileCategories[27] != null){
            if(Objects.equals(this.filters[13], "No")){
                if(!Objects.equals(profileCategories[27], "No")){System.out.println("Did Not Agree On Marijuana, Match Failed"); return false;}
            }
            else{
                if(Objects.equals(profileCategories[27], "No")){return false;}
            }
        }

        //drugs
        if(!this.filters[14].equals("") && profileCategories[28] != null){
            if(Objects.equals(this.filters[14], "No")){
                if(!Objects.equals(profileCategories[28], "No")){System.out.println("Did Not Agree On Drugs, Match Failed"); return false;}
            }
            else{
                if(Objects.equals(profileCategories[28], "No")){System.out.println("Did Not Agree On Drugs, Match Failed"); return false;}
            }

        }
        System.out.println("Passed Filters");
        return true;
    }

}

