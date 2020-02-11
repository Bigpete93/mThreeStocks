package org.mThree.API;

/*******************
 * Record Class holds the various data from the API requests in a single object.
 *  Has a stock record's data for a given time period
 ******************/
public class Record {

	private String symbol;
    private String date;
    private double open;
    private  double high;
    private double low;
    private  double close;
    private  long volume;

    public Record(String Symbol, String Date, double Open, double High, double Low, double Close, long Volume){
        date = Date;
        open = Open;
        high = High;
        low = Low;
        close = Close;
        volume = Volume;
        symbol = Symbol;
    }

    public Record() {
        // Auto-generated constructor stub
    }

    //human readable if record needs checking later
    public String toString() {
        return date + "\n"
        + "open: " + open + "\n"
        + "high: " + high + "\n"
        + "low: " + low + "\n"
        + "close: " + close + "\n"
        + "volume: " + volume + "\n";
    }

	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
    public void setDate(String date) {
        this.date = date;
    }
    public void setOpen(double open) {
        this.open = open;
    }
    public void setHigh(double high) {
        this.high = high;
    }
    public void setLow(double low) {
        this.low = low;
    }
    public void setClose(double close) {
        this.close = close;
    }
    public void setVolume(long volume) {
        this.volume = volume;
    }
    public String getDate(){
        return date;
    }
    public double getOpen(){
        return open;
    }
    public double getHigh(){
        return high;
    }
    public double getLow(){
        return low;
    }
    public double getClose(){
        return close;
    }
    public long getVolume(){
        return volume;
    }
}