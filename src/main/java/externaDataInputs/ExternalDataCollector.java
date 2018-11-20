package externaDataInputs;

import java.util.List;

import dataTypes.Deposit;
import dataTypes.Trade;
import dataTypes.Withdrawal;

public interface ExternalDataCollector {
	
	public void collectInputData();
	
	public List<Trade> getTradeList();
	public List<Deposit> getDepositList();
	public List<Withdrawal> getWithdrawalList();
}
