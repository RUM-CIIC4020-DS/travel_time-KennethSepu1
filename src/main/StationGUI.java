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
	
    Map<String, String> departureTimes = new HashTableSC<>(1, new SimpleHashFunction<>());
    private TrainStationManager manager;
    /**
     * 
     * @param manager - cretaes the gui that shows each city their departure and arrival time.
     * It also displays the shortest route between cities depending which city you input.
     */
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
        
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        JTextField stationInput = new JTextField(10);
        topPanel.add(stationInput);

        JButton showRouteButton = new JButton("Show Route");
        showRouteButton.addActionListener(e -> {
            String stationName = stationInput.getText();
            String route = manager.traceRoute(stationName);
            JOptionPane.showMessageDialog(this, route);
            }
        );
        topPanel.add(showRouteButton);

        add(topPanel, BorderLayout.NORTH);
    }
    /**
     * Gets the station data for the GUI
     * Creates a 2D array that has the station name and their departure and arrival time.
     * @return the array that contains all the station data.
     */
    private Object[][] getStationData() {
        Map<String, Double> travelTimes = manager.getTravelTimes();
        List<String> keys = travelTimes.getKeys();
        keys.remove("Westside");
        Object[][] data = new Object[keys.size()][3];
        for (int i = 0; i < keys.size(); i++) {
            String station = keys.get(i);
            Double travelTime = travelTimes.get(station);
            data[i][0] = station;
            data[i][1] = getDepartureTime(station); 
            data[i][2] = getArrivalTime(station, travelTime);
        }
        return data;
    }

    /**
     * 
     * @param station - name of the station
     * @return the departure time for the station
     */
    private String getDepartureTime(String station) {
    	
    	departureTimes.put("Bugapest", "9:35 AM");
        departureTimes.put("Dubay", "10:30 AM");
        departureTimes.put("Berlint", "8:25 PM");
        departureTimes.put("Mosbull", "6:00 PM");
        departureTimes.put("Cayro", "6:40 AM");
        departureTimes.put("Bostin", "10:25 AM");
        departureTimes.put("Los Angelos", "12:30 PM");
        departureTimes.put("Dome", "1:30 PM");
        departureTimes.put("Takyo", "3:35 PM");
        departureTimes.put("Unstabul", "4:45 PM");
        departureTimes.put("Chicargo", "7:25 AM");
        departureTimes.put("Loondun", "2:00 PM");

        String departureTime = departureTimes.get(station);
       
        return departureTime;
    }

    /**
     * Calculate the arrival time for the station based on their departure time
     * 
     * @param station - name of the station
     * @param travelTime - travel time from one station to another
     * @return the arrival time calculated from the departure time
     */
    private String getArrivalTime(String station, Double travelTime) {
        String departureTime = departureTimes.get(station);

        String[] parts = departureTime.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1].substring(0, 2));
        String amPm = parts[1].substring(3);

        int totalMinutes = hours * 60 + minutes;
        if (amPm.equals("PM") && hours != 12) {
            totalMinutes += 12 * 60;
        } else if (amPm.equals("AM") && hours == 12) {
            totalMinutes -= 12 * 60;
        }
        
        totalMinutes += (int)Math.round(travelTime); 

        hours = (totalMinutes / 60) % 24;
        minutes = totalMinutes % 60;

        if (hours >= 12 && hours < 24) {
            amPm = "PM";
        } else {
            amPm = "AM";
        }
        hours = hours % 12; 
        if (hours == 0) {
            hours = 12;
        }

        return String.format("%02d:%02d", hours, minutes) + " " + amPm;
    }


    /**
     *  Used to display the input file
     * @param args
     */
    public static void main(String[] args) {
        TrainStationManager manager = new TrainStationManager("stations.csv");
        StationGUI gui = new StationGUI(manager);
        gui.setVisible(true);
    }
}
