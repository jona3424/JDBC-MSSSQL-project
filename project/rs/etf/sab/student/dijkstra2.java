/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 *
 * @author petri
 */
public class dijkstra2 {
    
 public static void main(String[] args) {
         Connection connection= db.getInstance().getConnection();
        
         
       int buyerCityId = 1; // Replace with the buyer's city ID

        try {
            // Fetch the cities and their connections from the 'Linija' table
            Map<Integer, List<CityConnection>> cityConnections = fetchCityConnections(connection);

            // Fetch the shops and their cities from the 'Prodavnica' table
            Map<Integer, Integer> shopCities = fetchShopCities(connection);

            // Find the closest city with a shop to the buyer's city
            int closestCityId = findClosestShopCity(cityConnections, shopCities, buyerCityId);

            if (closestCityId == -1) {
                System.out.println("No city with a shop found.");
            } else {
                System.out.println("Closest city with a shop: " + closestCityId);
                
               // Find all the shops included in one order
                List<Integer> selectedShops = findSelectedShops(connection);
                System.out.println("Selected shops: " + selectedShops);

                // Calculate the shortest path and distances from each shop's city to the closest city
                calculateShortestPaths(cityConnections, closestCityId, shopCities, selectedShops);
             

               
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Fetches city connections from the 'Linija' table and constructs the graph
    public static Map<Integer, List<CityConnection>> fetchCityConnections(Connection connection) throws SQLException {
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

    // Fetches the shops and their cities from the 'Prodavnica' table
    public static Map<Integer, Integer> fetchShopCities(Connection connection) throws SQLException {
        Map<Integer, Integer> shopCities = new HashMap<>();

        String query = "SELECT idProdavnice, idGrada FROM Prodavnica";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int shopId = resultSet.getInt("idProdavnice");
                int cityId = resultSet.getInt("idGrada");

                shopCities.put(shopId, cityId);
            }
        }

        return shopCities;
    }

    // Finds the closest city with a shop to the buyer's city using Dijkstra's algorithm
    public static int findClosestShopCity(Map<Integer, List<CityConnection>> cityConnections,
                                           Map<Integer, Integer> shopCities, int buyerCityId) {
        Map<Integer, Integer> distanceMap = new HashMap<>();
        Set<Integer> visited = new HashSet<>();
        PriorityQueue<CityDistance> queue = new PriorityQueue<>(Comparator.comparingInt(c -> c.distance));

        distanceMap.put(buyerCityId, 0);
        queue.add(new CityDistance(buyerCityId, 0));

        while (!queue.isEmpty()) {
            CityDistance current = queue.poll();
            int currentCityId = current.cityId;

            if (shopCities.containsValue(currentCityId)) {
                return currentCityId; // Found a city with a shop
            }

            visited.add(currentCityId);

            List<CityConnection> connections = cityConnections.get(currentCityId);
            if (connections != null) {
                for (CityConnection connection : connections) {
                    int neighborCityId = connection.cityId;
                    int distance = connection.distance;

                    if (!visited.contains(neighborCityId)) {
                        int newDistance = distanceMap.get(currentCityId) + distance;

                        if (!distanceMap.containsKey(neighborCityId) || newDistance < distanceMap.get(neighborCityId)) {
                            distanceMap.put(neighborCityId, newDistance);
                            queue.add(new CityDistance(neighborCityId, newDistance));
                        }
                    }
                }
            }
        }

        return -1; // No city with a shop found
    }

    // Represents a city with its distance from the buyer's city
    public static class CityDistance {
        private final int cityId;
        private final int distance;

        public CityDistance(int cityId, int distance) {
            this.cityId = cityId;
            this.distance = distance;
        }
    }

    // Calculates the transport days to assemble the order from each selected shop in the closest city

  // Finds all the shops included in one order from the 'Selektovani' table
    public static List<Integer> findSelectedShops(Connection connection) throws SQLException {
        List<Integer> selectedShops = new ArrayList<>();

        String query = "SELECT DISTINCT a.idProdavnice " +
                "FROM Selektovani s " +
                "JOIN Artikal a ON s.idArtikla = a.idArtikla";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int shopId = resultSet.getInt("idProdavnice");
                selectedShops.add(shopId);
            }
        }

        return selectedShops;
    }

