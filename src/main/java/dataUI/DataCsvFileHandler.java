package dataUI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dataTypes.Asset;
import dataTypes.Deposit;
import dataTypes.Price;
import dataTypes.Trade;
import dataTypes.TradingElement;
import dataTypes.Withdrawal;
import easyCsvHandler.EasyCsvFile;
import easyCsvHandler.EasyCsvHandler;
import externaDataInputs.ExternalDataCollector.AssetType;
import priceProvider.PriceProvider;

public class DataCsvFileHandler extends EasyCsvHandler implements DataUI {

	private final String FILE_PATH = "./resources/internal/tradingElements.csv";
	
	private final String TIME = "time";
	private final String TYPE = "type";
	private final String EXCHANGE = "exchange";
	private final String ASSET = "asset";
	private final String BOUGHT = "bought";
	private final String SOLD = "sold";
	private final String FEE = "fee";
	private final String PRICE = "price";
	private final String FIAT = "fiat price [sold asset / fiat]";
	
	private final String[] header = {TIME, TYPE, EXCHANGE, ASSET, BOUGHT, SOLD, FEE, PRICE, FIAT};

	private int TIME_INDEX;
	private int TYPE_INDEX;
	private int EXCHANGE_INDEX;
	private int ASSET_INDEX;
	private int BOUGHT_INDEX;
	private int SOLD_INDEX;
	private int FEE_INDEX;
	private int PRICE_INDEX;
	private int FIAT_INDEX;
	
	private final int NUMOFHEADERELEMENTS = 9;
	
	private final String DEFAULT_STRING = "-";
	
	
	
	public DataCsvFileHandler() {
		
	}
	
	
	private void getHeaderIdexes() {
		TIME_INDEX = getHeaderIndex(header, TIME);
		TYPE_INDEX = getHeaderIndex(header, TYPE);
		EXCHANGE_INDEX = getHeaderIndex(header, EXCHANGE);
		ASSET_INDEX = getHeaderIndex(header, ASSET);
		BOUGHT_INDEX = getHeaderIndex(header, BOUGHT);
		SOLD_INDEX = getHeaderIndex(header, SOLD);
		FEE_INDEX = getHeaderIndex(header, FEE);
		PRICE_INDEX = getHeaderIndex(header, PRICE);
		FIAT_INDEX = getHeaderIndex(header, FIAT);
	}
	
	
	@Override
	public void outputData(List<TradingElement> elements) {
		getHeaderIdexes();
		
		try { 
			createCsvFile(FILE_PATH, new EasyCsvFile(header, createRecords(elements)));
		} catch (Exception e) { 
			e.printStackTrace();
		}
	
	}
	
	
	private List<String[]> createRecords(List<TradingElement> tradingElements) {
		List<String[]> recordList = new ArrayList<>();
		
		for(TradingElement element : tradingElements) {
			switch (element.getType()) {
			case TRADE:
				recordList.add(createTradeRecord((Trade) element));
				break;
			case DEPOSIT:
				recordList.add(createDepositRecord((Deposit) element));
				break;
			case WITHDRAWAL:
				recordList.add(createWithdrawalRecord((Withdrawal) element));
				break;
			default:
				break;
			}
		}
		
		return recordList;
	}
	
	private String[] createTradeRecord(Trade trade) {
		String[] record = new String[NUMOFHEADERELEMENTS];

		
		record[TIME_INDEX] = trade.time.toString();
		record[TYPE_INDEX] = "trade";
		record[EXCHANGE_INDEX] = trade.exchange;
		record[ASSET_INDEX] = DEFAULT_STRING;
		record[BOUGHT_INDEX] = constructAssetString(trade.bought); 
		record[SOLD_INDEX] = constructAssetString(trade.sold);
		record[PRICE_INDEX] = constructPriceString(trade.price);
		record[FEE_INDEX] = constructAssetString(trade.fee);
		record[FIAT_INDEX] = constructPriceString(trade.priceToFiat);
		
		return record;
	}
	
	private String constructAssetString(Asset asset) {
		return asset.amount + " " + asset.type;
	}
	
	private String constructPriceString(Price price) {
		return price.value + " " + price.base + "/" + price.quote;
	}
	
	private String[] createDepositRecord(Deposit deposit) {
		String[] record = new String[NUMOFHEADERELEMENTS];
		
		record[TIME_INDEX] = deposit.time.toString();
		record[TYPE_INDEX] = "deposit";
		record[EXCHANGE_INDEX] = deposit.exchange;
		record[ASSET_INDEX] = constructAssetString(deposit.asset); 
		record[BOUGHT_INDEX] = DEFAULT_STRING; 
		record[SOLD_INDEX] = DEFAULT_STRING;
		record[FEE_INDEX] = constructAssetString(deposit.fee);
		record[PRICE_INDEX] = DEFAULT_STRING;
		record[FIAT_INDEX] = constructPriceString(deposit.priceToFiat);
		
		return record;
	}
	
	private String[] createWithdrawalRecord(Withdrawal withdrawal) {
		String[] record = new String[NUMOFHEADERELEMENTS];
		
		record[TIME_INDEX] = withdrawal.time.toString();
		record[TYPE_INDEX] = "withdrawal";
		record[EXCHANGE_INDEX] = withdrawal.exchange;
		record[ASSET_INDEX] = constructAssetString(withdrawal.asset); 
		record[BOUGHT_INDEX] = DEFAULT_STRING; 
		record[SOLD_INDEX] = DEFAULT_STRING;
		record[FEE_INDEX] = constructAssetString(withdrawal.fee);
		record[PRICE_INDEX] = DEFAULT_STRING;
		record[FIAT_INDEX] = constructPriceString(withdrawal.priceToFiat);
		
		return record;
	}


	@Override
	public List<TradingElement> inputData() {

		try {
			return getElements(parseCsvFile(FILE_PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	private List<TradingElement> getElements(EasyCsvFile csvFile) {
		
		
		
		
		return null;
	}
}














