package com.z.zebra.BowlingScoreReport.Model;

import java.util.Arrays;

public class Frame {

	private String[] pinFalls;
	private int score;

	public String[] getPinfalls() {
		return pinFalls;
	}

	public void setPinfalls(String[] pinfalls) {
		this.pinFalls = pinfalls;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Frame(int maxRollsPerFrame) {

		this.pinFalls = new String[maxRollsPerFrame];
		this.score = score;
	}

	@Override
	public String toString() {
		return "Frame [pinFalls=" + Arrays.toString(pinFalls) + ", score=" + score + "]";
	}

	
}
