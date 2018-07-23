package externaDataInputs;

import java.util.List;

import dataTypes.Deposit;
import dataTypes.Trade;
import dataTypes.Withdrawal;

public interface ExternalDataCollector {
	
//	public enum AssetType { NONE, BTC, LTC, EUR, DAO, ETH, ETC, REP, ZEC, XMR, BCH }
	
	public void collectInputData();
	
	public List<Trade> getTradeList();
	public List<Deposit> getDepositList();
	public List<Withdrawal> getWithdrawalList();

	
}
