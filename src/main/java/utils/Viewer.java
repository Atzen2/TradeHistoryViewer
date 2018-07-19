package utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import dataTypes.Deposit;
import dataTypes.Trade;
import dataTypes.TradingElement;
import dataTypes.Withdrawal;
import dataTypes.TradingElement.ElementType;

public class Viewer {

	public static void showRecord(CSVRecord record) {
		String txId = record.get(Constants.Kraken.TXID);
		String refid = record.get(Constants.Kraken.REFID);
		String time = record.get(Constants.Kraken.TIME);
		String type = record.get(Constants.Kraken.TYPE);
		String aclass = record.get(Constants.Kraken.ACLASS);
		String asset = record.get(Constants.Kraken.ASSET);
		String amount = record.get(Constants.Kraken.AMOUNT);
		String fee = record.get(Constants.Kraken.FEE);
		String balance = record.get(Constants.Kraken.BALANCE);
		

		System.out.println("Record No - " + record.getRecordNumber());
		System.out.println("---------------");
		System.out.println("txId : " + txId);
		System.out.println("refid : " + refid);
		System.out.println("time : " + time);
		System.out.println("type : " + type);
		System.out.println("aclass : " + aclass);
		System.out.println("asset : " + asset);
		System.out.println("amount : " + amount);
		System.out.println("fee : " + fee);
		System.out.println("balance : " + balance);
		System.out.println("---------------\n\n");
	}
	
	
	public static void showDeposit(Deposit deposit) {
		System.out.println("deposit");
		System.out.println("exchange: " + deposit.exchange);
		System.out.println("asset: " + deposit.asset.amount + " " + deposit.asset.type);
		System.out.println("fee: " + deposit.fee.amount + " " + deposit.fee.type);
		System.out.println("time: " + deposit.time);
		System.out.println("\n\n");
	}
	
	public static void showWithdrawal(Withdrawal withdrawal) {
		System.out.println("withdrawal");
		System.out.println("exchange: " + withdrawal.exchange);
		System.out.println("asset: " + withdrawal.asset.amount + " " + withdrawal.asset.type);
		System.out.println("fee: " + withdrawal.fee.amount + " " + withdrawal.fee.type);
		System.out.println("time: " + withdrawal.time);
		System.out.println("\n\n");
	}
	
	public static void showTrade(Trade trade) {
		System.out.println("trade");
		System.out.println("exchange: " + trade.exchange);
		System.out.println("bought: " + trade.bought.amount + " " + trade.bought.type);
		System.out.println("sold: " + trade.sold.amount + " " + trade.sold.type);
		System.out.println("fee: " + trade.fee.amount + " " + trade.fee.type);
		System.out.println("price: " + trade.price.value + " " + trade.price.base + "/" + trade.price.quote);
		System.out.println("time: " + trade.time);
		System.out.println("\n\n");
	}
	
	
	public static void showTradingElement(TradingElement action) {
		switch(action.getType()) {
			case TRADE: 
				showTrade((Trade) action);
				break;
			case DEPOSIT:
				showDeposit((Deposit) action);
				break;
			case WITHDRAWAL:
				showWithdrawal((Withdrawal) action);
				break;
			default: 
				break;
		}
	}
	
	
	public static void showRecords(CSVParser parser) {
		for (CSVRecord record : parser) showRecord(record);
	}
	
	
	public static void showColumnElements(CSVParser parser, String columnHeader) {
		List<String> elementList = new ArrayList();
		
		String element;
		for(CSVRecord record : parser) {
			
			element = record.get(columnHeader);
			if(!elementList.contains(element)) elementList.add(element);
		}
		
		for(String e : elementList) System.out.println(columnHeader + ": " + e);
	}
	
}
