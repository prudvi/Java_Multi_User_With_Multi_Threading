################################package multiThreadingTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class testTimeoutinThread {
	
	public static void main(String[] args) {
		testTimeoutinThread testTimeout = new testTimeoutinThread();
		testTimeout.mainExecution(" Raju ");
	}
	
	public void mainExecution(String user) {
		int intitialCount = 57;
		List<String> responseList = multiThreadExecuiton(intitialCount, user);
		System.out.println(user+"::responseList.size::"+responseList.size());
		if (responseList.size() == intitialCount) {
			Iterator<String> iterataor = responseList.iterator();
			while (iterataor.hasNext()) {
				String output = (String) iterataor.next();
				//System.out.println("::Output::"+output);
				
			}
		} else {
			System.out.println(" Failed Mismatch Count for :"+user);
		}
	}
    
	public static List<String> multiThreadExecuiton(int count, String user) {
		
    	long threadTimeoutInMilliSeconds = 3000;
    	
    	 List<String> responseList = new ArrayList<String>();
    	long start =  System.nanoTime();
    	
        ExecutorService executor = Executors.newFixedThreadPool(5);
        List<Runnable> awaitingThread = new ArrayList<>();
        List<Future<String>> listStr = new ArrayList<Future<String>>();
        for (int x=1;x<=count; x++ ) {
        	try {
        	Future<String> future = executor.submit(new Task("Name::"+x, x, executor, user));
        	 listStr.add(future);
        	} catch (RejectedExecutionException e) {
        		//System.out.println(" RejectedExecutionException");
        		e.printStackTrace();
			}
        }
        
        if (listStr.size() != count) {
        	System.out.println(" Exit!!!! THREADS!!!!");
        	//System.exit(0);
        } else {
	        for (Future<String> future: listStr ) {
		        try {
		        	System.out.println("Begins!");
		        	String value = future.get(threadTimeoutInMilliSeconds, TimeUnit.MILLISECONDS);
		            System.out.println( " Value::::"+value);
		            responseList.add(value);
		        } catch (TimeoutException e) {
		        	//System.out.println("Terminated!");
		        	e.printStackTrace();
		            future.cancel(true);
		            if (!executor.isShutdown()) {
		            	 awaitingThread =  executor.shutdownNow();
		            	//System.out.println(":TimeoutException:FIRST::awaitingThread:::"+awaitingThread.size());
		            	awaitingThread =  executor.shutdownNow();
		            	//System.out.println(":TimeoutException:SECOND:awaitingThread:::"+awaitingThread.size());
		            }
		            break;
		        } catch (Exception e) {
		        	
		        	if (!executor.isShutdown()) {
		            	 awaitingThread =  executor.shutdownNow();
		            	//System.out.println(":Other Exception:FIRST::awaitingThread:::"+awaitingThread.size());
		            	awaitingThread =  executor.shutdownNow();
		            	//System.out.println(": Other Exception:SECOND:awaitingThread:::"+awaitingThread.size());
		            }
		        	break;
		        } 
	        }
        }
        
        
        if (!executor.isShutdown()) {
        	awaitingThread = executor.shutdownNow();
        	//System.out.println(":Finally::awaitingThread:::"+awaitingThread.size());
        	awaitingThread =  executor.shutdownNow();
        	//System.out.println(":Finally:SECOND:awaitingThread:::"+awaitingThread.size());
        }
        
        //System.out.println(awaitingThread.size()+"::Date::"+new Date());
        long end =  System.nanoTime() - start ;
		System.out.println("Time Taken :: SECONDS :"+TimeUnit.NANOSECONDS.toSeconds(end));
		//System.out.println("Time Taken ::MilliSeconds:"+TimeUnit.NANOSECONDS.toMillis(end));
		
		return responseList;
    }
    
    static class Task implements Callable<String> {
    	String name;
    	int counter;
    	ExecutorService executor;
    	String user;
    	public Task(String name, int counter, ExecutorService executor, String user) {
    		this.name = name;
    		this.counter= counter;
    		this.executor = executor;
    		this.user = user;
    	}
    	
        @Override
        public String call() throws Exception {
        	System.out.println(Thread.currentThread().getName() +": Started:"+this.counter%10);
        	if (this.counter % 5 == 0) {
        		if (!this.executor.isShutdown() ) {
        			List awaitingThread = executor.shutdownNow();
                	//System.out.println(":call::awaitingThread:::"+awaitingThread.size());
                	awaitingThread =  executor.shutdownNow();
                	//System.out.println(":call :SECOND:awaitingThread:::"+awaitingThread.size());
        		}
        	}
            else
        		Thread.sleep(200);
        	
//        	if (!user.equals("user:3")) {
//        		if (!this.executor.isShutdown() ) {
//        			List awaitingThread = executor.shutdownNow();
//                	System.out.println(":call::awaitingThread:::"+awaitingThread.size());
//                	awaitingThread =  executor.shutdownNow();
//                	System.out.println(":call :SECOND:awaitingThread:::"+awaitingThread.size());
//        		}
//        	} else {
//        		Thread.sleep(200);
//        	}
        	
//        	if (this.counter % 5 == 0) {
//        		Thread.sleep(4000);
//        	} else
//        	Thread.sleep(200);
            return "return obligation:::!!!"+name;
        }        
    }

    
}



