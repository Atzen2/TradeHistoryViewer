package logic;


import java.util.ArrayList;
import java.util.List;

import dataTypes.Asset;
import dataTypes.Asset.AssetType;
import dataTypes.Balance;
import dataTypes.Deposit;
import dataTypes.Price;
import dataTypes.Trade;
import dataTypes.TradingElement;
import dataTypes.Withdrawal;
import dataUI.DataCsvFileHandler;
import dataUI.DataUI;
import externaDataInputs.ExternalDataCollector;
import externaDataInputs.KrakenCsvParser;
import priceProvider.CryptoComparePriceProvider;
import priceProvider.PriceProvider;
import utils.DataProcessor;
import utils.Viewer;

public class LogicAPI {
	
	private static List<ExternalDataCollector> externalDataCollectors = new ArrayList<>();
	private static List<Trade> tradeList = new ArrayList<>();
	private static List<Deposit> depositList = new ArrayList<>();
	private static List<Withdrawal> withdrawalList = new ArrayList<>();
	private static List<TradingElement> tradingElementList = new ArrayList<>();
	private static List<List<Balance>> balancesList = new ArrayList<>();
	
	private static PriceProvider priceProvider;
	private static DataUI dataUI;
	
	
	
	public static void addExternalDataCollector(ExternalDataCollector dataCollector) {
		externalDataCollectors.add(dataCollector);
	}
	
	
	
	public static void setPriceProvider(PriceProvider _priceProvider) {
		priceProvider = _priceProvider;
	}
	
	
	
	public static void setDataUI(DataUI _dataUI) {
		dataUI = _dataUI;
	}
	
	
	
	public static void collectExternalData() {
		for(ExternalDataCollector collector : externalDataCollectors) {
			collector.collectInputData();
			tradeList.addAll(collector.getTradeList());
			depositList.addAll(collector.getDepositList());
			withdrawalList.addAll(collector.getWithdrawalList());
		}
	}
	
	
	
	public static void processExternalData() {
		tradeList = DataProcessor.mergeTrades(tradeList, 0);
		
		tradingElementList.addAll(tradeList);
		tradingElementList.addAll(depositList);
		tradingElementList.addAll(withdrawalList);
		tradingElementList = DataProcessor.sortTradingElements(tradingElementList);
		tradingElementList = DataProcessor.addFiatPrice(priceProvider, tradingElementList);
	}
	
	
	
	public static void outputProcessedData() {
		dataUI.outputData(tradingElementList);
	}
	
	
	
	public static void inputProcessedData() {
		tradingElementList = dataUI.inputData();
		tradingElementList = DataProcessor.sortTradingElements(tradingElementList);
	}
	
	
	
	public static void createBalances() {
		
		for(int i = 0 ; i < tradingElementList.size() ; i++) {
			List<Balance> inputBalances;
			
			if(i == 0) inputBalances = new ArrayList<>();
			else inputBalances = new ArrayList<>(balancesList.get(i -1));
			
			balancesList.add(DataProcessor.getBalances(tradingElementList.get(i), inputBalances));
		}
		
	}

	
	
	public static  List<Trade> getTrades(){
		return tradeList;
	}
	
	
	
	public static  List<Deposit> getDeposits(){
		return depositList;
	}
	
	
	
	public static  List<Withdrawal> getWithdrawals(){
		return withdrawalList;
	}
	
	
	
	public static List<TradingElement> getTradingElements() {
		return tradingElementList;
	}
	
	
	
	public static List<List<Balance>> getBalances() {
		return balancesList;
	}
	
	
	
	
	public static void main(String[] args) {
		boolean externalData = false;
		boolean dataInput = true;
		
		LogicAPI.addExternalDataCollector(new KrakenCsvParser());
		LogicAPI.setDataUI(new DataCsvFileHandler());
		LogicAPI.setPriceProvider(new CryptoComparePriceProvider());
		

		if(externalData) {
			LogicAPI.collectExternalData();
			LogicAPI.processExternalData();
			
			LogicAPI.outputProcessedData();
		}
		
		if(dataInput) {
			LogicAPI.inputProcessedData();
			LogicAPI.createBalances();
			Viewer.showBalances(LogicAPI.getBalances());
		}
		
//		for(TradingElement element : LogicAPI.getTradingElements()) Viewer.showTradingElement(element);
		
//		Trade trade = LogicAPI.getTrades().get(0);
//		System.out.println("Price: " + ccPriceProvider.getPrice(trade.exchange, trade.price.quote, AssetType.EUR, trade.time));
		
		
	}
}
