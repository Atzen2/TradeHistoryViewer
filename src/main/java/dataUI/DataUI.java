package dataUI;

import java.util.List;

import dataTypes.TradingElement;

public interface DataUI {

	public void outputData(List<TradingElement> elements);
	public List<TradingElement> inputData();
}
