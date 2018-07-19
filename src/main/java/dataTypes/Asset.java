package dataTypes;

import externaDataInputs.ExternalDataCollector.AssetType;

public class Asset {
	public AssetType type;
	public float amount;
	
	
	public Asset(AssetType type, float amount) {
		this.type = type;
		this.amount = amount;
	}
}
