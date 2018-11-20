package playground;

import dataUI.DataCsvFileHandler;
import externaDataInputs.BitfinexCsvParser;
import externaDataInputs.HitbtcCsvParser;
import externaDataInputs.KrakenCsvParser;
import logic.LogicAPI;
import priceProvider.CryptoComparePriceProvider;

public class Playground {
	
	
	
	private static void logicAPI() {
		boolean externalData = true;
		boolean dataInput = true;
		
		LogicAPI.addExternalDataCollector(new KrakenCsvParser());
		LogicAPI.addExternalDataCollector(new BitfinexCsvParser());
		LogicAPI.addExternalDataCollector(new HitbtcCsvParser());
		LogicAPI.setDataUI(new DataCsvFileHandler());
		LogicAPI.setPriceProvider(new CryptoComparePriceProvider());
		

		if(externalData) {
			LogicAPI.collectExternalData();
			LogicAPI.processExternalData();
			
			LogicAPI.outputProcessedData();
		}
		
		if(dataInput) {
			LogicAPI.inputProcessedData();
			LogicAPI.createBalances();
//			Viewer.showBalances(LogicAPI.getBalances());
		}
		
//		for(TradingElement element : LogicAPI.getTradingElements()) Viewer.showTradingElement(element);
		
//		Trade trade = LogicAPI.getTrades().get(0);
//		System.out.println("Price: " + ccPriceProvider.getPrice(trade.exchange, trade.price.quote, AssetType.EUR, trade.time));
		
		
	}

	public static void main(String[] args) {
		logicAPI();
		
//		"2016-03-02 17:37:28"
//		long timestamp = 1456940248;
//		
//		
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//		String date = formatter.format(ZonedDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault()));
//		System.out.println(date);
//		
//		long time = ZonedDateTime.of(LocalDateTime.parse(date, formatter), ZoneId.systemDefault()).toEpochSecond();
//		System.out.println(time);
		
		
//		Instant in = Instant.ofEpochMilli(new Date().getTime());
//		System.out.println(in.toString());
		
//		System.out.println(Instant.parse(time).toEpochMilli());
		
		
//		Date d = new Date();
//		
//		String s = d.toString();
//		System.out.println(s);
//		
//		String time = "2017-09-07 11:09:29";
//		System.out.println(time);
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
////		LocalDateTime t = LocalDateTime.parse(time, formatter);
////		long sec = t.toEpochSecond(ZoneOffset.UTC);
//		long s = LocalDateTime.parse(time, formatter).toInstant(ZoneOffset.UTC).getEpochSecond();
//		System.out.println(s);
//		
////		DateTimeFormatter formatter2 = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
//		Instant i = Instant.ofEpochSecond(s);
//		ZonedDateTime r = ZonedDateTime.ofInstant(i, ZoneId.systemDefault());
//		
		
//		System.out.println(formatter2.format(r));
		
//		
//
//		
//		System.out.println(formatter.format());
		
		
//		System.out.println(new Date((long) 1504782540 * 1000));
		
//		URL url;
//		try {
////			url = new URL("https://coinmarketcap.com/#EUR");
//			url = new URL("https://coinmarketcap.com/currencies/iota/");
//			
//			
//			
//			HttpURLConnection request = (HttpURLConnection) url.openConnection();
//			request.setDoOutput(true);
//			request.setRequestMethod("GET");
//			request.connect();
//			
//			InputStream in = new BufferedInputStream(request.getInputStream());
////			InputStreamReader reader = new InputStreamReader(in);
//			
//			String content = new String(IOUtils.readFully(in, -1, true));
//			
//			System.out.println(content);
//			
//			PrintWriter writer = new PrintWriter("log.html", "UTF-8");
//			writer.write(content);
//			writer.close();
//			
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 		
		
	}

}













