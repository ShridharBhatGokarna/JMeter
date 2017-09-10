package utils;

public class TimerThread implements Runnable {

	@Override
	public void run() {

     try {
		Thread.sleep(5000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
	}

}
