package dataTypes;

import java.util.Date;

public interface TradingElement {
	
	enum ElementType {TRADE, DEPOSIT, WITHDRAWAL};
	
	public Date getDate();
	public ElementType getType();
}
