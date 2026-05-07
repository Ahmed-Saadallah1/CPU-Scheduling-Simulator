package model;

public class Process {
    public String pid;
    public double arrivalTime;
    public double burstTime;
    public int priority;
    public double remainingTime;
    public double startTime;
    public double completionTime;

    public Process(String pid, double arrivalTime, double burstTime, int priority) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
        this.startTime = -1;
        this.completionTime = -1;
    }

    public double getTurnaroundTime() {
        if (completionTime == -1)
            return -1;
        return completionTime - arrivalTime;
    }

    public double getWaitingTime() {
        if (completionTime == -1)
            return -1;
        return getTurnaroundTime() - burstTime;
    }

    public double getResponseTime() {
        if (startTime == -1)
            return -1;
        return startTime - arrivalTime;
    }

    public String toString() {
        return "Process [pid=" + pid + ", arrivalTime=" + arrivalTime
                + ", burstTime=" + burstTime + ", priority=" + priority
                + ", remainingTime=" + remainingTime + ", startTime="
                + startTime + ", completionTime=" + completionTime + "]";
    }
}
