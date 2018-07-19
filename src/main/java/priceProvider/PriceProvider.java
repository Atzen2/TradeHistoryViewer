package priceProvider;

import java.util.Date;

import externaDataInputs.ExternalDataCollector.AssetType;

public interface PriceProvider {
	public float getPrice(String exchange, AssetType base, AssetType quote, Date time);
}
