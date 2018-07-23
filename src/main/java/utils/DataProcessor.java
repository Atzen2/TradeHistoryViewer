package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import dataTypes.Asset;
import dataTypes.Asset.AssetType;
import dataTypes.Balance;
import dataTypes.Deposit;
import dataTypes.Price;
import dataTypes.Trade;
import dataTypes.TradingElement;
import dataTypes.Withdrawal;
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
		        if (element1.getDate().getTime() < element2.getDate().getTime()) return -1;
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
	
	
	
	public static boolean isSameAsset(Asset asset1, Asset asset2) {
		if(asset1.amount == asset2.amount && asset1.type == asset2.type) return true;
		return false;
	}
	
	
	
	public static boolean isSamePrice(Price price1, Price price2) {
		if(price1.value == price2.value && price1.base == price2.base && price1.quote == price2.quote) return true;
		return false;
	}
	
	
	
	public static boolean isSameTime(Date time1, Date time2) {
		return time1.getTime() == time2.getTime() ? true : false;
	}
	
	
	
	public static boolean isSameExchange(String exchange1, String exchange2) {
		return exchange1.equals(exchange2) ? true : false;
	}

	
	
	public static List<AssetType> getAssetTypes(List<TradingElement> tradingElements) {
		List<AssetType> assetTypeList = new ArrayList<>();
		
		
		for(TradingElement element : tradingElements) {
			switch (element.getType()) {
			case TRADE:
				Trade trade = (Trade) element;
				
				if(!assetTypeList.contains(trade.bought.type)) assetTypeList.add(trade.bought.type);
				if(!assetTypeList.contains(trade.sold.type)) assetTypeList.add(trade.sold.type);
				break;
				
			case DEPOSIT:
				Deposit deposit = (Deposit) element;
				
				if(!assetTypeList.contains(deposit.asset.type)) assetTypeList.add(deposit.asset.type);
				break;
				
			case WITHDRAWAL:
				Withdrawal withdrawal = (Withdrawal) element;

				if(!assetTypeList.contains(withdrawal.asset.type)) assetTypeList.add(withdrawal.asset.type);
				break;

			default:
				break;
			}
		}
		
		return assetTypeList;
	}
	
	
	
	public static List<Balance> getBalances(TradingElement tradingElement, List<Balance> initialBalances) {
		List<Balance> balances = initialBalances;
		List<Balance> newBalances = new ArrayList<>();

		
		Deposit deposit = null;
		Trade trade = null;
		Withdrawal withdrawal = null;
		
		Balance initBalance;
		switch (tradingElement.getType()) {
		case TRADE:
			trade = (Trade) tradingElement;
			
			initBalance = new Balance();
			initBalance.asset = new Asset(trade.bought.type, 0);
			initBalance.fiatValue = new Asset(AssetType.EUR, 0);
			initBalance.priceToFiat = new Price(AssetType.NONE, AssetType.NONE, 0);
			balances = addIfNotExist(balances, initBalance);
			
			initBalance = new Balance();
			initBalance.asset = new Asset(trade.sold.type, 0);
			initBalance.fiatValue = new Asset(AssetType.EUR, 0);
			initBalance.priceToFiat = new Price(AssetType.NONE, AssetType.NONE, 0);
			balances = addIfNotExist(balances, initBalance);
			
			break;
			
		case DEPOSIT:
			deposit = (Deposit) tradingElement;
			
			initBalance = new Balance();
			initBalance.asset = new Asset(deposit.asset.type, 0);
			initBalance.fiatValue = new Asset(AssetType.EUR, 0);
			initBalance.priceToFiat = new Price(AssetType.NONE, AssetType.NONE, 0);
			balances = addIfNotExist(balances, initBalance);

			break;
			
		case WITHDRAWAL:
			withdrawal = (Withdrawal) tradingElement;
			
			initBalance = new Balance();
			initBalance.asset = new Asset(withdrawal.asset.type, 0);
			initBalance.fiatValue = new Asset(AssetType.EUR, 0);
			initBalance.priceToFiat = new Price(AssetType.NONE, AssetType.NONE, 0);
			balances = addIfNotExist(balances, initBalance);

			break;

		default:
			break;
		}

		
		for(Balance balance : balances) {
			boolean isAdded = false;
			
			if(deposit != null) {
				if(balance.asset.type == deposit.asset.type) {
					float amount = balance.asset.amount + deposit.asset.amount;
					float fiat = amount * deposit.priceToFiat.value; 
					
					Balance newBalance = new Balance();
					newBalance.asset = new Asset(balance.asset.type, amount);
					newBalance.fiatValue = new Asset(balance.fiatValue.type, fiat);
					newBalance.priceToFiat = deposit.priceToFiat;
					newBalance.date = tradingElement.getDate();
					
					newBalances.add(newBalance);
					isAdded = true;
				}
			}
			
			
			if(withdrawal != null) {
				if(balance.asset.type == withdrawal.asset.type) {
					float amount = balance.asset.amount - withdrawal.asset.amount;
					float fiat = amount * withdrawal.priceToFiat.value; 
					
					Balance newBalance = new Balance();
					newBalance.asset = new Asset(balance.asset.type, amount);
					newBalance.fiatValue = new Asset(balance.fiatValue.type, fiat);
					newBalance.priceToFiat = withdrawal.priceToFiat;
					newBalance.date = tradingElement.getDate();
					
					newBalances.add(newBalance);
					isAdded = true;
				}
			}
			
			
			if(trade != null) {
				if(balance.asset.type == trade.bought.type) {
					float amount = balance.asset.amount + trade.bought.amount;
					float fiat = amount * trade.price.value * trade.priceToFiat.value; 
					
					Balance newBalance = new Balance();
					newBalance.asset = new Asset(balance.asset.type, amount);
					newBalance.fiatValue = new Asset(balance.fiatValue.type, fiat);
					newBalance.priceToFiat = new Price(trade.price.base, trade.priceToFiat.quote, trade.price.value * trade.priceToFiat.value);
					newBalance.date = tradingElement.getDate();
					
					newBalances.add(newBalance);
					isAdded = true;
				}
				
				if(balance.asset.type == trade.sold.type) {
					float amount = balance.asset.amount - trade.sold.amount;
					float fiat = amount * trade.priceToFiat.value; 
					
					Balance newBalance = new Balance();
					newBalance.asset = new Asset(balance.asset.type, amount);
					newBalance.fiatValue = new Asset(balance.fiatValue.type, fiat);
					newBalance.priceToFiat = trade.priceToFiat;
					newBalance.date = tradingElement.getDate();
					
					newBalances.add(newBalance);
					isAdded = true;
				}
			}
			
			if(!isAdded) {
				Balance newBalance = new Balance();
				newBalance.asset = balance.asset;
				newBalance.fiatValue = balance.fiatValue;
				newBalance.priceToFiat = balance.priceToFiat;
				newBalance.date = tradingElement.getDate();
				
				newBalances.add(newBalance);
			}
				
		}
		
		return newBalances;
	}
	
	
	private static List<Balance> addIfNotExist(List<Balance> balances, Balance balance) {
		
		for(Balance b : balances) {
			if(b.asset.type == balance.asset.type) return balances;
		}
		
		balances.add(balance);
		return balances;
	}
	
}