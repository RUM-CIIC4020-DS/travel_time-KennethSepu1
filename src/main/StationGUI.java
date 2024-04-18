package main;

import data_structures.*;
import interfaces.*;
import interfaces.List;

import javax.swing.*;
import java.awt.*;
/**
 * @author Kenneth S. Sepulveda
 * StationGUI to show the departure and arrival time
 */
public class StationGUI extends JFrame {
    private TrainStationManager manager;

    public StationGUI(TrainStationManager manager) {
        this.manager = manager;
        setTitle("Welcome to Westside Station!");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] columnNames = {"Station", "Departure", "Arrival"};
        Object[][] data = getStationData();
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private Object[][] getStationData() {
        Map<String, Double> travelTimes = manager.getTravelTimes();
        List<String> keys = travelTimes.getKeys();
        Object[][] data = new Object[keys.size()][3];
        for (int i = 0; i < keys.size(); i++) {
            String station = keys.get(i);
            Double travelTime = travelTimes.get(station);
            data[i][0] = station;
            data[i][1] = getDepartureTime(station); // You need to implement this method
            data[i][2] = getArrivalTime(station, travelTime); // You need to implement this method
        }
        return data;
    }


    private String getDepartureTime(String station) {
        return "8:00 AM";
    }

    private String getArrivalTime(String station, Double travelTime) {
        String departureTime = getDepartureTime(station);
        String[] parts = departureTime.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1].substring(0, 2));
        
        int totalMinutes = hours * 60 + minutes + (int)Math.round(travelTime);
        hours = totalMinutes / 60;
        minutes = totalMinutes % 60;
        
        return String.format("%02d:%02d", hours, minutes) + " AM";
    }


    public static void main(String[] args) {
        TrainStationManager manager = new TrainStationManager("stations.csv");
        StationGUI gui = new StationGUI(manager);
        gui.setVisible(true);
    }
}
