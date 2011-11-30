package architecture;

import processing.core.PApplet;

public class HandsTogetherListener extends SkeletonListener {
	
	private HandsTogetherAnalyzer hta;

	public HandsTogetherListener(PApplet main, SkeletonAnalyzer skeletonAnalyzer) {
		super(main, skeletonAnalyzer);
		this.hta = new HandsTogetherAnalyzer(main, skeletonAnalyzer, this);
	}

	@Override
	public void update() {
		hta.analyze();
	}

}
