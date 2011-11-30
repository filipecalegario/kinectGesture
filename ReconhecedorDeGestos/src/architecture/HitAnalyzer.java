package architecture;

import processing.core.PApplet;
import processing.core.PVector;
import basic.Rectangle;
import body.Skeleton;

public class HitAnalyzer implements SimpleAnalyzer{
	
	private PApplet main;
	private SkeletonAnalyzer sa;
	private HitListener hl;
	
	float lastDistanceHandTorso;
	boolean forwardLock = false;
	int counter;

	public HitAnalyzer(PApplet main, SkeletonAnalyzer sa, HitListener hl) {
		super();
		this.main = main;
		this.sa = sa;
		this.hl = hl;
	}

	public void analyze(){
		for (Skeleton s : sa.skels.values()) {
			float currentDistanceHandTorso = PVector.dist((s.lHandCoords),
					(s.lShoulderCoords));
			float abs = Math.abs(currentDistanceHandTorso - lastDistanceHandTorso);
			
			if (abs > 0.03f && !forwardLock) {
				//Rectangle userRectangle = new Rectangle(rectLength, rectHeight, angle, new PVector(rectX, rectY), main.color(127));
				//rr.add(userRectangle);
				forwardLock = true;
				//game.checkSimilarity(userRectangle);
				counter = 0;
			}
			if (counter == 10) {
				counter = 0;
				forwardLock = false;
			}
			counter++;
			lastDistanceHandTorso = currentDistanceHandTorso;
		}
	}

}
