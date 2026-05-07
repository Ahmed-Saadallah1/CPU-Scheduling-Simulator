package algorithms;          

import java.util.*;
import model.Process;        

// === FCFS.java ===

public class FCFS {

    public static void runFCFS(List<Process> processes) {

        for (int i = 0; i < processes.size() - 1; i++) {
            for (int j = 0; j < processes.size() - 1 - i; j++) {
                if (processes.get(j).arrivalTime > processes.get(j + 1).arrivalTime) {
                    Process temp = processes.get(j);
                    processes.set(j, processes.get(j + 1));
                    processes.set(j + 1, temp);
                }
            }
        }

        double currentTime = 0;
        for (int i = 0; i < processes.size(); i++) {
            Process p = processes.get(i);
            if (currentTime < p.arrivalTime) currentTime = p.arrivalTime;
            p.startTime = currentTime;
            currentTime += p.burstTime;
            p.completionTime = currentTime;
            p.remainingTime = 0;
        }

        System.out.println("===== FCFS RESULTS =====");
        printResults(processes);
    }

    public static void printResults(List<Process> processes) {
        List<Process> ganttWithIdle = new ArrayList<>();
        double lastEnd = 0.0;

        for (int i = 0; i < processes.size(); i++) {
            Process p = processes.get(i);
            if (p.startTime > lastEnd) {
                Process idle = new Process("IDLE", lastEnd, 0.0, 0);
                idle.startTime = lastEnd;
                idle.completionTime = p.startTime;
                ganttWithIdle.add(idle);
            }
            ganttWithIdle.add(p);
            lastEnd = p.completionTime;
        }

        System.out.print("Gantt Chart: |");
        for (int i = 0; i < ganttWithIdle.size(); i++)
            System.out.print(" " + ganttWithIdle.get(i).pid + " |");
        System.out.println();

        System.out.print("Time:         ");
        for (int i = 0; i < ganttWithIdle.size(); i++) {
            Process p = ganttWithIdle.get(i);
            System.out.print(p.startTime + "-" + p.completionTime + "|");
        }
        System.out.println();

        double totalWT = 0, totalTAT = 0, totalBurst = 0;
        System.out.println("PID | WT | TAT | RT | CT");
        for (int i = 0; i < processes.size(); i++) {
            Process p = processes.get(i);
            if (!p.pid.equals("IDLE")) {
                totalWT += p.getWaitingTime();
                totalTAT += p.getTurnaroundTime();
                totalBurst += p.burstTime;
                System.out.println(p.pid + " | " + p.getWaitingTime() + " | " + p.getTurnaroundTime()
                        + " | " + p.getResponseTime() + " | " + p.completionTime);
            }
        }

        double avgWT = totalWT / processes.size();
        double throughput = (double) processes.size() / lastEnd;
        double cpuUtil = (totalBurst / lastEnd) * 100;
        System.out.println("Average Waiting Time: " + avgWT);
        System.out.println("Throughput: " + throughput);
        System.out.println("CPU Utilization: " + cpuUtil + "%");
        System.out.println();
    }
}