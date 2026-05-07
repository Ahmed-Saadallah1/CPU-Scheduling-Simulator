# CPU Scheduling Simulator

A Java-based CPU scheduling simulator built as part of the **Operating Systems (INCS 102)** course at **German International University (GIU)**. The simulator reads process data from a CSV file, applies multiple scheduling algorithms, and outputs Gantt charts along with key performance metrics for each algorithm.

## Algorithms Implemented

### Mandatory
- **FCFS** — First Come First Served
- **SJF** — Shortest Job First (Non-Preemptive)
- **Priority (Non-Preemptive)** — Runs highest-priority process to completion
- **Priority (Preemptive)** — Preempts current process if a higher-priority process arrives
- **Round Robin** — Time-sharing with configurable time quantum (default: 4.0)

### Bonus
- **MLQ** — Multi-Level Queue
- **MLFQ** — Multi-Level Feedback Queue (by cycles)

## Project Structure

```
src/
├── app/
│   └── Main.java               # Entry point — runs all algorithms on all scenarios
├── algorithms/
│   ├── FCFS.java
│   ├── SJFNonPreemptive.java
│   ├── PriorityNonPreemptive.java
│   ├── PriorityPreemptive.java
│   ├── RoundRobin.java
│   ├── MLQ.java
│   └── MLFQ.java
├── model/
│   └── Process.java            # Process model with WT, TAT, RT, CT calculations
├── io/
│   └── CSVLoader.java          # Loads process scenarios from CSV
└── resources/
    └── TestCases.csv           # Input: 5 test scenarios
```

## Input Format

Processes are defined in `src/resources/TestCases.csv` with the following columns:

```
Scenario, PID, ArrivalTime, BurstTime, Priority
```

Example:
```
Scenario,PID,ArrivalTime,BurstTime,Priority
A,P1,0,5,2
A,P2,1,3,1
A,P3,2,8,3
```

The simulator loads all scenarios and runs every algorithm on each one independently.

## How to Run

### 1. Compile
```bash
javac -d bin -sourcepath src src/app/Main.java
```

### 2. Run
```bash
java -cp bin app.Main
```

## Output

For each algorithm and scenario, the simulator prints:

- **Gantt Chart** — visual timeline of process execution including idle periods
- **Per-process metrics:**
  - Waiting Time (WT)
  - Turnaround Time (TAT)
  - Response Time (RT)
  - Completion Time (CT)
- **Summary metrics:**
  - Average Waiting Time
  - Throughput
  - CPU Utilization

## Technologies

- Java 22
- Pure Java standard library — no external dependencies
