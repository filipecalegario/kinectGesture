package basic;
import java.util.ArrayList;


import processing.core.PApplet;
import processing.core.PVector;

public class RectangleRepository {
	
	private ArrayList<Rectangle> rectangles;
	private PApplet main;
	
	public RectangleRepository(PApplet main){
		this.rectangles = new ArrayList<Rectangle>();
		this.main = main;
	}
	
	public void add(Rectangle rec){
		rec.setSpeed(10);
		rec.setLifeCycle(40);
		this.rectangles.add(rec);
	}
	
	public void process(){
		for (int i = 0; i < rectangles.size(); i++) {
			Rectangle rec = rectangles.get(i);
			if(rec.getLifeCycle() != 0){
				PVector v = rec.getPoint();
				v.y = v.y - rec.getSpeed();
				rec.setLifeCycle(rec.getLifeCycle() - 1);
			} else {
				rectangles.remove(i);
			}
		}
		//System.out.println(rectangles.size());
	}
	
	public void draw(){
		for (Rectangle rec : rectangles) {
			rec.render(main);
		}
	}

	public boolean isEmpty() {
		return rectangles.isEmpty();
	}

}
