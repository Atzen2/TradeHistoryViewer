package dataTypes;


public class Asset {
	
	public enum AssetType { NONE, BTC, LTC, EUR, DAO, ETH, ETC, REP, ZEC, XMR, BCH, IOTA, NXT, ARDR, AE, LSK, SBTC }

	public AssetType type;
	public float amount;
	
	
	public Asset(AssetType type, float amount) {
		this.type = type;
		this.amount = amount;
	}
	
	
	
	public Asset(String type, float amount) {
		this.type = convertStringToAssetType(type);
		this.amount = amount;
	}
	
	
	
	static public AssetType convertStringToAssetType(String type) {
		switch (type) {
		case "BTC": return AssetType.BTC;
		case "LTC": return AssetType.LTC;
		case "EUR": return AssetType.EUR;
		case "DAO": return AssetType.DAO;
		case "ETH": return AssetType.ETH;
		case "ETC": return AssetType.ETC;
		case "REP": return AssetType.REP;
		case "ZEC": return AssetType.ZEC;
		case "XMR": return AssetType.XMR;
		case "BCH": return AssetType.BCH;
		case "IOTA": return AssetType.IOTA;
		case "NXT": return AssetType.NXT;
		case "ARDR": return AssetType.ARDR;
		case "AE": return AssetType.AE;
		case "LSK": return AssetType.LSK;
		case "SBTC": return AssetType.SBTC;
		default: return AssetType.NONE;
		}
	}
}
