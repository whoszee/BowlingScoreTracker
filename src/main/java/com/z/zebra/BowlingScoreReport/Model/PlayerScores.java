package com.z.zebra.BowlingScoreReport.Model;

import java.util.Arrays;

public class PlayerScores {

	private String playerName;
	private Frame[] playerFrames;
	
	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public Frame[] getPlayerFrames() {
		return playerFrames;
	}

	public void setPlayerFrames(Frame[] playerFrames) {
		this.playerFrames = playerFrames;
	}

	public PlayerScores(int maxFrames) {
		this.playerFrames = new Frame[maxFrames];
		this.playerName = playerName;
	}

	@Override
	public String toString() {
		return "PlayerScores [playerName=" + playerName + ", playerFrames=" + Arrays.toString(playerFrames) + "]";
	}

	
}
