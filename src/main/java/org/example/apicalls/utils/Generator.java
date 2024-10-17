package org.example.apicalls.utils;

import static org.example.apicalls.utils.Constants.GMAIL_DOMAIN;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Generator {
  public static void openHtml() { // Not working :/
    try {
      File htmlFile = new File("target/cucumber-reports.html").getCanonicalFile();
      if (!htmlFile.exists()) {
        throw new RuntimeException("The file does not exist: ".concat(htmlFile.getAbsolutePath()));
      }

      if (Desktop.isDesktopSupported()) {
        Desktop.getDesktop().browse(htmlFile.toURI());
      } else {
        throw new RuntimeException("Desktop is not supported in this System");
      }
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public static String generateRandomString(int length, String characterString) {
    Random random = new Random();
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      int randomIndex = random.nextInt(characterString.length());
      sb.append(characterString.charAt(randomIndex));
    }
    return sb.toString();
  }

  public static String generateRandomGmail(int length, String characterString){
    return generateRandomString(length, characterString).concat(GMAIL_DOMAIN);
  }

  public static String generateRandomAccountType(){
    String[] strings = {"Savings", "Checking"};
    Random random = new Random();
    return strings[random.nextInt(strings.length)];
  }

  public static double generateRandomDouble(int min, int max){
    Random r = new Random();
      return min + (max - min) * r.nextDouble();
  }

  public static boolean generateRandomBoolean(){
    Random random = new Random();
    return random.nextBoolean();
  }

  public static Date generateRandomDate(long startMillis, long endMillis) {
    long randomMillis = ThreadLocalRandom.current().nextLong(startMillis, endMillis + 1);
    return new Date(randomMillis);
  }

  public static Date generateLocalDate() {
    LocalDateTime now = LocalDateTime.now();
    return Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
  }
}
