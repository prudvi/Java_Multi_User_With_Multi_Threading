import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class multiUserswithThreads {

	public static void main(String[] args) {
		try {
			startMultipleUsers();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void startMultipleUsers() throws InterruptedException {
		int concurrentUsers = 5;
		long start =  System.nanoTime();
		testTimeoutinThread testTimeout = new testTimeoutinThread();
        ExecutorService executor = Executors.newFixedThreadPool(5);
        List<Runnable> awaitingThread = new ArrayList<>();
        List<Future<String>> listStr = new ArrayList<Future<String>>();
        for (int x=1;x<=concurrentUsers; x++ ) {
        	//Thread.sleep(1000);
        	 Future<String> future = executor.submit(new User("user:"+x, x, testTimeout));
        	 //listStr.add(future);
        }	
        long end =  System.nanoTime() - start ;
		System.out.println("Time Taken :: SECONDS :"+TimeUnit.NANOSECONDS.toSeconds(end));
		//System.out.println("Time Taken ::MilliSeconds:"+TimeUnit.NANOSECONDS.toMillis(end));
		
		executor.shutdown();
	}
	
	static class User implements Callable<String> {
	    	String name;
	    	int counter;
	    	ExecutorService executor;
	    	testTimeoutinThread testTimeout;
	    	public User(String name, int counter, testTimeoutinThread timeoutClass) {
	    		this.name = name;
	    		this.counter= counter;
	    		this.testTimeout = timeoutClass;
	    	}
	    	
	        @Override
	        public String call() throws Exception {
	        	////System.out.println(Thread.currentThread().getName() +":User: Started:");
	        	this.testTimeout.mainExecution(this.name);
	            return "User Response!!!!!!!:::"+name;
	        }        
	    }
}
