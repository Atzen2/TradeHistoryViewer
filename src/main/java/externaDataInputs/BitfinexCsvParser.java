package externaDataInputs;

import java.io.File;
import java.io.FileFilter;
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

public class BitfinexCsvParser extends EasyCsvHandler implements ExternalDataCollector{

	private final String EXCHANGE_NAME = "bitfinex";

	private final String CSV_PATH = "./resources/external/bitfinex/";
	
	private final String CSV_TRADE_FILE_SUFFIX = "trades.csv";
	private final String CSV_DEPOSIT_FILE_SUFFIX = "deposits.csv";
	private final String CSV_WITHDRAWAL_FILE_SUFFIX = "withdrawals.csv";
	
	private final String REFID = "#";  
	private final String PAIR = "Pair";   
	private final String AMOUNT = "Amount"; 
	private final String PRICE = "Price"; 
	private final String FEE = "Fee";
	private final String FEE_CURRENCY = "FeeCurrency";
	private final String DATE = "Date";
	
	private final String CURRENCY = "Currency";
	private final String METHOD = "Method";
	private final String STATUS = "Status";
	private final String ADDRESS = "Address";
	private final String TXID = "TXID";
	private final String CREATED = "Created";
	private final String UPDATED = "Updated";
	
	private int REFID_INDEX = 0;
	private int PAIR_INDEX = 0;
	private int AMOUNT_INDEX = 0; 
	private int PRICE_INDEX = 0;
	private int FEE_INDEX = 0;
	private int FEE_CURRENCY_INDEX = 0;
	private int DATE_INDEX = 0;
	
	private int CURRENCY_INDEX = 0;
	private int METHOD_INDEX = 0;
	private int STATUS_INDEX = 0;
	private int ADDRESS_INDEX = 0;
	private int TXID_INDEX = 0;
	private int CREATED_INDEX = 0;
	private int UPDATED_INDEX = 0;

