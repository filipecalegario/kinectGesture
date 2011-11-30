package basic;

public class TimerThread extends Thread {
	
	boolean flag = false;
	
	@Override
	public void run() {
		
	}
	
	public boolean isFlag() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		flag = !flag;
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

}
