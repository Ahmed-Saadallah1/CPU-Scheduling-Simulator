package algorithms;

import java.util.*;
import model.Process;

// === Priority Non-Preemptive.java ===

public class PriorityNonPreemptive {//(smallest num has highest priority)
	    public static void runPriority(List<Process> processes) {
	        int n = processes.size();
	        int currentTime = 0, completed = 0;

		for (int i = 0; i < n; i++) {
	            processes.get(i).remainingTime = processes.get(i).burstTime;
	            processes.get(i).startTime = -1;
	            processes.get(i).completionTime = -1;
	        }

	        boolean[] isCompleted = new boolean[n];
	        List<Process> executionOrder = new ArrayList<>(); 

	        while (completed < n) {
	            int idx = -1;
	            int highestPriority = Integer.MAX_VALUE; 

	            for (int i = 0; i < n; i++) {
	                Process p = processes.get(i);
	                if (p.arrivalTime <= currentTime && !isCompleted[i]) {
	                    if (p.priority < highestPriority) {
	                        highestPriority = p.priority;
	                        idx = i;
	                    } else if (p.priority == highestPriority) {
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
	        System.out.println("===== PriorityNonPreemptive RESULTS =====");
	        FCFS.printResults(executionOrder);
	    }
}