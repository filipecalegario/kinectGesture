package architecture;

import processing.core.PApplet;
import processing.core.PVector;
import basic.Rectangle;
import basic.RectangleRepository;
import basic.Utils;
import body.Skeleton;

public class HandsTogetherAnalyzer implements SimpleAnalyzer{

	private PApplet main;
	private SkeletonAnalyzer sa;
	private HandsTogetherListener htl;

	private RectangleRepository rr;
	private boolean rectDrawStatus = false;
	private boolean lockDist = false;
	
	public HandsTogetherAnalyzer(PApplet main, SkeletonAnalyzer skeletonAnalyzer,
			HandsTogetherListener handsTogetherListener) {
		this.main = main;
		this.sa = skeletonAnalyzer;
		this.htl = handsTogetherListener;	
	}
	
	public void analyze(){
		for (Skeleton s : sa.skels.values()) {
			PVector leftHand = Utils.convertToScreen(main, s.lHandCoords);
			PVector rightHand = Utils.convertToScreen(main, s.rHandCoords);

			float dist = PVector.dist(s.lHandCoords, s.rHandCoords);
			float angle = Utils.calculateAngle(s.lHandCoords, s.rHandCoords);

			float rectX = leftHand.x - (leftHand.x - rightHand.x) / 2;
			float rectY = leftHand.y - (leftHand.y - rightHand.y) / 2;
			float rectLength = PVector.dist(leftHand, rightHand);
			float rectHeight = 30;
			
			if (dist < 0.05 && lockDist == false) {
				if (rectDrawStatus == false) {
					rectDrawStatus = true;
				} else {
					rectDrawStatus = false;
				}
				lockDist = true;
				
				this.htl.onAction();

			} else if (dist > 0.06) {
				lockDist = false;

				this.htl.offAction();
			}

			float mappedAngle = PApplet.map(angle, -PApplet.PI / 2, PApplet.PI / 2, 0, 1);
			float colorHue = mappedAngle * 100;

			if (rectDrawStatus == true) {
				main.registerDraw(this);
				
				Object[] oargs = {new Float(angle)};
				this.htl.updateAction(oargs);
			} else {
				main.unregisterDraw(this);
			}

//			float currentDistanceHandTorso = PVector.dist((s.lHandCoords),
//					(s.lShoulderCoords));
//			float abs = Math.abs(currentDistanceHandTorso - lastDistanceHandTorso);
//
//			if (abs > 0.03f && !forwardLock) {
//				Rectangle userRectangle = new Rectangle(rectLength, rectHeight, angle, new PVector(rectX, rectY), main.color(127));
//				rr.add(userRectangle);
//				forwardLock = true;
//				//game.checkSimilarity(userRectangle);
//				counter = 0;
//			}
//			if (counter == 10) {
//				counter = 0;
//				forwardLock = false;
//			}
//			counter++;
//			lastDistanceHandTorso = currentDistanceHandTorso;
		}
	}
	
	public void draw() {
//		main.pushMatrix();
//		main.translate(rectX, rectY);
//		main.rotateZ(angle);
//		main.colorMode(PApplet.HSB, 100);
//		main.fill(colorHue, 100, 100);
//		main.colorMode(PApplet.RGB, 255);
//		main.rect(-rectLength / 2, -rectHeight / 2, rectLength, rectHeight);
//		main.popMatrix();
	}
	
	private void drawRectangles() {
		rr.process();
		rr.draw();
	}

}
