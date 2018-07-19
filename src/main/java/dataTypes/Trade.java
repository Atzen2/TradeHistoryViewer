package dataTypes;

import java.util.Date;

public class Trade implements TradingElement{
	public Date time;
	public Asset bought;
	public Asset sold;
	public Asset fee;
	public String exchange ="none";
	public Price price;
	public Price priceToFiat;

	
	
	public Trade() {};
	
	
	@Override
	public Date getDate() {
		return time;
	}

	
	@Override
	public ElementType getType() {
		return ElementType.TRADE;
	}
}
