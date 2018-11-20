package externaDataInputs;

import java.util.ArrayList;
import java.util.List;

import dataTypes.Deposit;
import dataTypes.Trade;
import dataTypes.Withdrawal;
import easyCsvHandler.EasyCsvHandler;

public class UserCsvParser extends EasyCsvHandler implements ExternalDataCollector{

	private List<Trade> tradeList = new ArrayList<>();
	private List<Deposit> depositList = new ArrayList<>();
	private List<Withdrawal> wthdrawalList = new ArrayList<>();
	
	@Override
	public void collectInputData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Trade> getTradeList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Deposit> getDepositList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Withdrawal> getWithdrawalList() {
		// TODO Auto-generated method stub
		return null;
	}

}
