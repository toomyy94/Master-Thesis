package PT.PTInov.ArQoSPocketEDP.JSONConnector.Util;

import android.annotation.SuppressLint;

@SuppressLint("ParserError")
public class Tuplo {
	
	private String first = null;
	private String second = null;
	private String three = null;
	
	public Tuplo(String first, String second, String three) {
		this.first = first;
		this.second = second;
		this.three = three;
	}
	
	public String getFirst() {
		return first;
	}
	
	public String getSecond() {
		return second;
	}
	
	public String getThree() {
		return three;
	}
	
	public String toString() {
		return "\nfirst :"+first+"\nsecond :"+second+"\nthree :"+three;
	}
}
