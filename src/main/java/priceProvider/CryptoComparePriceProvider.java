package priceProvider;

import java.io.IOException;
import java.util.Date;

import dataTypes.Asset.AssetType;
import me.joshmcfarlin.CryptoCompareAPI.Historic;
import me.joshmcfarlin.CryptoCompareAPI.Historic.History;
import me.joshmcfarlin.CryptoCompareAPI.Historic.History.Data;
import me.joshmcfarlin.CryptoCompareAPI.Utils.OutOfCallsException;

public class CryptoComparePriceProvider implements PriceProvider{

	private final long HOUR = 3600;
	
	
	
	@Override
	public float getPrice(String exchange, AssetType base, AssetType quote, long timestamp) {
		return getPriceInternal(exchange, base, quote, timestamp, false);
	}
	
	
	private float getPriceInternal(String exchange, AssetType base, AssetType quote, long time, boolean backup) {
		float price = 0;
		int timestamp = (int) setToNextHour(time);
		
		try {
			History history;


			if(backup) history = Historic.getHour(base.toString(), quote.toString(), 1, timestamp);
			else history = Historic.getHour(base.toString(), quote.toString(), 1, timestamp, exchange);

			System.out.println("got fiat price for " + base + " date: " + new Date(time * 1000) + (backup ? "(backup)" : ""));
			Thread.sleep(210);

			if(history.response.contains("Error") || history.data.get(0).high == 0 && history.data.get(0).low == 0) {
				if(backup) return 0;
				else return getPriceInternal(exchange, base, quote, time, true);
			}


			Data data = history.data.get(history.data.size() - 1);
			price = (float) ((data.high + data.low) / 2);


		} catch (IOException | OutOfCallsException | InterruptedException e) {
			e.printStackTrace();
			price = 0;
		}

		return price;
	}
	
	private long setToNextHour(long timestamp) {
		long minutes = timestamp % HOUR;
		return timestamp + (HOUR - minutes);
	}

}
