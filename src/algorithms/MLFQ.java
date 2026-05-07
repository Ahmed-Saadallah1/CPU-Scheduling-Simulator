package algorithms;

import java.util.*;
import model.Process;

// === MLFQ.java ===

public class MLFQ {

    private static final int Q1_QUANTUM = 2;
    private static final int Q2_QUANTUM = 4;

    public static void runMLFQ(List<Process> processes) {
        if (processes == null || processes.isEmpty()) {
            System.out.println("No processes to schedule.");
            return;
        }

        processes.sort(Comparator.comparingDouble(p -> p.arrivalTime));
        for (Process p : processes) {
            p.remainingTime = p.burstTime;
            p.startTime = -1;
            p.completionTime = -1;
        }

        Queue<Process> q1 = new ArrayDeque<>();
        Queue<Process> q2 = new ArrayDeque<>();
        Queue<Process> q3 = new ArrayDeque<>();
        List<Process> gantt = new ArrayList<>();

        double time = 0.0;
        int nextIndex = 0;
        int total = processes.size();

        while (nextIndex < total || !q1.isEmpty() || !q2.isEmpty() || !q3.isEmpty()) {
            while (nextIndex < total && processes.get(nextIndex).arrivalTime <= time) {
                q1.add(processes.get(nextIndex));
                nextIndex++;
            }

            if (q1.isEmpty() && q2.isEmpty() && q3.isEmpty()) {
                double nextArrival = processes.get(nextIndex).arrivalTime;
                appendToGantt(gantt, "IDLE", time, nextArrival, 0);
                time = nextArrival;
                continue;
            }

            Queue<Process> currentQueue;
            int quantum;
            if (!q1.isEmpty()) {
                currentQueue = q1;
                quantum = Q1_QUANTUM;
            } else if (!q2.isEmpty()) {
                currentQueue = q2;
                quantum = Q2_QUANTUM;
            } else {
                currentQueue = q3;
                quantum = Integer.MAX_VALUE; // FCFS, only preempted by new Q1 arrivals
            }

            Process current = currentQueue.poll();
            if (current.startTime < 0) {
                current.startTime = time;
            }

            double plannedRun = Math.min(current.remainingTime, quantum);
            double nextArrivalTime = nextIndex < total ? processes.get(nextIndex).arrivalTime : Double.MAX_VALUE;
            boolean preemptForTopQueue = currentQueue != q1 && nextArrivalTime < time + plannedRun;
            double actualRun = preemptForTopQueue ? nextArrivalTime - time : plannedRun;

            appendToGantt(gantt, current.pid, time, time + actualRun, current.priority);

            current.remainingTime -= actualRun;
            time += actualRun;

            while (nextIndex < total && processes.get(nextIndex).arrivalTime <= time) {
                q1.add(processes.get(nextIndex));
                nextIndex++;
            }

            if (current.remainingTime <= 0) {
                current.completionTime = time;
                continue;
            }

            boolean usedFullQuantum = (quantum != Integer.MAX_VALUE && actualRun >= quantum)
                    || (quantum == Integer.MAX_VALUE && !preemptForTopQueue);

            if (currentQueue == q1) {
                if (usedFullQuantum) q2.add(current);
                else q1.add(current);
            } else if (currentQueue == q2) {
                if (preemptForTopQueue || !usedFullQuantum) {
                    q2.add(current); // preempted before using full quantum
                } else {
                    q3.add(current); // demote after consuming Q2 quantum
                }
            } else {
                q3.add(current); // lowest queue runs FCFS unless preempted by new Q1 work
            }
        }

        System.out.println("===== MLFQ RESULTS =====");
        printResults(gantt, processes);
    }

    private static void appendToGantt(List<Process> gantt, String pid, double start, double end, int priority) {
        if (start == end) return;

        if (!gantt.isEmpty()) {
            Process last = gantt.get(gantt.size() - 1);
            if (last.pid.equals(pid) && last.completionTime == start) {
                last.completionTime = end;
                return;
            }
        }

        Process block = new Process(pid, start, end - start, priority);
        block.startTime = start;
        block.completionTime = end;
        gantt.add(block);
    }

    private static void printResults(List<Process> gantt, List<Process> processes) {
        if (gantt.isEmpty()) {
            System.out.println("No execution recorded (gantt empty).");
            return;
        }

        System.out.print("Gantt Chart: |");
        for (Process p : gantt) System.out.print(" " + p.pid + " |");
        System.out.println();

        System.out.print("Time:         ");
        for (Process p : gantt) System.out.print(p.startTime + "-" + p.completionTime + "|");
        System.out.println();

        double totalWT = 0, totalTAT = 0, totalBurst = 0;
        System.out.println("PID | WT | TAT | RT | CT");
        for (Process p : processes) {
            double tat = p.completionTime - p.arrivalTime;
            double wt = tat - p.burstTime;
            double rt = p.startTime - p.arrivalTime;
            totalWT += wt;
            totalTAT += tat;
            totalBurst += p.burstTime;
            System.out.println(p.pid + " | " + wt + " | " + tat + " | " + rt + " | " + p.completionTime);
        }

        double lastEnd = gantt.get(gantt.size() - 1).completionTime;
        double avgWT = totalWT / processes.size();
        double throughput = processes.size() / lastEnd;
        double cpuUtil = (totalBurst / lastEnd) * 100;

        System.out.println("Average Waiting Time: " + avgWT);
        System.out.println("Throughput: " + throughput);
        System.out.println("CPU Utilization: " + cpuUtil + "%");
        System.out.println();
    }
}
