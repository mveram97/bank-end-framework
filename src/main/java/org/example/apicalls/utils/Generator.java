package org.example.apicalls.utils;

import static org.example.apicalls.utils.Constants.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.example.api.data.entity.Account;
import org.example.api.data.entity.Card;
import org.example.api.data.entity.Customer;
import org.example.api.data.entity.Transfer;
import org.example.apicalls.dto.AccountDTO;
import org.example.apicalls.dto.CardDTO;
import org.example.apicalls.dto.CustomerDTO;
import org.example.apicalls.dto.TransferDTO;

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

  // Strings

  public static String generateRandomString(int length) {
    Random random = new Random();
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      int randomIndex = random.nextInt(CHARACTERS.length());
      sb.append(CHARACTERS.charAt(randomIndex));
    }
    return sb.toString();
  }

  public static String generateRandomGmail(int length){
    return generateRandomString(length).concat(GMAIL_DOMAIN);
  }

  public static String generateRandomPassword(int length){
    return generateRandomString(length).concat(String.valueOf(generateRandomInt(1, 3)));
  }

  public static String randomlyChooseFrom(String opt1, String opt2){
    String[] strings = {opt1, opt2};
    Random random = new Random();
    return strings[random.nextInt(strings.length)];
  }

  public static String generateRandomTransferStatus(){
    String[] strings = {"PENDING", "FAILED", "SUCCESSFUL"};
    Random random = new Random();
    return strings[random.nextInt(strings.length)];
  }

  // Numbers

  public static int generateRandomInt(int min, int max) {
    Random random = new Random();
    return random.nextInt((max - min) + 1) + min;
  }

  public static double generateRandomDouble(int min, int max){
    Random r = new Random();
      return min + (max - min) * r.nextDouble();
  }

  public static long generateRandomCardNumber(){
    return ThreadLocalRandom.current().nextLong(minLong, maxLong + 1);
  }

  // Dates

  public static Date generateRandomFutureDate() {
    LocalDate startDate = LocalDate.now();
    long startMillis = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    long endMillis = startDate.plusYears(5).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    long randomMillis = ThreadLocalRandom.current().nextLong(startMillis, endMillis + 1);
    return new Date(randomMillis);
  }

  public static Date generateLocalDate() {
    LocalDateTime now = LocalDateTime.now();
    return Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
  }

  // Auxiliary

  public static boolean generateRandomBoolean(){
    Random random = new Random();
    return random.nextBoolean();
  }

  public static <T extends Enum<?>> T generateRandomEnum(Class<T> enumClass) {
    T[] enumValues = enumClass.getEnumConstants();
    int randomIndex = new Random().nextInt(enumValues.length);
    return enumValues[randomIndex];
  }

  // Generate Random Entities and DTOs

  public static Account generateRandomAccount(int nCards){
    Account account = new Account();
    List<Card> cards = new java.util.ArrayList<>(List.of());
    int n = 0;

    if(nCards < 0){
      throw new IllegalArgumentException("The number of Cards must be higher than 0!");
    }

    while(n != nCards){
      cards.add(generateRandomCard());
      n++;
    }

    account.setAccountType(randomlyChooseFrom("Savings", "Checking"));
    account.setIsBlocked(generateRandomBoolean());
    account.setIsInDebt(generateRandomBoolean());
    account.setAmount(generateRandomDouble(minAmount, maxAmount));
    account.setCreationDate(generateLocalDate());
    account.setExpirationDate(generateRandomFutureDate());
    account.setCards(cards);

    return account;
  }

  public static AccountDTO generateRandomAccountDTO(int nCards){
    AccountDTO account = new AccountDTO();
    List<CardDTO> cards = new java.util.ArrayList<>(List.of());
    int n = 0;

    if(nCards < 0){
      throw new IllegalArgumentException("The number of Cards must be higher than 0!");
    }

    while(n != nCards){
      cards.add(generateRandomCardDTO());
      n++;
    }
    account.setAccountType(randomlyChooseFrom("Savings", "Checking"));
    account.setIsBlocked(generateRandomBoolean());
    account.setIsInDebt(generateRandomBoolean());
    account.setAmount(generateRandomDouble(minAmount, maxAmount));
    account.setCreationDate(generateLocalDate());
    account.setExpirationDate(generateRandomFutureDate());
    account.setCards(cards);


    return account;
  }

  public static Card generateRandomCard(){
    Card card = new Card();
    card.setType(randomlyChooseFrom("Credit", "Debit"));
    card.setCvc(generateRandomInt(3, 3));
    card.setNumber(generateRandomCardNumber());
    card.setExpirationDate(generateRandomFutureDate());

    return card;
  }

  public static CardDTO generateRandomCardDTO(){
    CardDTO card = new CardDTO();
    card.setType(randomlyChooseFrom("Credit", "Debit"));
    card.setCvc(generateRandomInt(3, 3));
    card.setNumber(generateRandomCardNumber());
    card.setExpirationDate(generateRandomFutureDate());

    return card;
  }

  public static Customer generateRandomCustomer(int nCards, int nAccounts){
    Customer customer = new Customer();
    List<Account> accounts = new java.util.ArrayList<>(List.of());
    int n = 0;

    if(nAccounts < 0){
      throw new IllegalArgumentException("The number of Accounts must be higher than 0!");
    }

    while(n != nAccounts){
      accounts.add(generateRandomAccount(nCards));
      n++;
    }

    customer.setName(generateRandomString(nameLength));
    customer.setSurname(generateRandomString(nameLength));
    customer.setEmail(generateRandomGmail(nameLength));
    customer.setPassword(generateRandomPassword(passwordLength));
    customer.setAccounts(accounts);

    return customer;
  }

  public static CustomerDTO generateRandomCustomerDTO(int nCards, int nAccounts){
    CustomerDTO customer = new CustomerDTO();
    List<AccountDTO> accounts = new java.util.ArrayList<>(List.of());
    int n = 0;

    if(nAccounts < 0){
      throw new IllegalArgumentException("The number of Accounts must be higher than 0!");
    }

    while(n != nAccounts){
      accounts.add(generateRandomAccountDTO(nCards));
      n++;
    }

    customer.setName(generateRandomString(nameLength));
    customer.setSurname(generateRandomString(nameLength));
    customer.setEmail(generateRandomGmail(nameLength));
    customer.setPassword(generateRandomPassword(passwordLength));
    customer.setAccounts(accounts);

    return customer;
  }

  public static Transfer generateRandomTransfer(Account origin, Account receiver){
    Transfer transfer = new Transfer();
    Transfer.TransferStatus transferStatus = generateRandomEnum(Transfer.TransferStatus.class);
    Transfer.CurrencyType currencyType = generateRandomEnum(Transfer.CurrencyType.class);

    transfer.setTransferAmount(generateRandomDouble(minAmount, maxAmount));
    transfer.setCurrencyType(currencyType);
    transfer.setTransferStatus(transferStatus);
    transfer.setTransferDate(generateLocalDate());
    transfer.setOriginAccount(origin);
    transfer.setReceivingAccount(receiver);

    return transfer;
  }

  public static TransferDTO generateRandomTransferDTO(Account origin, Account receiver){
    TransferDTO transfer = new TransferDTO();

    transfer.setTransferAmount(generateRandomDouble(minAmount, maxAmount));
    transfer.setCurrencyType(randomlyChooseFrom("USD", "EUR"));
    transfer.setTransferStatus(generateRandomTransferStatus());
    transfer.setTransferDate(generateLocalDate());
    transfer.setOriginAccountId(origin.getAccountId());
    transfer.setReceivingAccountId(receiver.getAccountId());

    return transfer;
  }
}
