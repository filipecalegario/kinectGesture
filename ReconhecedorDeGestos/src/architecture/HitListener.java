package architecture;

import processing.core.PApplet;

public class HitListener extends SkeletonListener{

	private HitAnalyzer hta;

	public HitListener(PApplet main, SkeletonAnalyzer skeletonAnalyzer) {
		super(main, skeletonAnalyzer);
		this.hta = new HitAnalyzer(main, skeletonAnalyzer, this);
	}

	@Override
	public void update() {
		hta.analyze();
	}
	
	
}
