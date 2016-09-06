package com.z.zebra.BowlingScoreReport;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.z.zebra.BowlingScoreReport.Model.Frame;
import com.z.zebra.BowlingScoreReport.Model.PlayerScores;

import static com.z.zebra.BowlingScoreReport.Constants.BowlingGameConstants.FOUL;
import static com.z.zebra.BowlingScoreReport.Constants.BowlingGameConstants.MAX_FRAMES;
import static com.z.zebra.BowlingScoreReport.Constants.BowlingGameConstants.MAX_PINS;
import static com.z.zebra.BowlingScoreReport.Constants.BowlingGameConstants.MAX_ROLLS_PER_FRAME;
import static com.z.zebra.BowlingScoreReport.Constants.BowlingGameConstants.NO_PINS;
import static com.z.zebra.BowlingScoreReport.Constants.BowlingGameConstants.SPARE;
import static com.z.zebra.BowlingScoreReport.Constants.BowlingGameConstants.STRIKE;
import static com.z.zebra.BowlingScoreReport.Constants.BowlingGameConstants.DISPLAY_DELIMITER;

public class ScoreCard {

	Multimap<String, String> playerScores = ArrayListMultimap.create();
	ArrayList<String> inputScores = new ArrayList<String>();
	PlayerScores outputScores = new PlayerScores(MAX_FRAMES);

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ScoreCard reader = new ScoreCard();

