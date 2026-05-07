package algorithms;

import java.util.*;
import model.Process;

// === SJF Non-Preemptive.java ===

public class SJFNonPreemptive {


	    public static void runSJF(List<Process> processes) {
	        int n = processes.size();
	        double currentTime = 0.0, completed = 0.0;

	        for (int i = 0; i < n; i++) {
	            processes.get(i).remainingTime = processes.get(i).burstTime;
	            processes.get(i).startTime = -1;
	            processes.get(i).completionTime = -1;
	        }

	        boolean[] isCompleted = new boolean[n];
	        List<Process> executionOrder = new ArrayList<>(); 

	        while (completed < n) {
	            int idx = -1;
	            double minBurst = Integer.MAX_VALUE;

	            for (int i = 0; i < n; i++) {
	                Process p = processes.get(i);
	                if (p.arrivalTime <= currentTime && !isCompleted[i]) {
	                    if (p.burstTime < minBurst) {
	                        minBurst = p.burstTime;
	                        idx = i;
	                    } else if (p.burstTime == minBurst) {
	                        if (p.arrivalTime < processes.get(idx).arrivalTime) idx = i;
	                    }
	                }
	            }

	            if (idx == -1) {
	                currentTime++; 
	            } else {
	                Process p = processes.get(idx);
	                p.startTime = currentTime;
	                currentTime += p.burstTime;
	                p.completionTime = currentTime;
	                p.remainingTime = 0;
	                isCompleted[idx] = true;
	                completed++;

	                executionOrder.add(p);
	            }
	        }
	        System.out.println("===== SJF Non-Preemptive RESULTS =====");
	        FCFS.printResults(executionOrder); 
	    }   
	}