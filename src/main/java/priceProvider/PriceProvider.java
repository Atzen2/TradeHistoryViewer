package priceProvider;

import dataTypes.Asset.AssetType;

public interface PriceProvider {
	public float getPrice(String exchange, AssetType base, AssetType quote, long timestamp);
}
