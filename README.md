# OS Scheduling Simulator (Team Ricochet)

Java console simulator for comparing CPU scheduling algorithms using scenarios defined in `src/resources/TestCases.csv`.

## Team
- Ziad Elshayeb - 16002080
- Ahmed Saadallah - 16008325
- Youssef Ahmed - 16009528
- Yassin Amr - 16005309
- Hazem Ahmed - 16005924
- Ahmed Dakroury - 16004521
- Rawda Wafaee - 16009445
- Hana Ahmed - 16003196

## Algorithms Implemented
- FCFS
- SJF (Non-Preemptive)
- Priority (Non-Preemptive)
- Priority (Preemptive)
- Round Robin (quantum = 4)
- Multi-Level Queue (MLQ)
- Multi-Level Feedback Queue (MLFQ)

## Quick Start
1. From the project root, compile all sources:
   ```sh
   javac -d out $(find src -name "*.java")
   ```
2. Run the simulator:
   ```sh
   java -cp out app.Main
   ```
3. Ensure the input file `src/resources/TestCases.csv` is present (update it to add or edit scenarios).

## Input Format
CSV columns: `Scenario,PID,Arrival,Burst,Priority`

## Outputs
- Gantt chart for each run
- WT, TAT, RT, CT per process
- Average waiting time, throughput, CPU utilization

## Report
Detailed analysis: `report/OS Project Report.pdf`.
