/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author petri
 */
public class Dijkstra {
  // JDBC connection parameters
  

    public static void main(String[] args) {
        Connection connection= db.getInstance().getConnection();

        // Starting and ending city IDs
        int startCityId = 4; // Replace with your starting city ID
        int endCityId = 1; // Replace with your ending city ID

        try  {
            // Fetch the cities and their connections from the 'Linija' table
            Map<Integer, List<CityConnection>> cityConnections = fetchCityConnections(connection);

            // Find the shortest path using Dijkstra's algorithm
            List<Integer> shortestPath = findShortestPath(cityConnections, startCityId, endCityId);

            // Print the shortest path
            if (shortestPath.isEmpty()) {
                System.out.println("No path found between the cities.");
            } else {
                System.out.println("Shortest path:");
                int prev=0;
                for (int cityId : shortestPath) {
                    System.out.println("City ID: " + cityId);
                  
                    
                }
            }
        } catch (SQLServerException e) {
            e.printStackTrace();
        }
    }

    // Fetches city connections from the 'Linija' table and constructs the graph
    public static Map<Integer, List<CityConnection>> fetchCityConnections(Connection connection) throws SQLServerException {
        Map<Integer, List<CityConnection>> cityConnections = new HashMap<>();

        String query = "SELECT idGrada1, idGrada2, daniTransporta FROM Linija";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int cityId1 = resultSet.getInt("idGrada1");
                int cityId2 = resultSet.getInt("idGrada2");
                int distance = resultSet.getInt("daniTransporta");

                // Add city connections in both directions
                addCityConnection(cityConnections, cityId1, cityId2, distance);
                addCityConnection(cityConnections, cityId2, cityId1, distance);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Dijkstra.class.getName()).log(Level.SEVERE, null, ex);
        }

        return cityConnections;
    }

    // Adds a city connection to the graph
    public static void addCityConnection(Map<Integer, List<CityConnection>> cityConnections,
                                          int cityId1, int cityId2, int distance) {
        if (!cityConnections.containsKey(cityId1)) {
            cityConnections.put(cityId1, new ArrayList<>());
        }
        cityConnections.get(cityId1).add(new CityConnection(cityId2, distance));
    }

    // Represents a connection between two cities
    public static class CityConnection {
        private final int cityId;
        private final int distance;

        public CityConnection(int cityId, int distance) {
            this.cityId = cityId;
            this.distance = distance;
        }
    }

    // Finds the shortest path between two cities using Dijkstra's algorithm
    public static List<Integer> findShortestPath(Map<Integer, List<CityConnection>> cityConnections,
                                                  int startCityId, int endCityId) {
        // Distance map to track the shortest distance from the start city
        Map<Integer, Integer> distanceMap = new HashMap<>();

        // Visited set to keep track of visited cities
        Set<Integer> visited = new HashSet<>();

        // Path map to store the previous city in the shortest path
        Map<Integer, Integer> pathMap = new HashMap<>();

        // Initialize distances
        for (int cityId : cityConnections.keySet()) {
            distanceMap.put(cityId, Integer.MAX_VALUE);
        }
        distanceMap.put(startCityId, 0);

        while (!visited.contains(endCityId)) {
            int currentCityId = getNextCityWithMinDistance(distanceMap, visited);

            if (currentCityId == -1) {
                break; // No path found to the end city
            }

            visited.add(currentCityId);

            List<CityConnection> connections = cityConnections.get(currentCityId);
            if (connections != null) {
                for (CityConnection connection : connections) {
                    int neighborCityId = connection.cityId;
                    int distance = connection.distance;

                    if (!visited.contains(neighborCityId)) {
                        int newDistance = distanceMap.get(currentCityId) + distance;

                        if (newDistance < distanceMap.get(neighborCityId)) {
                            distanceMap.put(neighborCityId, newDistance);
                            pathMap.put(neighborCityId, currentCityId);
                        }
                    }
                }
            }
        }

        // Reconstruct the shortest path
        List<Integer> shortestPath = new ArrayList<>();
        int cityId = endCityId;
        while (pathMap.containsKey(cityId)) {
            shortestPath.add(0, cityId);
            cityId = pathMap.get(cityId);
        }
        if (cityId == startCityId) {
            shortestPath.add(0, startCityId);
        }

        return shortestPath;
    }

    // Returns the next city with the minimum distance
    public static int getNextCityWithMinDistance(Map<Integer, Integer> distanceMap, Set<Integer> visited) {
        int minDistance = Integer.MAX_VALUE;
        int minDistanceCity = -1;

        for (Map.Entry<Integer, Integer> entry : distanceMap.entrySet()) {
            int cityId = entry.getKey();
            int distance = entry.getValue();

            if (!visited.contains(cityId) && distance < minDistance) {
                minDistance = distance;
                minDistanceCity = cityId;
            }
        }

        return minDistanceCity;
    }
}