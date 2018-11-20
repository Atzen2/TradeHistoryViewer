package externaDataInputs;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import dataTypes.Asset;
import dataTypes.Deposit;
import dataTypes.Price;
import dataTypes.Trade;
import dataTypes.Withdrawal;
import easyCsvHandler.EasyCsvFile;
import easyCsvHandler.EasyCsvHandler;

public class HitbtcCsvParser extends EasyCsvHandler implements ExternalDataCollector {
	
	private final String EXCHANGE_NAME = "hitbtc";

	private final String CSV_PATH = "./resources/external/hitbtc/";
	
	private final String CSV_TRADE_FILE = "trades.csv";
	private final String CSV_PAYMENT_FILE = "payment_history.csv";
	
	private final String DATE = "Date (UTC)";   
	private final String INSTRUMENT = "Instrument";
	private final String TRADEID = "Trade ID";  
	private final String ORDERID = "Order ID";   
	private final String SIDE = "Side"; 
	private final String QUANTITY = "Quantity";  
	private final String PRICE = "Price"; 
	private final String VOLUME = "Volume";
	private final String FEE = "Fee";
	private final String REBATE = "Rebate";
	private final String TOTAL = "Total";

	private final String OPERATIONID = "Operation id";
	private final String TYPE = "Type";
	private final String AMOUNT = "Amount";
	private final String TRANSACTIONHASH = "Transaction Hash";
	private final String MAINACCOUNTBALANCE = "Main account balance";
	private final String ASSET = "Asset";
	
	private final String DEPOSIT = "Deposit";
	private final String WITHDRAWAL = "Withdraw";
	private final String BUY = "buy";
	private final String SELL = "sell";
	
	private int DATE_INDEX = 0;   
	private int INSTRUMENT_INDEX = 0;
	private int TRADEID_INDEX = 0;  
	private int ORDERID_INDEX = 0;   
	private int SIDE_INDEX = 0; 
	private int QUANTITY_INDEX = 0;  
	private int PRICE_INDEX = 0; 
	private int VOLUME_INDEX = 0;
	private int FEE_INDEX = 0;
	private int REBATE_INDEX = 0;
	private int TOTAL_INDEX = 0;
	
	private int OPERATIONID_INDEX = 0;
	private int TYPE_INDEX = 0;
	private int AMOUNT_INDEX = 0;
	private int TRANSACTIONHASH_INDEX = 0;
	private int MAINACCOUNTBALANCE_INDEX = 0;
	private int ASSET_INDEX = 0;
	
	private List<Trade> tradeList = new ArrayList<>();
	private List<Deposit> depositList = new ArrayList<>();
	private List<Withdrawal> withdrawalList = new ArrayList<>();
	
	
	