    // Calculates the shortest paths and distances from each shop's city to the closest city
    public static int calculateShortestPaths(Map<Integer, List<CityConnection>> cityConnections,
                                               int closestCityId, Map<Integer, Integer> shopCities,
                                               List<Integer> selectedShops) {
        
        int max=0;
        for (int shopId : selectedShops) {
            int shopCityId = shopCities.getOrDefault(shopId, -1);
            if (shopCityId != -1) {
                List<Integer> shortestPath = findShortestPath(cityConnections, shopCityId, closestCityId);
                int distance = calculateDistance(cityConnections, shortestPath);

                System.out.println("Shop ID: " + shopId);
                System.out.println("Shop City ID: " + shopCityId);
                System.out.println("Shortest Path: " + shortestPath);
                System.out.println("Distance: " + distance);
                System.out.println();
                if(distance>max)max=distance;
            }
        }
        
        System.out.println("Treba vremena za asembl:"+ max);
        return max;
    }

    // Finds the shortest path from the start city to the end city using Dijkstra's algorithm
    public static List<Integer> findShortestPath(Map<Integer, List<CityConnection>> cityConnections,
                                                  int startCityId, int endCityId) {
        Map<Integer, Integer> distanceMap = new HashMap<>();
        Map<Integer, Integer> previousMap = new HashMap<>();
        Set<Integer> visited = new HashSet<>();
        PriorityQueue<CityDistance> queue = new PriorityQueue<>(Comparator.comparingInt(c -> c.distance));

        distanceMap.put(startCityId, 0);
        queue.add(new CityDistance(startCityId, 0));

        while (!queue.isEmpty()) {
            CityDistance current = queue.poll();
            int currentCityId = current.cityId;

            if (currentCityId == endCityId) {
                return reconstructPath(previousMap, currentCityId);
            }

            visited.add(currentCityId);

            List<CityConnection> connections = cityConnections.get(currentCityId);
            if (connections != null) {
                for (CityConnection connection : connections) {
                    int neighborCityId = connection.cityId;
                    int distance = connection.distance;

                    if (!visited.contains(neighborCityId)) {
                        int newDistance = distanceMap.get(currentCityId) + distance;

                        if (!distanceMap.containsKey(neighborCityId) || newDistance < distanceMap.get(neighborCityId)) {
                            distanceMap.put(neighborCityId, newDistance);
                            previousMap.put(neighborCityId, currentCityId);
                            queue.add(new CityDistance(neighborCityId, newDistance));
                        }
                    }
                }
            }
        }

        return new ArrayList<>(); // No path found
    }

    // Reconstructs the shortest path from the previous map
    public static List<Integer> reconstructPath(Map<Integer, Integer> previousMap, int endCityId) {
        List<Integer> path = new ArrayList<>();
        int currentCityId = endCityId;

        while (previousMap.containsKey(currentCityId)) {
            path.add(0, currentCityId);
            currentCityId = previousMap.get(currentCityId);
        }

        path.add(0, currentCityId);

        return path;
    }

    // Calculates the distance of a path in the graph
    public static int calculateDistance(Map<Integer, List<CityConnection>> cityConnections, List<Integer> path) {
        int distance = 0;

        for (int i = 0; i < path.size() - 1; i++) {
            int currentCityId = path.get(i);
            int nextCityId = path.get(i + 1);

            List<CityConnection> connections = cityConnections.get(currentCityId);
            if (connections != null) {
                for (CityConnection connection : connections) {
                    if (connection.cityId == nextCityId) {
                        distance += connection.distance;
                        break;
                    }
                }
            }
        }

        return distance;
    }
}