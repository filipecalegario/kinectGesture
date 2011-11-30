package basic;
import processing.core.PApplet;
import processing.core.PVector;


public class Utils {
	
	public static final int Z_MULTIPLIER = 100;
	public static final int BALL_SIZE = 10;

	
	public static PVector copyPVector(PVector vector){
		return new PVector(vector.x, vector.y, vector.z);
	} 
	
	@Deprecated
	public static float calculateAngle(PVector v1, PVector v2){
		double catetoOposto = v2.y - v1.y;
		double catetoAdjacente = v2.x - v1.x;
		double tan = catetoOposto/catetoAdjacente;
		double radians = Math.atan(tan);
		return (float)(radians);
	}
	
	public static void linePVector(PApplet main, PVector v1, PVector v2) {
		main.line(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z);
	}

	public static PVector convertToScreen(PApplet main, PVector v1) {
		return new PVector(v1.x * main.width, v1.y * main.height, -v1.z * Z_MULTIPLIER);
	}
	
	public static void writeOnScreen(PApplet main, String txt, int color){
		main.textFont(main.loadFont("CourierNew36.vlw"), 50);
		float textWidth = main.textWidth(txt);
		float textHeight = main.textAscent();
		float x = main.width / 2 - textWidth / 2;
		float y = main.height / 2 - textHeight / 2;
		//fill(127, 50);
		//rect(x, y, textWidth, textHeight);
		main.fill(color);
		main.text(txt, x, y);
	}
	
}
