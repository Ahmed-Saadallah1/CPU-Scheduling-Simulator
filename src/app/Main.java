package app;

import algorithms.FCFS;
import algorithms.MLFQ;
import algorithms.MLQ;
import algorithms.PriorityNonPreemptive;
import algorithms.PriorityPreemptive;
import algorithms.RoundRobin;
import algorithms.SJFNonPreemptive;
import io.CSVLoader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import model.Process;

public class Main {

    public static void main(String[] args) {

        // CSV path relative to project root
        String filepath = Paths.get("src", "resources", "TestCases.csv").toString();

        Map<String, List<Process>> scenarios = CSVLoader.loadCSV(filepath);

        if (scenarios.isEmpty()) {
            System.out.println("No scenarios found in CSV file.");
            return;
        }

        List<String> scenarioNames = new ArrayList<>(scenarios.keySet());
        Collections.sort(scenarioNames);

        System.out.println("Loaded " + scenarioNames.size() + " scenario(s): " + scenarioNames);
        if (scenarioNames.size() != 5) {
            System.out.println("Warning: expected 5 scenarios; CSV currently has " + scenarioNames.size());
        }

        for (String scenario : scenarioNames) {
            System.out.println("\n####################################");
            System.out.println("###       SCENARIO " + scenario + "       ###");
            System.out.println("####################################");

            List<Process> processes = scenarios.get(scenario);

            runAllAlgorithms(processes);
        }
    }

    private static void runAllAlgorithms(List<Process> processes) {

        FCFS.runFCFS(clone(processes));
        SJFNonPreemptive.runSJF(clone(processes));
        PriorityNonPreemptive.runPriority(clone(processes));
        PriorityPreemptive.runPriorityPreemptive(clone(processes));
        RoundRobin.runRoundRobin(clone(processes), 4.0);
        MLQ.runMLQ(clone(processes));
        MLFQ.runMLFQ(clone(processes));
    }

    private static List<Process> clone(List<Process> original) {
        List<Process> c = new ArrayList<>();
        for (Process p : original) {
            c.add(new Process(p.pid, p.arrivalTime, p.burstTime, p.priority));
        }
        return c;
    }
}