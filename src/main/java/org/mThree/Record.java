package org.mThree;
public class Record {
    private String date;
    private double open;
    private  double high;
    private double low;
    private  double close;
    private  long volume;

    public Record(String Date, double Open, double High, double Low, double Close, long Volume){
        date = Date;
        open = Open;
        high = High;
        low = Low;
        close = Close;
        volume = Volume;
    }

    public Record() {
        // TODO Auto-generated constructor stub
    }

    public String toString() {
        String str = "";
        str += date + "\n";
        str += "open: " + open + "\n";
        str += "high: " + high + "\n";
        str += "low: " + low + "\n";
        str += "close: " + close + "\n";
        str += "volume: " + volume + "\n";

        return str;

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