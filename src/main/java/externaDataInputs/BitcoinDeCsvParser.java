package externaDataInputs;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import dataTypes.Deposit;
import dataTypes.Trade;
import dataTypes.Withdrawal;
import easyCsvHandler.EasyCsvHandler;

public class BitcoinDeCsvParser extends EasyCsvHandler implements ExternalDataCollector{
	
	private final String EXCHANGE_NAME = "bitcoin.de";
	
	private final String CSV_PATH = "./resources/external/bitcoin.de/";
	
	private final String CSV_FILE_SUFFIX = "account_statement.csv";

	
	private final String DATE = "Date";
	private final String TYPE = "Type";
	private final String CURRENCIES = "Currencies";
	private final String REFERENCE = "Reference";
	private final String PRICE = "Price";
	private final String BTC_INC_FEE = "BTC incl. fee";	
	private final String BTC_EXC_FEE = "BTC excl. Bitcoin.de fee";
	private final String EUR_INC_FEE = "EUR incl. fee";
	private final String EUR_EXC_FEE = "EUR excl. Bitcoin.de fee";
	private final String IN_OUT = "Incoming / Outgoing";
	private final String ACC_BALANCE = "Account balance";

	private final String REGISTRATION = "Registration";
	private final String PURCHASE = "Purchase";
	private final String SALE = "Sale";
	private final String DISBURSEMENT = "Disbursement";
	private final String NETWORK_FEE = "Network fee";
	
	
	private int DATE_INDEX = 0;
	private int TYPE_INDEX = 0;
	private int CURRENCIES_INDEX = 0;
	private int REFERENCE_INDEX = 0;
	private int PRICE_INDEX = 0;
	private int BTC_INC_FEE_INDEX = 0;	
	private int BTC_EXC_FEE_INDEX = 0;
	private int EUR_INC_FEE_INDEX = 0;
	private int EUR_EXC_FEE_INDEX = 0;
	private int IN_OUT_INDEX = 0;
	private int ACC_BALANCE_INDEX = 0;

//	private int TXID_INDEX = 0;
//	private int REFID_INDEX = 0;  
//	private int TIME_INDEX = 0;   
//	private int TYPE_INDEX = 0;   
//	private int ACLASS_INDEX = 0; 
//	private int ASSET_INDEX = 0;  
//	private int AMOUNT_INDEX = 0; 
//	private int FEE_INDEX = 0;    
//	private int BALANCE_INDEX = 0;
//	
//	private String tradeRefId = "";
//	private Trade trade = new Trade();
//	
	private List<Trade> tradeList = new ArrayList<>();
	private List<Deposit> depositList = new ArrayList<>();
	private List<Withdrawal> withdrawalList = new ArrayList<>();
//
//	
//	
	@Override
	public void collectInputData() {
//		
//		try {
//			EasyCsvFile csvFile = parseCsvFile(CSV_FILE_PATH);
//			setHeaderIndexes(csvFile);
//			
//			
//			for(String[] record : csvFile.records) {
//				String type = record[TYPE_INDEX]; 
//				
//				switch(type) {
//				
//					case DEPOSIT:
//						depositList.add(createDeposit(record));
//						break;
//						
//						
//					case WITHDRAWAL:
//						withdrawalList.add(createWithdrawal(record));
//						break;
//					
//						
//					case TRADE:
//						if(createTrade(record)) {
//							calculatePrice();
//							
//							if(isFeePayedInBoughtAsset()) convertFeeToSoldAsset();
//							
//							tradeList.add(trade);
//						}
//						break;
//					
//					default:
//						break;
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
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
	
	
//	
//	private void setHeaderIndexes(EasyCsvFile csvFile) {
//		TXID_INDEX = getHeaderIndex(csvFile, TXID);
//		REFID_INDEX = getHeaderIndex(csvFile, REFID);
//		TIME_INDEX = getHeaderIndex(csvFile, TIME);
//		TYPE_INDEX = getHeaderIndex(csvFile, TYPE);
//		ACLASS_INDEX = getHeaderIndex(csvFile, ACLASS);
//		ASSET_INDEX = getHeaderIndex(csvFile, ASSET);
//		AMOUNT_INDEX = getHeaderIndex(csvFile, AMOUNT);
//		FEE_INDEX = getHeaderIndex(csvFile, FEE);
//		BALANCE_INDEX = getHeaderIndex(csvFile, BALANCE);
//	}
//	
//	private Deposit createDeposit(String[] record) {
//		Deposit deposit = new Deposit();
//		deposit.asset = new Asset(getAssetType(record[ASSET_INDEX]), Float.parseFloat(record[AMOUNT_INDEX]));
//		deposit.fee = new Asset(getAssetType(record[ASSET_INDEX]), Float.parseFloat(record[FEE_INDEX]));
//		deposit.timestamp = convertDateToTimestamp(record[TIME_INDEX]);
//		deposit.exchange = EXCHANGE_NAME;
//		
//		return deposit;
//	}
//	
//	private long convertDateToTimestamp(String date) {
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//		return LocalDateTime.parse(date, formatter).toInstant(ZoneOffset.UTC).getEpochSecond();
//	}
//	
//	private AssetType getAssetType(String assetType) {
//		switch(assetType) {
//			case "XXBT": return AssetType.BTC;
//			case "XETH": return AssetType.ETH;
//			case "ZEUR": return AssetType.EUR;
//			case "XDAO": return AssetType.DAO;
//			case "XETC": return AssetType.ETC;
//			case "XREP": return AssetType.REP;
//			case "XZEC": return AssetType.ZEC;
//			case "XXMR": return AssetType.XMR;
//			case "XLTC": return AssetType.LTC;
//			case "BCH": return AssetType.BCH;
//			default: return AssetType.NONE;
//		}
//	}
//	
//	private Withdrawal createWithdrawal(String[] record) {
//		Withdrawal withdrawal = new Withdrawal();
//		withdrawal.asset = new Asset(getAssetType(record[ASSET_INDEX]), -Float.parseFloat(record[AMOUNT_INDEX]));
//		withdrawal.fee = new Asset(getAssetType(record[ASSET_INDEX]), Float.parseFloat(record[FEE_INDEX]));
//		withdrawal.timestamp = convertDateToTimestamp(record[TIME_INDEX]);
//		withdrawal.exchange = EXCHANGE_NAME;
//		
//		return withdrawal;
//	}
//	
//	private boolean createTrade(String[] record) {
//		String refId = record[REFID_INDEX];
//		boolean isTradeParsed = false;
//		
//		
//		long time = convertDateToTimestamp(record[TIME_INDEX]);
//		Asset asset = new Asset(getAssetType(record[ASSET_INDEX]), Float.parseFloat(record[AMOUNT_INDEX]));
//		Asset fee = new Asset(getAssetType(record[ASSET_INDEX]), Float.parseFloat(record[FEE_INDEX]));
//		
//		
//		if(!tradeRefId.equals(refId)) { // new trade
//			
//			trade = new Trade();
//			trade.fee = new Asset(AssetType.NONE, 0);
//			trade.exchange = EXCHANGE_NAME;
//			
//			tradeRefId = refId;
//			isTradeParsed = false;
//		
//		} else { // same trade
//		
//			trade.timestamp = time;
//			isTradeParsed = true;
//		}
//			
//			
//		if(asset.amount >=0) { // bought 
//			
//			trade.bought = asset;
//			if(fee.amount > 0) trade.fee = fee;
//			
//		} else { // sold
//			
//			asset.amount = Math.abs(asset.amount);
//			trade.sold = asset;
//			if(fee.amount > 0) trade.fee = fee;
//
//		}
//
//		return isTradeParsed;
//	}
//	
//	private void calculatePrice() {
//		trade.price = new Price(trade.bought, trade.sold);
//	}
//	
//	private boolean isFeePayedInBoughtAsset() {
//		return trade.fee.type.equals(trade.bought.type);
//	}
//	
//	private void convertFeeToSoldAsset() {
//		trade.fee.amount = trade.fee.amount * trade.price.value;
//		trade.fee.type = trade.sold.type;
//	}
//
//
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