	@Override
	public void collectInputData() {
		String tradeFilePathName = getFilePath(CSV_TRADE_FILE);
		String paymentFilePathName = getFilePath(CSV_PAYMENT_FILE);
		
		
		if(tradeFilePathName != null) {
			try {
				
				EasyCsvFile csvFile = parseCsvFile(tradeFilePathName);
				setHeaderIndexes(csvFile);
				
				addTradesToTradeList(csvFile.records);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(paymentFilePathName != null) {
			try {
				
				EasyCsvFile csvFile = parseCsvFile(paymentFilePathName);
				setHeaderIndexes(csvFile);
				
				addPaymentsToPaymentLists(csvFile.records);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	
	private String getFilePath(String fileName) {
		return new File(CSV_PATH + fileName).getAbsolutePath();
	}
	
	private void setHeaderIndexes(EasyCsvFile csvFile) {
		DATE_INDEX = getHeaderIndex(csvFile, DATE);
		INSTRUMENT_INDEX = getHeaderIndex(csvFile, INSTRUMENT);
		TRADEID_INDEX = getHeaderIndex(csvFile, TRADEID);
		ORDERID_INDEX = getHeaderIndex(csvFile, ORDERID);
		SIDE_INDEX = getHeaderIndex(csvFile, SIDE);
		QUANTITY_INDEX = getHeaderIndex(csvFile, QUANTITY);
		PRICE_INDEX = getHeaderIndex(csvFile, PRICE);
		VOLUME_INDEX = getHeaderIndex(csvFile, VOLUME);
		FEE_INDEX = getHeaderIndex(csvFile, FEE);
		REBATE_INDEX = getHeaderIndex(csvFile, REBATE);
		TOTAL_INDEX = getHeaderIndex(csvFile, TOTAL);
		
		OPERATIONID_INDEX = getHeaderIndex(csvFile, OPERATIONID);
		TYPE_INDEX = getHeaderIndex(csvFile, TYPE);
		AMOUNT_INDEX = getHeaderIndex(csvFile, AMOUNT);
		TRANSACTIONHASH_INDEX = getHeaderIndex(csvFile, TRANSACTIONHASH);
		MAINACCOUNTBALANCE_INDEX = getHeaderIndex(csvFile, MAINACCOUNTBALANCE);
		ASSET_INDEX = getHeaderIndex(csvFile, ASSET);
	}
	
	private void addTradesToTradeList(List<String[]> records) {
		for(String[] record: records) tradeList.add(createTrade(record));
	}
	
	private Trade createTrade(String[] record) {
		Trade trade = new Trade();
		
		trade.exchange = EXCHANGE_NAME;
		trade.timestamp = convertDateToTimestamp(record[DATE_INDEX]);

		
		int baseIndex = 0;
		int quoteIndex = 1;
		String [] pair = getPairs(record[INSTRUMENT_INDEX]);
		trade.price = new Price(Asset.convertStringToAssetType(pair[baseIndex]), Asset.convertStringToAssetType(pair[quoteIndex]), Float.parseFloat(record[PRICE_INDEX]));

		
		if(record[SIDE_INDEX].equals(BUY)) {
			trade.bought = new Asset(trade.price.base, Float.parseFloat(record[QUANTITY_INDEX]));
			trade.sold = new Asset(trade.price.quote, Float.parseFloat(record[VOLUME_INDEX]));	
		}
		
		if(record[SIDE_INDEX].equals(SELL)) {
			trade.bought = new Asset(trade.price.quote, Float.parseFloat(record[VOLUME_INDEX]));	
			trade.sold = new Asset(trade.price.base, Float.parseFloat(record[QUANTITY_INDEX]));
		}
		
		
		trade.fee = new Asset(record[TOTAL_INDEX], Math.abs(Float.parseFloat(record[TOTAL_INDEX])));
		
		return trade;
	}
	
	private long convertDateToTimestamp(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		date = adjustDateString(date);
		return LocalDateTime.parse(date, formatter).toInstant(ZoneOffset.UTC).getEpochSecond();
	}
	
	private String adjustDateString(String date) {
		String[] dateTime = date.split(" "); // dateTime: [yyy-MMM-dd, (H)H:mm:ss]
		String[] time = dateTime[1].split(":"); // time : [(H)H, mm, ss]
		String hour = time[0]; // hour : (H)H
		
		hour = (hour.length() == 1) ? "0" + hour : hour; // hour: HH
		
		return dateTime[0] + " " + hour + ":" + time[1] + ":" + time[2]; // yyy-MMM-dd HH:mm:ss
	}
	
	private String[] getPairs(String pair) {
		return pair.split("/");
	}
	
	private void addPaymentsToPaymentLists(List<String[]> records) {
		for(String[] record : records) {
			if(record[TYPE_INDEX].equals(DEPOSIT)) depositList.add(createDeposit(record));
			if(record[TYPE_INDEX].equals(WITHDRAWAL)) withdrawalList.add(createWithdrawal(record));
		}
	}
	
	private Deposit createDeposit(String[] record) {
		Deposit deposit = new Deposit();
		deposit.exchange = EXCHANGE_NAME;
		deposit.timestamp = convertDateToTimestamp(record[DATE_INDEX]); 
		deposit.asset = new Asset(Asset.convertStringToAssetType(record[ASSET_INDEX]), Math.abs(Float.parseFloat(record[AMOUNT_INDEX])));
		
		return deposit;
	}
	
	private Withdrawal createWithdrawal(String[] record) {
		Withdrawal withdrawal = new Withdrawal();
		withdrawal.exchange = EXCHANGE_NAME;
		withdrawal.timestamp = convertDateToTimestamp(record[DATE_INDEX]); 
		withdrawal.asset = new Asset(Asset.convertStringToAssetType(record[ASSET_INDEX]), Math.abs(Float.parseFloat(record[AMOUNT_INDEX])));
		
		return withdrawal;
	}
	
	
	
	@Override
	public List<Trade> getTradeList() {
		return tradeList;
	}

	@Override
	public List<Deposit> getDepositList() {
		return depositList;
	}

	@Override
	public List<Withdrawal> getWithdrawalList() {
		return withdrawalList;
	}

}
