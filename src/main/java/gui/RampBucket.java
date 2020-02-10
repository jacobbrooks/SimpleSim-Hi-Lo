package gui;

import javafx.beans.property.SimpleIntegerProperty;

public class RampBucket {

	private String row;

	private SimpleIntegerProperty tc0;
	private SimpleIntegerProperty tc1;
	private SimpleIntegerProperty tc2;
	private SimpleIntegerProperty tc3;
	private SimpleIntegerProperty tc4;
	private SimpleIntegerProperty tc5;
	private SimpleIntegerProperty tc6;

	public RampBucket(String row) {
		this.row = row;
		tc0 = new SimpleIntegerProperty();
		tc1 = new SimpleIntegerProperty();
		tc2 = new SimpleIntegerProperty();
		tc3 = new SimpleIntegerProperty();
		tc4 = new SimpleIntegerProperty();
		tc5 = new SimpleIntegerProperty();
		tc6 = new SimpleIntegerProperty();
	}

	public String getRow() {
		return row;
	}

	public Integer getTc0() {
		return tc0.get();
	}

	public Integer getTc1() {
		return tc1.get();
	}

	public Integer getTc2() {
		return tc2.get();
	}

	public Integer getTc3() {
		return tc3.get();
	}

	public Integer getTc4() {
		return tc4.get();
	}

	public Integer getTc5() {
		return tc5.get();
	}

	public Integer getTc6() {
		return tc6.get();
	}

	public void setTc0(int n) {
		tc0.set(n);
	}

	public void setTc1(int n) {
		tc1.set(n);
	}

	public void setTc2(int n) {
		tc2.set(n);
	}

	public void setTc3(int n) {
		tc3.set(n);
	}

	public void setTc4(int n) {
		tc4.set(n);
	}

	public void setTc5(int n) {
		tc5.set(n);
	}

	public void setTc6(int n) {
		tc6.set(n);
	}

	public void initDefaultBetValues(int minBet) {
		tc0.set(minBet);
		tc1.set(minBet);
		tc2.set(minBet);
		tc3.set(minBet);
		tc4.set(minBet);
		tc5.set(minBet);
		tc6.set(minBet);
	}

	public void initDefaultSpotValues() {
		tc0.set(1);
		tc1.set(1);
		tc2.set(1);
		tc3.set(1);
		tc4.set(1);
		tc5.set(1);
		tc6.set(1);
	}

}
