package dataTypes;

import java.util.Date;

public class Withdrawal implements TradingElement{
	public Date time;
	public Asset asset;
	public Asset fee;
	public String exchange ="none";
	public Price priceToFiat;

	
	
	public Withdrawal() {};

	
	@Override
	public Date getDate() {
		return time;
	}

	
	@Override
	public ElementType getType() {
		return ElementType.WITHDRAWAL;
	}
}
