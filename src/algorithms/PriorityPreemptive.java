package algorithms;

import java.util.*;
import model.Process;

// === Priority Preemptive.java ===

public class PriorityPreemptive {

    public static void runPriorityPreemptive(List<Process> processes) {
        int n = processes.size();
        for (Process p : processes) {
            p.remainingTime = p.burstTime;
            p.startTime = -1;
            p.completionTime = -1;
        }

        double currentTime = 0;
        int completed = 0;
        List<Process> gantt = new ArrayList<>();
        Process lastProcess = null;

        while (completed < n) {
            int idx = -1;
            int highestPriority = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                Process p = processes.get(i);
                if (p.remainingTime > 0 && p.arrivalTime <= currentTime) {
                    if (p.priority < highestPriority ||
                        (p.priority == highestPriority && (idx == -1 || p.arrivalTime < processes.get(idx).arrivalTime))) {
                        highestPriority = p.priority;
                        idx = i;
                    }
                }
            }

            if (idx == -1) {
                double nextArrival = Double.MAX_VALUE;
                for (Process p : processes)
                    if (p.remainingTime > 0 && p.arrivalTime > currentTime)
                        nextArrival = Math.min(nextArrival, p.arrivalTime);

                if (lastProcess == null || !lastProcess.pid.equals("IDLE")) {
                    Process idle = new Process("IDLE", currentTime, nextArrival - currentTime, 0);
                    idle.startTime = currentTime;
                    idle.completionTime = nextArrival;
                    gantt.add(idle);
                    lastProcess = idle;
                } else {
                    // extend existing IDLE block
                    lastProcess.completionTime = nextArrival;
                }

                currentTime = nextArrival;
            } else {
                Process p = processes.get(idx);
                if (p.startTime == -1) p.startTime = currentTime;

                if (lastProcess == null || !lastProcess.pid.equals(p.pid)) {
                    // start new block
                    Process block = new Process(p.pid, p.startTime, p.burstTime, p.priority);
                    block.startTime = currentTime;
                    block.completionTime = currentTime + 1;
                    gantt.add(block);
                    lastProcess = block;
                } else {
                    // extend current block
                    lastProcess.completionTime = currentTime + 1;
                }

                p.remainingTime -= 1.0;
                currentTime += 1.0;

                if (p.remainingTime <= 0) {
                    p.completionTime = currentTime;
                    completed++;
                }
            }
        }

        System.out.println("===== Priority Preemptive RESULTS =====");
        printResults(gantt, processes);
    }

    public static void printResults(List<Process> gantt, List<Process> originalProcesses) {
        if (originalProcesses == null || originalProcesses.isEmpty()) {
            System.out.println("No processes to schedule.");
            return;
        }
        if (gantt == null || gantt.isEmpty()) {
            System.out.println("No execution recorded (gantt empty).");
            return;
        }
        System.out.print("Gantt Chart: |");
        for (Process p : gantt) System.out.print(" " + p.pid + " |");
        System.out.println();

        System.out.print("Time:         ");
        for (Process p : gantt)
            System.out.print(p.startTime + "-" + p.completionTime + "|");
        System.out.println();

        double totalWT = 0, totalTAT = 0, totalBurst = 0;
        System.out.println("PID | WT | TAT | RT | CT");
        for (Process p : originalProcesses) {
            totalWT += p.getWaitingTime();
            totalTAT += p.getTurnaroundTime();
            totalBurst += p.burstTime;
            System.out.println(p.pid + " | " + p.getWaitingTime() + " | " + p.getTurnaroundTime()
                    + " | " + p.getResponseTime() + " | " + p.completionTime);
        }

        double lastEnd = gantt.get(gantt.size() - 1).completionTime;
        double avgWT = totalWT / originalProcesses.size();
        double throughput = originalProcesses.size() / lastEnd;
        double cpuUtil = (totalBurst / lastEnd) * 100;

        System.out.println("Average Waiting Time: " + avgWT);
        System.out.println("Throughput: " + throughput);
        System.out.println("CPU Utilization: " + cpuUtil + "%");
        System.out.println();
    }
}
