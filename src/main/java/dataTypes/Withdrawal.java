package dataTypes;

import dataTypes.Asset.AssetType;

public class Withdrawal implements TradingElement{
	public long timestamp = 0;
	public Asset asset = new Asset(AssetType.NONE, 0);
	public Asset fee = new Asset(AssetType.NONE, 0);
	public String exchange ="none";
	public Price priceToFiat = new Price(AssetType.NONE, AssetType.NONE, 0);

	
	
	public Withdrawal() {};

	
	@Override
	public long getTimestamp() {
		return timestamp;
	}

	
	@Override
	public ElementType getType() {
		return ElementType.WITHDRAWAL;
	}
}
