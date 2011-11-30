package game;

import java.util.HashMap;

import music.MusicModule;
import processing.core.PApplet;
import processing.core.PVector;
import basic.Rectangle;
import basic.Utils;

public class GameModule {

	MusicModule mm;
	HashMap<GameResult, Integer> counters;

	GameResult gr;
	GameStatus gameStatus;
	public Rectangle gameRectangle;

	PApplet main;

	int statusCounter = 0;

	public GameModule(PApplet main, MusicModule mm) {
		super();
		this.main = main;
		this.mm = mm;
		this.counters = new HashMap<GameResult, Integer>();
		this.populateCounters();

		this.gr = GameResult.OK;
		this.gameStatus = GameStatus.STATUS1;

		this.gameRectangle = new Rectangle();

	}

	private void populateCounters() {
		GameResult gr[] = GameResult.values();
		for (int i = 0; i < gr.length; i++) {
			this.counters.put(gr[i], 0);
		}
	}

	public void incrementCounter(GameResult gr, int value) {
		int current = this.counters.get(gr);
		current = current + value;
		this.counters.put(gr, current);
	}

	public HashMap<GameResult, Integer> getCounters() {
		return counters;
	}

	public void process() {
		for (GameResult r : this.counters.keySet()) {
			Integer currentCounter = this.counters.get(r);
			if (r.equals(GameResult.MISS) && currentCounter % 5 == 0) {
				int value = Math.max(0, Math.min(currentCounter * 2, 127));
				//System.out.println(value);
				mm.sendCC(2, PApplet.map(value, 0, 127, 0, 1));
			}
		}
	}

	@Override
	public String toString() {
		String result = "";
		result = result + "[ ";
		for (GameResult r : this.counters.keySet()) {
			result = result + r.name() + " = " + this.counters.get(r) + ", ";
		}
		result = result + "]";
		return result;
	}

	public void updateGame() {
		switch (gameStatus) {
		case STATUS1:
			PVector rectPoint = new PVector(main.width / 2, 50);
			float angle = main.random(-main.PI / 2, main.PI / 2);
			float rectLength = main.random(50, 250);
			float rectHeight = 30;
			gameRectangle = new Rectangle(rectLength, rectHeight, angle,
					rectPoint, main.color(127, 0, 127));
			gameRectangle.setLifeCycle(40);
			gameStatus = GameStatus.STATUS2;
			statusCounter = 0;
			break;
		case STATUS2:
			mm.playNote(60, 120, 100);
			gameRectangle.render(main);
			if (statusCounter == 100) {
				Utils.writeOnScreen(main, "MISS", main.color(255, 0, 0));
				incrementCounter(GameResult.MISS, 1);
				gameStatus = GameStatus.STATUS1;
			}
			statusCounter++;
			break;
		case STATUS3:
			switch (gr) {
			case PERFECT:
				Utils.writeOnScreen(main, "PERFECT", main.color(0, 255, 0));
				break;
			case OK:
				Utils.writeOnScreen(main, "GOOD", main.color(0, 0, 255));
				break;
			case TERRIBLE:
				Utils.writeOnScreen(main, "TERRIBLE", main.color(255, 0, 0));
				break;

			default:
				break;
			}
			if (statusCounter == 30) {
				gameStatus = GameStatus.STATUS1;
			}
			statusCounter++;
			break;

		default:
			break;
		}
		this.process();
	}

	public void checkSimilarity(Rectangle userRectangle) {

		float perfectTolerance = main.PI * 0.05f;
		float goodTolerance = main.PI * 0.20f;
		float diff = Math.abs(userRectangle.getAngle()
				- this.gameRectangle.getAngle());

		if (diff < perfectTolerance) {
			gr = GameResult.PERFECT;
		} else if (diff < goodTolerance) {
			gr = GameResult.OK;
		} else {
			gr = GameResult.TERRIBLE;
		}

		this.statusCounter = 0;
		this.gameStatus = GameStatus.STATUS3;
	}
	// public static void main(String[] args) {
	// GameModule gm = new GameModule(null);
	// System.out.println(gm);
	// gm.incrementCounter(GameResult.MISS, 5);
	// System.out.println(gm);
	// }

}
