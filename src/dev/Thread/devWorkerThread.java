package dev.Thread;

public class devWorkerThread implements Runnable{

	private String command;
	
	public devWorkerThread(String s){
		this.command = s;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println(Thread.currentThread().getName() + "Start. Command = "+ command);
		processCommand();
		System.out.println(Thread.currentThread().getName() + " End.");
	}

	
	private void processCommand(){
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
	
	public String toString(){
		return this.command;
		
	}
}
