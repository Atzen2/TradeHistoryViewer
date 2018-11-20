package dataTypes;

public interface TradingElement {
	
	enum ElementType {TRADE, DEPOSIT, WITHDRAWAL};
	
	public long getTimestamp();
	public ElementType getType();
}
