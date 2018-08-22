import java.util.HashSet;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.*;

public class Tweet {  
  private String userAccount;
  private String date;
  private String time;
  private String message;
  private static HashSet<String> stopWords;
  
  // tweet constructor
  public Tweet(String userAccount, String date, String time, String message) {
    this.userAccount = userAccount;
    this.date = date;
    this.time = time;
    this.message = message;
  }
  
  // checks if a word is a stop word
  public static boolean isStopWord(String word) {
    word = word.toLowerCase();
    if (stopWords.contains(word)) {
      return true; 
    } else if (word.contains(",")) {
      word = word.split(",")[0];
      return isStopWord(word);
    } else if (word.contains(".")) {
      word = word.split("\\.")[0];
      return isStopWord(word);
    } else if (word.contains(";")) {
      word = word.split(";")[0];
      return isStopWord(word);
    } else if (word.contains(":")) {
      word = word.split(":")[0];
      return isStopWord(word);
    }
    return false;
  }
  
  // checks if the message is 1-15 words long excluding stop words
  public boolean checkMessage() {
    String[] words = message.split(" ");
    int counter = 0;
    for (int i = 0; i < words.length; i++) {
      words[i] = words[i].toLowerCase();
      if (!isStopWord(words[i])) {
        counter ++;
      }
    }
    if (counter > 0 && counter < 16) {
      return true;
    }
    return false;
  }
  
  // returns the date of the tweet
  public String getDate() {
    return date; 
  }
  
  // returns the time of the tweet
  public String getTime() {
    return time; 
  }
  
  // returns the message of the tweet
  public String getMessage() {
    return message; 
  }
  
  // returns the user account of the tweet
  public String getUserAccount() {
    return userAccount; 
  }
  
  // a toString method for printing the tweet's information
  public String toString() {
    String s = userAccount + '\t' + date + '\t' + time + '\t' + message;
    return s;
  }
  
  // returns true if the tweet the method is called on comes before the input tweet
  public boolean isBefore(Tweet input) {
    // retrieving the dates and times of the two tweets
    String[] thisDate = date.split("-");
    int thisYear = Integer.parseInt(thisDate[0]);
    int thisMonth = Integer.parseInt(thisDate[1]);
    int thisDay = Integer.parseInt(thisDate[2]);
    
    String[] inputDate = input.date.split("-");
    int inputYear = Integer.parseInt(inputDate[0]);
    int inputMonth = Integer.parseInt(inputDate[1]);
    int inputDay = Integer.parseInt(inputDate[2]);
    
    String[] thisTime = time.split(":");
    int thisHour = Integer.parseInt(thisTime[0]);
    int thisMinute = Integer.parseInt(thisTime[1]);
    int thisSecond = Integer.parseInt(thisTime[2]);
    
    String[] inputTime = input.time.split(":");
    int inputHour = Integer.parseInt(inputTime[0]);
    int inputMinute = Integer.parseInt(inputTime[1]);
    int inputSecond = Integer.parseInt(inputTime[2]);
    
    if (thisYear < inputYear) {
      return true;
    } else if (thisYear == inputYear) {
      if (thisMonth < inputMonth) {
        return true;
      } else if (thisMonth == inputMonth) {
        if (thisDay < inputDay) {
          return true;
        } else if (thisDay == inputDay) {
          if (thisHour < inputHour) {
            return true;
          } else if (thisHour == inputHour) {
            if (thisMinute < inputMinute) {
              return true;
            } else if (thisMinute == inputMinute) {
              if (thisSecond < inputSecond) {
                return true;
              }
            }
          }
        }
      }
    }
    return false;
  }
  
  public static void loadStopWords(String fileName) {
    try {
      FileReader fr = new FileReader(fileName);
      BufferedReader br = new BufferedReader(fr);
      ArrayList<String> newList = new ArrayList<String>();
      String currentLine = br.readLine();
      while (currentLine != null) {
        newList.add(currentLine);
        currentLine = br.readLine();
      }
      stopWords = new HashSet<String> (newList);
      br.close();
      fr.close();      
    } catch (IOException e) {
      System.out.println("Please give me a vaild file name!"); 
    }
  }
}

