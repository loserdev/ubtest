package data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fahad on 3/8/2018.
 */

public class TransactionLogList {

  public static List<TransactionLog> getAllTransactionLog(){
    List<TransactionLog> tempTransLogList = new ArrayList<TransactionLog>();
    TransactionLog tempTransLog = new TransactionLog();
    tempTransLog.setAmount(10);
    tempTransLog.setDate("12 Jan 2018");
    tempTransLog.setMerchantName("Agora");
    tempTransLogList.add(tempTransLog);

    tempTransLog = new TransactionLog();
    tempTransLog.setAmount(800);
    tempTransLog.setDate("10 Feb 2018");
    tempTransLog.setMerchantName("Mena Bazar");
    tempTransLogList.add(tempTransLog);

    tempTransLog = new TransactionLog();
    tempTransLog.setAmount(50);
    tempTransLog.setDate("14 Feb 2018");
    tempTransLog.setMerchantName("Agora");
    tempTransLogList.add(tempTransLog);

    tempTransLog = new TransactionLog();
    tempTransLog.setAmount(90);
    tempTransLog.setDate("15 March 2018");
    tempTransLog.setMerchantName("Shoopers World");
    tempTransLogList.add(tempTransLog);

    tempTransLog = new TransactionLog();
    tempTransLog.setAmount(750);
    tempTransLog.setDate("29 Dec 2017");
    tempTransLog.setMerchantName("7 Eleven");
    tempTransLogList.add(tempTransLog);

    tempTransLog = new TransactionLog();
    tempTransLog.setAmount(350);
    tempTransLog.setDate("29 Jan 2018");
    tempTransLog.setMerchantName("Shopno");
    tempTransLogList.add(tempTransLog);

    tempTransLog = new TransactionLog();
    tempTransLog.setAmount(3950);
    tempTransLog.setDate("20 Jan 2018");
    tempTransLog.setMerchantName("7X24 Shop");
    tempTransLogList.add(tempTransLog);

    tempTransLog = new TransactionLog();
    tempTransLog.setAmount(750);
    tempTransLog.setDate("29 Dec 2017");
    tempTransLog.setMerchantName("7 Eleven");
    tempTransLogList.add(tempTransLog);

    tempTransLog = new TransactionLog();
    tempTransLog.setAmount(350);
    tempTransLog.setDate("29 Jan 2018");
    tempTransLog.setMerchantName("Shopno");
    tempTransLogList.add(tempTransLog);

    tempTransLog = new TransactionLog();
    tempTransLog.setAmount(3950);
    tempTransLog.setDate("20 Jan 2018");
    tempTransLog.setMerchantName("7X24 Shop");
    tempTransLogList.add(tempTransLog);

    return tempTransLogList;
  }

}
