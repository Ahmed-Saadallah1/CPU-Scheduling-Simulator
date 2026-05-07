package algorithms;

import java.util.*;
import model.Process;

// === Round Robin.java ===

public class RoundRobin {

    private static final double DEFAULT_QUANTUM = 2.0;

    public static void runRoundRobin(List<Process> processes) {
        runRoundRobin(processes, DEFAULT_QUANTUM);
    }

    public static void runRoundRobin(List<Process> processes, double quantum) {

        processes.sort(Comparator.comparingDouble(p -> p.arrivalTime));

        Queue<Process> readyQueue = new LinkedList<>();
        List<Process> gantt = new ArrayList<>();

        double currentTime = 0.0;
        int nextIndex = 0;

        while (!readyQueue.isEmpty() || nextIndex < processes.size()) {

            if (readyQueue.isEmpty()) {
                Process next = processes.get(nextIndex);

                if (currentTime < next.arrivalTime) {
                    Process idle = new Process("IDLE", currentTime, 0.0, 0);
                    idle.startTime = currentTime;
                    idle.completionTime = next.arrivalTime;
                    gantt.add(idle);

                    currentTime = next.arrivalTime;
                }

                readyQueue.add(next);
                nextIndex++;
            }

            Process p = readyQueue.poll();

            if (p.startTime < 0) {
                p.startTime = currentTime;
            }

            double runTime = Math.min(quantum, p.remainingTime);

            Process slice = new Process(p.pid, p.arrivalTime, p.burstTime, p.priority);
            slice.startTime = currentTime;
            slice.completionTime = currentTime + runTime;
            gantt.add(slice);

            currentTime += runTime;
            p.remainingTime -= runTime;

            while (nextIndex < processes.size()
                    && processes.get(nextIndex).arrivalTime <= currentTime) {
                readyQueue.add(processes.get(nextIndex));
                nextIndex++;
            }

            if (p.remainingTime > 0) {
                readyQueue.add(p);
            } else {
                p.completionTime = currentTime;
            }
        }

        System.out.println("===== ROUND ROBIN RESULTS (q = " + quantum + ") =====");
        printResults(processes, gantt);
    }

    private static void printResults(List<Process> processes, List<Process> gantt) {

        System.out.print("Gantt Chart: |");
        for (Process seg : gantt) {
            System.out.print(" " + seg.pid + " |");
        }
        System.out.println();

        System.out.print("Time:         ");
        for (Process seg : gantt) {
            System.out.print(seg.startTime + "-" + seg.completionTime + "|");
        }
        System.out.println();

        double totalWT = 0.0;
        double totalTAT = 0.0;
        double totalBurst = 0.0;
        double lastEnd = 0.0;

        System.out.println("PID | WT | TAT | RT | CT");
        for (Process p : processes) {
            totalWT += p.getWaitingTime();
            totalTAT += p.getTurnaroundTime();
            totalBurst += p.burstTime;
            if (p.completionTime > lastEnd) {
                lastEnd = p.completionTime;
            }

            System.out.println(
                    p.pid + " | " +
                    p.getWaitingTime() + " | " +
                    p.getTurnaroundTime() + " | " +
                    p.getResponseTime() + " | " +
                    p.completionTime
            );
        }

        double avgWT = totalWT / processes.size();
        double throughput = processes.size() / lastEnd;
        double cpuUtil = (totalBurst / lastEnd) * 100.0;

        System.out.println("Average Waiting Time: " + avgWT);
        System.out.println("Throughput: " + throughput);
        System.out.println("CPU Utilization: " + cpuUtil + "%");
        System.out.println();
    }
}