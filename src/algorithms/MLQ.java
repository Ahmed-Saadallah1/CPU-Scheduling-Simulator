package algorithms;

import java.util.*;
import model.Process;

// === MLQ.java ===

public class MLQ {

    public static void runMLQ(List<Process> processes) {

        List<Process> Q1 = new ArrayList<>();
        List<Process> Q2 = new ArrayList<>();
        List<Process> Q3 = new ArrayList<>();

        for (int i = 0; i < processes.size(); i++) {
            Process p = processes.get(i);

            if (p.priority <= 2) {
                Q1.add(new Process(p.pid, p.arrivalTime, p.burstTime, p.priority));
            } 
            else if (p.priority <= 4) {
                Q2.add(new Process(p.pid, p.arrivalTime, p.burstTime, p.priority));
            } 
            else {
                Q3.add(new Process(p.pid, p.arrivalTime, p.burstTime, p.priority));
            }
        }

        System.out.println("=== Multi Level Queue Scheduling ===");

        System.out.println("\nQueue 1 (RR q=2):");
        RoundRobin.runRoundRobin(Q1, 2.0);

        System.out.println("\nQueue 2 (FCFS):");
        FCFS.runFCFS(Q2);

        System.out.println("\nQueue 3 (Priority Preemptive):");
        PriorityPreemptive.runPriorityPreemptive(Q3);
    }
}
