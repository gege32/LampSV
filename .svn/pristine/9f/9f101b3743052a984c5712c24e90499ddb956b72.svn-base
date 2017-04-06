package hu.gehorvath.lampsv.core.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hu.gehorvath.lampsv.core.jobs.IJob;

public class JobServer {

	ExecutorService threadPool;
	
	public void submitJob(IJob job){
		threadPool.submit(job.run());
	}
	
	public JobServer(){
		threadPool = Executors.newCachedThreadPool();
	}
	
}