		try {
			reader.run(args);
		} catch (Exception e) {
			System.exit(1);
		}
	}

	private void run(String... args) {
		// TODO Auto-generated method stub

		System.out.print("Frame" + DISPLAY_DELIMITER + DISPLAY_DELIMITER);
		for (int frame = 1; frame <= MAX_FRAMES; frame++) {

			System.out.print(frame + DISPLAY_DELIMITER + DISPLAY_DELIMITER);
		}
		System.out.println();

		parseInput(args);
		processData();

	}

	private void processData() {
		// TODO Auto-generated method stub

		try {
			for (String player : playerScores.keySet()) {

				outputScores.setPlayerName(player);

				int frameCount = 0;

				int totalScoreForPlayer = 0;

				List<String> currentPlayerPinfalls = new ArrayList<String>(playerScores.get(player));

				Frame[] listOfFrames = new Frame[MAX_FRAMES];
				Frame lastFrame = new Frame(MAX_ROLLS_PER_FRAME + 1);
				String[] lastFramePinFalls = new String[MAX_ROLLS_PER_FRAME + 1];
				for (int x = 0; x < (currentPlayerPinfalls.size()) - 3; x++) {

					int score = handleFoul(currentPlayerPinfalls.get(x));

					if (frameCount < (MAX_FRAMES - 1)) {

						// Strikes
						if (score == MAX_PINS) {

							Frame frame = new Frame(MAX_ROLLS_PER_FRAME);
							String[] framePinFalls = new String[MAX_ROLLS_PER_FRAME];

							framePinFalls[0] = "";
							framePinFalls[1] = "X";
							frame.setPinfalls(framePinFalls);

							// 10 + 7 + 3
							String[] strikeScores = new String[3];
							strikeScores[0] = String.valueOf(STRIKE);

							for (int y = 0; y < 2; y++) {
								strikeScores[y + 1] = currentPlayerPinfalls.get((x + 1) + y);
							}

							totalScoreForPlayer = totalScoreForPlayer + processScore(strikeScores);
							frame.setScore(totalScoreForPlayer);

							listOfFrames[frameCount] = frame;
							frameCount++;

							outputScores.setPlayerFrames(listOfFrames);

						} else {

							Frame frame = new Frame(MAX_ROLLS_PER_FRAME);

							String[] framePinFalls = new String[MAX_ROLLS_PER_FRAME];

							if (score + (handleFoul(currentPlayerPinfalls.get(x + 1))) == MAX_PINS) {

								framePinFalls[0] = String.valueOf(currentPlayerPinfalls.get(x));

								framePinFalls[1] = "/";

								frame.setPinfalls(framePinFalls);

								totalScoreForPlayer = totalScoreForPlayer
										+ (processScore((currentPlayerPinfalls.get(x + 2)), String.valueOf(SPARE)));

								frame.setScore(totalScoreForPlayer);
								listOfFrames[frameCount] = frame;
								outputScores.setPlayerFrames(listOfFrames);
								frameCount++;

								x++;

							} else if (currentPlayerPinfalls.get(x).equalsIgnoreCase(FOUL)
									&& currentPlayerPinfalls.get(x + 1).equalsIgnoreCase(FOUL)) {

								String[] framePinFallsFoul = new String[MAX_ROLLS_PER_FRAME];

								framePinFallsFoul[0] = String.valueOf(currentPlayerPinfalls.get(x));

								framePinFallsFoul[1] = currentPlayerPinfalls.get(x + 1);

								frame.setPinfalls(framePinFallsFoul);

								listOfFrames[frameCount] = frame;

								outputScores.setPlayerFrames(listOfFrames);

								frameCount++;
								x++;

							} else {

								framePinFalls[0] = String.valueOf(currentPlayerPinfalls.get(x));

								framePinFalls[1] = currentPlayerPinfalls.get(x + 1);

								frame.setPinfalls(framePinFalls);

								totalScoreForPlayer = totalScoreForPlayer + processScore((currentPlayerPinfalls.get(x)),
										currentPlayerPinfalls.get(x + 1));
								frame.setScore(totalScoreForPlayer);
								listOfFrames[frameCount] = frame;
								outputScores.setPlayerFrames(listOfFrames);
								frameCount++;

								x++;

							}
						}
					}
				}

				int y = 0;
				for (int x = currentPlayerPinfalls.size() - 3; x < (currentPlayerPinfalls.size()); x++) {
					String tempPinFall = null;
					if (currentPlayerPinfalls.get(x).equals(String.valueOf(10))) {
						lastFramePinFalls[y] = "X";
						y++;
					} else {
						lastFramePinFalls[y] = currentPlayerPinfalls.get(x);
						y++;
					}
				}

				lastFrame.setPinfalls(lastFramePinFalls);
				lastFrame.setScore(totalScoreForPlayer + processScore(lastFramePinFalls));
				listOfFrames[frameCount] = lastFrame;
				outputScores.setPlayerFrames(listOfFrames);

				// System.out.println(outputScores);

				display(outputScores);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void display(PlayerScores outputScores2) {
		// TODO Auto-generated method stub
		System.out.println(outputScores2.getPlayerName());
		System.out.print("Pinfalls" + DISPLAY_DELIMITER);

		for (Frame playerFrame : outputScores2.getPlayerFrames()) {

			for (String singleRoll : playerFrame.getPinfalls()) {
				System.out.print(singleRoll + DISPLAY_DELIMITER);
			}
		}

		System.out.println();
		System.out.print("Score" + DISPLAY_DELIMITER + DISPLAY_DELIMITER);

		for (Frame playerFrame : outputScores2.getPlayerFrames()) {

			System.out.print(playerFrame.getScore() + DISPLAY_DELIMITER + DISPLAY_DELIMITER);
		}

		System.out.println();

	}

	private int processScore(String... framePinFalls) {
		// TODO Auto-generated method stub
		// if not F then add all

		// System.out.println("Processing list:
		// "+Arrays.toString(framePinFalls));
		int score = 0;
		int temp = 0;

		for (String singleRoll : framePinFalls) {
			if (singleRoll.equalsIgnoreCase(FOUL)) {
				score = score + temp;
			} else if (singleRoll.equalsIgnoreCase("X")) {
				score = score + 10;
			} else {
				score = score + Integer.valueOf(singleRoll);
			}
		}
		return score;
	}

	private int handleFoul(String pinFalls) {
		int score;

		if (pinFalls.equalsIgnoreCase(FOUL)) {
			score = NO_PINS;
		} else {
			score = Integer.valueOf(pinFalls);
		}

		return score;
	}

	private void parseInput(String... args) {
		// TODO Auto-generated method stub
		try {
			FileInputStream fstream = new FileInputStream(args[0]);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;

			while ((strLine = br.readLine()) != null) {
				inputScores.add(strLine);
			}

			fstream.close();
			// System.out.println("Data parsed successfully");
			// System.out.println("Number of lines: " + inputScores.size());
			String[] splitData = null;

			for (int lineCount = 0; lineCount <= (inputScores.size() - 1); lineCount++) {
				String line = (inputScores.get(lineCount));
				splitData = line.split("\\s+");
				playerScores.put(splitData[0], splitData[1]);
			}

			// System.out.println("Scores multimap size: " +
			// playerScores.size());

		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
}