	private List<Trade> tradeList = new ArrayList<>();
	private List<Deposit> depositList = new ArrayList<>();
	private List<Withdrawal> withdrawalList = new ArrayList<>();


	
	@Override
	public void collectInputData() {

		String tradeFilePathName = getFilePath(CSV_TRADE_FILE_SUFFIX);
		String depositFilePathName = getFilePath(CSV_DEPOSIT_FILE_SUFFIX);
		String withdrawalFilePathName = getFilePath(CSV_WITHDRAWAL_FILE_SUFFIX);
		
		
		if(tradeFilePathName != null) {
			try {
				
				EasyCsvFile csvFile = parseCsvFile(tradeFilePathName);
				setHeaderIndexes(csvFile);
				
				addTradesToTradeList(csvFile.records);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(depositFilePathName != null) {
			try {
				
				EasyCsvFile csvFile = parseCsvFile(depositFilePathName);
				setHeaderIndexes(csvFile);
				
				addDepositsToDepositList(csvFile.records);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(withdrawalFilePathName != null) {
			try {
				
				EasyCsvFile csvFile = parseCsvFile(withdrawalFilePathName);
				setHeaderIndexes(csvFile);
				
				addWithdrawalsToWithdrawalList(csvFile.records);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	private String getFilePath(String fileSuffix) {

		File[] files = new File(CSV_PATH).listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				return doesFileNameContainsSuffix(pathname, fileSuffix) && isFileNotHidden(pathname);
			}
		});

		
		if(files == null) return null;
		return files[0].getAbsolutePath();
	}
	
	private boolean isFileNotHidden(File file) {
		return !file.isHidden();
	}
	
	private boolean doesFileNameContainsSuffix(File file, String fileSuffix) {
		return file.getName().contains(fileSuffix);
	}
	
	private void setHeaderIndexes(EasyCsvFile csvFile) {
		REFID_INDEX = getHeaderIndex(csvFile, REFID);
		PAIR_INDEX = getHeaderIndex(csvFile, PAIR);
		AMOUNT_INDEX = getHeaderIndex(csvFile, AMOUNT);
		PRICE_INDEX = getHeaderIndex(csvFile, PRICE);
		FEE_INDEX = getHeaderIndex(csvFile, FEE);
		FEE_CURRENCY_INDEX = getHeaderIndex(csvFile, FEE_CURRENCY); 
		DATE_INDEX = getHeaderIndex(csvFile, DATE);
		
		CURRENCY_INDEX = getHeaderIndex(csvFile, CURRENCY);
		METHOD_INDEX = getHeaderIndex(csvFile, METHOD);
		STATUS_INDEX = getHeaderIndex(csvFile, STATUS);
		ADDRESS_INDEX = getHeaderIndex(csvFile, ADDRESS);
		TXID_INDEX = getHeaderIndex(csvFile, TXID);
		CREATED_INDEX = getHeaderIndex(csvFile, CREATED);
		UPDATED_INDEX = getHeaderIndex(csvFile, UPDATED);
	}
	
	private void addTradesToTradeList(List<String[]> records) {
		for(String[] record: records) tradeList.add(createTrade(record));
	}
	
	private Trade createTrade(String[] record) {
		Trade trade = new Trade();
		
		trade.exchange = EXCHANGE_NAME;
		trade.timestamp = convertDateToTimestamp(record[DATE_INDEX]);
		trade.fee = new Asset(record[FEE_CURRENCY_INDEX], Math.abs(Float.parseFloat(record[FEE_INDEX])));
		
		int baseIndex = 0;
		int quoteIndex = 1;
		String [] pair = getPairs(record[PAIR_INDEX]);
		trade.price = new Price(Asset.convertStringToAssetType(pair[baseIndex]), Asset.convertStringToAssetType(pair[quoteIndex]), Float.parseFloat(record[PRICE_INDEX]));

		trade.bought = new Asset(trade.price.base, Float.parseFloat(record[AMOUNT_INDEX]));
		trade.sold = new Asset(trade.price.quote, calculateSoldAmount(trade));
				
		if(isFeePayedInBoughtAsset(trade)) trade = convertFeeToSoldAsset(trade);
		
		return trade;
	}
	
	private long convertDateToTimestamp(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return LocalDateTime.parse(date, formatter).toInstant(ZoneOffset.UTC).getEpochSecond();
	}
	
	private String[] getPairs(String pair) {
		return pair.split("/");
	}
	
	private float calculateSoldAmount(Trade trade) {
		return trade.price.value * trade.bought.amount;
	}
	
	private boolean isFeePayedInBoughtAsset(Trade trade) {
		return trade.fee.type.equals(trade.bought.type);
	}
	
	private Trade convertFeeToSoldAsset(Trade trade) {
		trade.fee.amount = trade.fee.amount * trade.price.value;
		trade.fee.type = trade.sold.type;
		
		return trade;
	}
	
	private void addDepositsToDepositList(List<String[]> records) {
		for(String[] record: records) depositList.add(createDeposit(record));
	}
	
	private Deposit createDeposit(String[] record) {
		Deposit deposit = new Deposit();
		
		deposit.exchange = EXCHANGE_NAME;
		deposit.timestamp = convertDateToTimestamp(record[UPDATED_INDEX]);
		deposit.asset = new Asset(Asset.convertStringToAssetType(record[CURRENCY_INDEX]), Float.parseFloat(record[AMOUNT_INDEX]));
		
		return deposit;
	}
	
	private void addWithdrawalsToWithdrawalList(List<String[]> records) {
		for(String[] record: records) withdrawalList.add(createWithdrawal(record));
	}
	
	private Withdrawal createWithdrawal(String[] record) {
		Withdrawal withdrawal = new Withdrawal();
		
		withdrawal.exchange = EXCHANGE_NAME;
		withdrawal.timestamp = convertDateToTimestamp(record[UPDATED_INDEX]);
		withdrawal.asset = new Asset(Asset.convertStringToAssetType(record[CURRENCY_INDEX]), Math.abs(Float.parseFloat(record[AMOUNT_INDEX])));
		
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



