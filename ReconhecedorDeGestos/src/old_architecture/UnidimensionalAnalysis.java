package old_architecture;
public class UnidimensionalAnalysis {

	float currentValue;
	float lastValue;
	float filterValue;
	String currentStatus = "";
	String lastStatus = "";

	public UnidimensionalAnalysis(){
		 
	}
	
	public UnidimensionalAnalysis(float value, float filter) {
		this.currentValue = value;
		this.lastValue = value;
		this.filterValue = filter;
	}

	public UnidimensionalEvent analyse(float currentValue) {
		UnidimensionalEvent event = null;
		
		this.currentValue = currentValue;
		
		this.filter();
		event = this.check();
		this.lastValue = this.currentValue;
		this.lastStatus = this.currentStatus;
		
		return event;
		
	}

	private void filter() {
		float filterDiff = Math.abs(currentValue - lastValue);

		if (filterDiff < 2) {
			currentValue = lastValue;
		}
	}

	private UnidimensionalEvent check() {
		UnidimensionalEvent event = UnidimensionalEvent.NONE;
		
		float diff = currentValue - lastValue;
		//System.out.println("Cur: " + currentValue + " Last: " + lastValue);

		if (diff <= 0) {
			currentStatus = "red"; // curStatusRight = "red";
		} else {
			currentStatus = "green"; // curStatusRight = "green";
		}

//		//System.out.println("Cur: " + currentStatus + " Last: " + lastStatus);
//		if (currentStatus == true && lastStatus == false) {
//			event = UnidimensionalEvent.PEAKDOWN;
//		} else if (currentStatus == false && lastStatus == true){
//			event = UnidimensionalEvent.PEAKUP;
//		}
		
		if(currentStatus.equals("green") && lastStatus.equals("red")){
			event = UnidimensionalEvent.PEAKDOWN;
		} else if(currentStatus.equals("red") && lastStatus.equals("green")){
			event = UnidimensionalEvent.PEAKUP;
		}
		
		return event;
	}

	public void setCurrentValue(float currentValue) {
		this.currentValue = currentValue;
	}

}
