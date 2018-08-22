import java.util.ArrayList;
import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

public class Twitter {
  public static void main(String[] args) {
    Twitter example = new Twitter();
    Tweet.loadStopWords("stopWords.txt");
    example.loadDB("tweets.txt");
  }
  
  private ArrayList<Tweet> tweets;
  
  public Twitter() {
    this.tweets = new ArrayList<Tweet> (); 
  }
  
  // loads the tweets into an ArrayList of Tweets
  public void loadDB(String fileName) {
    try {
      FileReader fr = new FileReader(fileName);
      BufferedReader br = new BufferedReader(fr);
      this.tweets = new ArrayList<Tweet>();
      String currentLine = br.readLine();
      while (currentLine != null) {
        // splits the tweet into its parts
        String[] tweet = currentLine.split("\t");
        String userAccount = tweet[0];
        String date = tweet[1];
        String time = tweet[2];
        String message = tweet[3];
        // initializes the Tweet object with its values
        Tweet currentTweet = new Tweet(userAccount, date, time, message);
        // checks if the message is valid and if so adds it to the ArrayList
        if (currentTweet.checkMessage()) {
          tweets.add(currentTweet);
        }
        currentLine = br.readLine();
      }
      sortTwitter();
      br.close();
      fr.close();      
    } catch (IOException e) {
      System.out.println("Please give me a vaild file name!"); 
    } catch (NullPointerException e) {
      System.out.println("Error checking the stopWords database: The file of stopWords has not been loaded yet.");
    }
  }
  
  // sorts tweets by date/time tweeted with the earliest one first
  public void sortTwitter() {
    int del = 0;
    while (del < this.tweets.size() - 1) {
      Tweet earliest = this.tweets.get(del);
      for (int i = del + 1; i < this.tweets.size(); i++) {
        if (this.tweets.get(i).isBefore(earliest)) {
          earliest = this.tweets.get(i); 
        }
      }
      Tweet temp = this.tweets.get(del);
      int index = this.tweets.indexOf(earliest);
      this.tweets.set(del, earliest);
      this.tweets.set(index, temp);
      del++;
    }
  }
  
  // returns the number of tweets in the data base
  public int getSizeTwitter() {
    return tweets.size(); 
  }
  
  // returns the tweet stored at a given index
  public Tweet getTweet(int index) {
    return tweets.get(index); 
  }
  
  // returns a string containing all the elements in the ArrayList of Tweets in the same format as the tweets.txt file
  public String printDB() {
    String s = "";
    for (int i = 0; i < tweets.size(); i++) {
      s = s + tweets.get(i).toString() + "\n";
    }
    return s;
  }
  
  // returns an ArrayList of Tweets tweeted between tweet1 and tweet2 (inclusive)
  public ArrayList<Tweet> rangeTweets(Tweet tweet1, Tweet tweet2) {
    ArrayList<Tweet> range = new ArrayList<Tweet>();
    int tweet1Index = tweets.indexOf(tweet1);
    int tweet2Index = tweets.indexOf(tweet2);
    if (tweet1Index <= tweet2Index) {
      for (int i = tweet1Index; i <= tweet2Index; i++) {
        range.add(tweets.get(i)); 
      }
      return range;
    } else {
      for (int i = tweet2Index; i <= tweet1Index; i++) {
        range.add(tweets.get(i)); 
      }
      return range;
    }
  }
  
  // writes all the tweets in the database to a given file
  public void saveDB(String fileName) {
    try {
      FileWriter fw = new FileWriter(fileName);
      BufferedWriter bw = new BufferedWriter(fw);
      String s = printDB();
      bw.write(s);
      bw.close();
      fw.close();
    } catch (IOException e) {
    }
  } 
  
  // a helper method for trendingTopic: produces a hashset for a message
  private HashSet<String> uniqueWords(String[] message) {
    HashSet<String> uniqueWords = new HashSet<String>();
    for(String s: message) {
      if (!Tweet.isStopWord(s)) {
        s = s.toLowerCase();
        if (s.contains(",")) {
          s = s.split(",")[0];
        } else if (s.contains(".")) {
          s = s.split("\\.")[0];
        } else if (s.contains(";")) {
          s = s.split(";")[0];
        } else if (s.contains(":")) {
          s = s.split(":")[0];
        } 
        uniqueWords.add(s); 
      }
    }
    return uniqueWords;
  }
  
// a helper method for trendingTopic: produces a hashmap containing how many times each word appears
  private HashMap<String, Integer> countWord(ArrayList<Tweet> tweets) {
    HashMap<String, Integer> map = new HashMap<String, Integer>();
    for (int i = 0; i < tweets.size(); i++) {
      String message = tweets.get(i).getMessage();
      String[] s = message.split(" ");
      HashSet<String> uniqueWords = uniqueWords(s);
      for (String word: uniqueWords) {
        if (!map.containsKey(word)) {
          map.put(word, 1);
        } else {
          int value = map.get(word);
          map.put(word, ++value);
        }
      }
    }
    return map;
  }
  
  // returns the word (excluding stopWords) that is appears in the most tweets in the database
  // only counts words once per tweet
  public String trendingTopic() {
    HashMap<String, Integer> map = countWord(this.tweets);
    ArrayList<String> words = new ArrayList<String>(map.keySet());
    HashMap<String, Integer> frequencyMap = new HashMap<>();
    String trending = null;
    int frequencyCount = -1;
    for (String s: words) {
      Integer count = map.get(s);
      map.put(s, count);
      if (count > frequencyCount) {
        trending = s;
        frequencyCount = count;
      }
    }
    return trending;
  }
}


