package data;

import java.util.ArrayList;
import java.util.List;


public class TransactionLogList {

  public static List<TransactionLog> getAllTransactionLog(){
    List<TransactionLog> tempTransLogList = new ArrayList<TransactionLog>();
    TransactionLog tempTransLog = new TransactionLog();
    tempTransLog.setAmount(10);
    tempTransLog.setDate("12 Jan 2018");
    tempTransLog.setMerchantName("Agora");
    tempTransLog.setTransactionId("9089765431");
    tempTransLog.setTransactionType("Grocery");
    tempTransLogList.add(tempTransLog);

    tempTransLog = new TransactionLog();
    tempTransLog.setAmount(800);
    tempTransLog.setDate("10 Feb 2018");
    tempTransLog.setMerchantName("Mena Bazar");
    tempTransLog.setTransactionId("9082335431");
    tempTransLog.setTransactionType("Grocery");
    tempTransLogList.add(tempTransLog);

    tempTransLog = new TransactionLog();
    tempTransLog.setAmount(50);
    tempTransLog.setDate("14 Feb 2018");
    tempTransLog.setMerchantName("Pathao");
    tempTransLog.setTransactionId("4509765431");
    tempTransLog.setTransactionType("Transport");
    tempTransLogList.add(tempTransLog);

    tempTransLog = new TransactionLog();
    tempTransLog.setAmount(90);
    tempTransLog.setDate("15 March 2018");
    tempTransLog.setMerchantName("Uber BD");
    tempTransLog.setTransactionId("1144765431");
    tempTransLog.setTransactionType("Transport");
    tempTransLogList.add(tempTransLog);

    tempTransLog = new TransactionLog();
    tempTransLog.setAmount(750);
    tempTransLog.setDate("29 Dec 2017");
    tempTransLog.setMerchantName("7 Eleven");
    tempTransLog.setTransactionId("1122339900");
    tempTransLog.setTransactionType("Grocery");
    tempTransLogList.add(tempTransLog);

    tempTransLog = new TransactionLog();
    tempTransLog.setAmount(350);
    tempTransLog.setDate("29 Jan 2018");
    tempTransLog.setMerchantName("Shopno");
    tempTransLog.setTransactionId("99447722431");
    tempTransLog.setTransactionType("Grocery");
    tempTransLogList.add(tempTransLog);

    tempTransLog = new TransactionLog();
    tempTransLog.setAmount(3950);
    tempTransLog.setDate("20 Jan 2018");
    tempTransLog.setMerchantName("7X24 Shop");
    tempTransLog.setTransactionId("33660065431");
    tempTransLog.setTransactionType("Grocery");
    tempTransLogList.add(tempTransLog);

    tempTransLog = new TransactionLog();
    tempTransLog.setAmount(750);
    tempTransLog.setDate("29 Dec 2017");
    tempTransLog.setMerchantName("7 Eleven");
    tempTransLog.setTransactionId("4477765431");
    tempTransLog.setTransactionType("Grocery");
    tempTransLogList.add(tempTransLog);

    tempTransLog = new TransactionLog();
    tempTransLog.setAmount(350);
    tempTransLog.setDate("29 Jan 2018");
    tempTransLog.setMerchantName("Shopno");
    tempTransLog.setTransactionId("5522991122");
    tempTransLog.setTransactionType("Grocery");
    tempTransLogList.add(tempTransLog);

    tempTransLog = new TransactionLog();
    tempTransLog.setAmount(3950);
    tempTransLog.setDate("20 Jan 2018");
    tempTransLog.setMerchantName("7X24 Shop");
    tempTransLog.setTransactionId("3388115531");
    tempTransLog.setTransactionType("Grocery");
    tempTransLogList.add(tempTransLog);

    return tempTransLogList;
  }

}
