package io;

import java.io.*;
import java.util.*;
import model.Process;

public class CSVLoader {

    public static Map<String, List<Process>> loadCSV(String filepath) {
        Map<String, List<Process>> scenarioMap = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line = br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) continue;

                String scenario = parts[0].trim();
                String pid = parts[1].trim();
                double arrival = Double.parseDouble(parts[2].trim());
                double burst = Double.parseDouble(parts[3].trim());
                int priority = Integer.parseInt(parts[4].trim());

                Process p = new Process(pid, arrival, burst, priority);

                scenarioMap.putIfAbsent(scenario, new ArrayList<>());
                scenarioMap.get(scenario).add(p);
            }
        }
        catch (Exception e) {
            System.out.println("Error loading CSV: " + e.getMessage());
        }

        return scenarioMap;
    }
}