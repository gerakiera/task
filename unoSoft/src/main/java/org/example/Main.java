package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Необходим аргумент: путь к тестовому файлу.");
            return;
        }
        String filePath = args[0];
        BufferedReader in = new BufferedReader(new FileReader(filePath));

        List<String> uniqueLines = new ArrayList<>();
        String readed;
        while ((readed = in.readLine()) != null) {
            if (isValid(readed)) {
                uniqueLines.add(readed);
            }
        }
        in.close();

        Map<Integer, Map<String, Set<String>>> columnValueToLinesMap = new HashMap<>();
        for (String line : uniqueLines) {
            String[] values = line.split(";");
            for (int columnIndex = 0; columnIndex < values.length; columnIndex++) {
                String value = values[columnIndex];
                if (!value.isEmpty()) {
                    columnValueToLinesMap
                            .computeIfAbsent(columnIndex, k -> new HashMap<>())
                            .computeIfAbsent(value, k -> new HashSet<>())
                            .add(line);
                }
            }
        }

        List<Set<String>> groups = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        for (String line : uniqueLines) {
            if (!visited.contains(line)) {
                Set<String> group = new HashSet<>();
                dfs(line, columnValueToLinesMap, visited, group);
                if (group.size() > 1) {
                    groups.add(group);
                }
            }
        }
        writeGroupsToFile(groups);
    }

    private static void dfs(String line, Map<Integer, Map<String, Set<String>>> columnValueToLinesMap,
                            Set<String> visited, Set<String> group) {
        visited.add(line);
        group.add(line);
        String[] values = line.split(";");
        for (int columnIndex = 0; columnIndex < values.length; columnIndex++) {
            String value = values[columnIndex];
            if (!value.isEmpty()) {
                Set<String> connectedLines = columnValueToLinesMap.getOrDefault(columnIndex, Collections.emptyMap())
                        .getOrDefault(value, Collections.emptySet());
                for (String connectedLine : connectedLines) {
                    if (!visited.contains(connectedLine)) {
                        dfs(connectedLine, columnValueToLinesMap, visited, group);
                    }
                }
            }
        }
    }

    private static boolean isValid(String line) {
        String[] values = line.split(";");
        for (String value : values) {
            if (value.matches("\"\\d+\"") || value.trim().isEmpty()) {
                continue;
            }
            return false;
        }
        return true;
    }

    private static void writeGroupsToFile(List<Set<String>> groups) throws IOException {
        groups.removeIf(group -> group.size() <= 1); // Убираем группы с одним элементом
        groups.sort((g1, g2) -> Integer.compare(g2.size(), g1.size())); // Сортировка по размеру группы
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
            writer.write("Количество групп с более чем одним элементом: " + groups.size());
            writer.newLine();
            int groupCount = 1;
            for (Set<String> group : groups) {
                writer.write("Группа " + groupCount++);
                writer.newLine();
                for (String line : group) {
                    writer.write(line);
                    writer.newLine();
                }
                writer.newLine();
            }
        }
    }
}
