package com.future.parallelkmeansgui.util;

import com.future.parallelkmeansgui.model.Point;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;

public class DatasetLoader {

    public static List<Point> load2DDataset(String filePath) {
        List<Point> rawPoints = new ArrayList<>();


        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {

                String[] parts = line.split(",");


                if (parts.length >= 2) {
                    try {
                        double x = Double.parseDouble(parts[0].trim());
                        double y = Double.parseDouble(parts[1].trim());
                        rawPoints.add(new Point(x, y));
                    } catch (NumberFormatException e) {

                        System.out.println("Skipping invalid line (likely header): " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return new ArrayList<>();
        }

        if (rawPoints.isEmpty()) {
            return new ArrayList<>();
        }

        DoubleSummaryStatistics xStats = rawPoints.stream().mapToDouble(Point::x).summaryStatistics();
        DoubleSummaryStatistics yStats = rawPoints.stream().mapToDouble(Point::y).summaryStatistics();

        double minX = xStats.getMin();
        double maxX = xStats.getMax();
        double minY = yStats.getMin();
        double maxY = yStats.getMax();

        double rangeX = (maxX - minX) == 0 ? 1.0 : (maxX - minX);
        double rangeY = (maxY - minY) == 0 ? 1.0 : (maxY - minY);

        List<Point> normalizedPoints = new ArrayList<>();
        for (Point p : rawPoints) {
            double scaledX = (p.x() - minX) / rangeX;
            double scaledY = (p.y() - minY) / rangeY;
            normalizedPoints.add(new Point(scaledX, scaledY));
        }

        return normalizedPoints;
    }
}