package priceProvider;

import java.io.IOException;
import java.util.Date;

import dataTypes.Asset.AssetType;
import me.joshmcfarlin.CryptoCompareAPI.Historic;
import me.joshmcfarlin.CryptoCompareAPI.Historic.History;
import me.joshmcfarlin.CryptoCompareAPI.Historic.History.Data;
import me.joshmcfarlin.CryptoCompareAPI.Utils.OutOfCallsException;

public class CryptoComparePriceProvider implements PriceProvider{

	private final long HOUR = 3600*1000;
	int cnt = 0;
	
	@Override
	public float getPrice(String exchange, AssetType base, AssetType quote, Date time) {
		float price = 0;
		int timestamp = convertMillisecondsToSeconds(setToNextHour(time.getTime()));
		
		try {
			
			History history = Historic.getHour(base.toString(), quote.toString(), 1, timestamp, exchange);
			Data data = history.data.get(history.data.size() - 1);
			price = (float) ((data.high + data.low) / 2);
			
			Thread.sleep(210);
			System.out.println("got price " + cnt++ + " " + time);
			
		} catch (IOException | OutOfCallsException | InterruptedException e) {
			e.printStackTrace();
			price = 0;
		}
		
		return price;
	}
	
	
	private int convertMillisecondsToSeconds(long timestamp) {
		return (int) (timestamp / 1000);
	}
	
	private long setToNextHour(long timestamp) {
		long minutes = timestamp % HOUR;
		return timestamp + (HOUR - minutes);
	}

}
