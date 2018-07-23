package priceProvider;

import java.util.Date;

import dataTypes.Asset.AssetType;

public interface PriceProvider {
	public float getPrice(String exchange, AssetType base, AssetType quote, Date time);
}
