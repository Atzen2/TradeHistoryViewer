package dataTypes;

import dataTypes.Asset.AssetType;

public class Price {
	public float value;
	public AssetType base = AssetType.NONE;
	public AssetType quote = AssetType.NONE;
	
	
	
	public Price(AssetType base, AssetType quote, float value) {
		this.value = value;
		this.base = base;
		this.quote = quote;
	}
	
	
	
	public Price (Asset bought, Asset sold) {
		// priceType : base (bought) / quote (sold)
		// price : sold /bought
		
		value = sold.amount / bought.amount;
		base = bought.type;
		quote = sold.type;
	}
}
