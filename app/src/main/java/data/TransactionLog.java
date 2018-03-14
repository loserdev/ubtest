package data;


import java.util.ArrayList;
import java.util.List;

public class TransactionLog {

  private String transactionId;
  private int amount;
  private String date;
  private String merchantName;
  private String transactionType;

  public String getTransactionType() {
    return transactionType;
  }

  public void setTransactionType(String transactionType) {
    this.transactionType = transactionType;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getMerchantName() {
    return merchantName;
  }

  public void setMerchantName(String merchantName) {
    this.merchantName = merchantName;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }




}
