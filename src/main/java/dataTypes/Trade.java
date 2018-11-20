package dataTypes;

import dataTypes.Asset.AssetType;

public class Trade implements TradingElement{
	public long timestamp = 0;
	public Asset bought = new Asset(AssetType.NONE, 0);
	public Asset sold = new Asset(AssetType.NONE, 0);
	public Asset fee = new Asset(AssetType.NONE, 0);
	public String exchange ="none";
	public Price price = new Price(AssetType.NONE, AssetType.NONE, 0);
	public Price priceToFiat = new Price(AssetType.NONE, AssetType.NONE, 0);

	
	
	public Trade() {};
	
	
	@Override
	public long getTimestamp() {
		return timestamp;
	}

	
	@Override
	public ElementType getType() {
		return ElementType.TRADE;
	}
}
