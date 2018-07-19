package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import dataTypes.Asset;
import dataTypes.Deposit;
import dataTypes.Price;
import dataTypes.Trade;
import dataTypes.TradingElement;
import dataTypes.Withdrawal;
import externaDataInputs.ExternalDataCollector.AssetType;
import priceProvider.PriceProvider;

public class DataProcessor {

	
	
	public static List<Trade> mergeTrades(List<Trade> trades, long timeWindow) {
		List<Trade> mergedList = new ArrayList<>();
		
		Asset dummyAsset = new Asset(AssetType.NONE, 0.0f);
		Trade lastTrade = new Trade();
		lastTrade.time = new Date();
		lastTrade.bought = dummyAsset;
		lastTrade.sold = dummyAsset;
		
		
		for(Trade currentTrade : trades) {
		
			if(isInTimeWindow(currentTrade.time.getTime(), lastTrade.time.getTime(), timeWindow) &&
			   isSameTradeType(currentTrade, lastTrade)) { // merge trade
				
				lastTrade.bought.amount += currentTrade.bought.amount;
				lastTrade.sold.amount += currentTrade.sold.amount;
				lastTrade.fee.amount += currentTrade.fee.amount;
				
			} else {
				
				mergedList.add(lastTrade);
				lastTrade = currentTrade;
				
			}
		}
		
		mergedList.add(lastTrade);
		mergedList.remove(0); // remove dummy trade
		
		return mergedList;
	}
	
	
	private static boolean isInTimeWindow(long time1, long time2, long timeWindow) {
		return (time1 <= time2 + timeWindow) ? true : false;
	}
	
	private static boolean isSameTradeType(Trade trade1, Trade trade2) {
		return (trade1.bought.type.equals(trade2.bought.type) && trade1.sold.type.equals(trade2.sold.type)) ? true : false;
	}
	
	
	
	public static List<TradingElement> sortTradingElements(List<TradingElement> list) {
		Collections.sort(list, new Comparator<TradingElement>() {
		
			@Override
		    public int compare(TradingElement element1, TradingElement element2) {
		        if (element1.getDate().getTime() <= element2.getDate().getTime()) return -1;
		        else return 1;
		    }
		});
		
		return list;
	}
	
	
	
	public static List<TradingElement> addFiatPrice(PriceProvider provider, List<TradingElement> tradingElementList) {
		List<TradingElement> tradingElements = new ArrayList<>();
		
		
		for(TradingElement element : tradingElementList) {
			switch (element.getType()) {
			case TRADE:
				Trade trade = (Trade) element;
				
				if(isFiat(trade.price.quote)) trade.priceToFiat = trade.price;
				else if(isFiat(trade.price.base)) trade.priceToFiat = new Price(trade.price.quote, trade.price.base, (1 / trade.price.value));
				else trade.priceToFiat = getFiatPrice(provider, trade.exchange, trade.sold.type, trade.time);
				
				tradingElements.add(trade);
				break;
				
			case DEPOSIT:
				Deposit deposit = (Deposit) element;
				
				if(isFiat(deposit.asset.type)) deposit.priceToFiat = new Price(AssetType.EUR, AssetType.EUR, 1);
				else deposit.priceToFiat = getFiatPrice(provider, deposit.exchange, deposit.asset.type, deposit.time);
				
				tradingElements.add(deposit);
				break;
				
			case WITHDRAWAL:
				Withdrawal withdrawal = (Withdrawal) element;
				
				if(isFiat(withdrawal.asset.type)) withdrawal.priceToFiat = new Price(AssetType.EUR, AssetType.EUR, 1);
				else withdrawal.priceToFiat = getFiatPrice(provider, withdrawal.exchange, withdrawal.asset.type, withdrawal.time);
				
				tradingElements.add(withdrawal);
				break;
				
			default:
				break;
			}	
		}
		
		return tradingElements;
	}
	
	
	private static boolean isFiat(AssetType assetType) {
		return assetType == AssetType.EUR;
	}
	
	private static Price getFiatPrice(PriceProvider provider, String exchange, AssetType assetType, Date time) {
		if(provider != null) return new Price(assetType, AssetType.EUR, provider.getPrice(exchange, assetType, AssetType.EUR, time));
		return new Price(assetType, AssetType.EUR, 0); 
	}

}