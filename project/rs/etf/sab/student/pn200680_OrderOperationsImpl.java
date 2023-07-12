/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.GeneralOperations;
import rs.etf.sab.operations.OrderOperations;
import static rs.etf.sab.student.dijkstra2.*;

/**
 *
 * @author jona
 */
public class pn200680_OrderOperationsImpl implements OrderOperations{
    Connection con= db.getInstance().getConnection();

    @Override
    public int addArticle(int i, int i1, int i2) {
        try (
            PreparedStatement prep=con.prepareStatement("Select kolicina from Artikal where idArtikla=?");
            PreparedStatement prep1=con.prepareStatement("Select idSelektovani from Selektovani where idArtikla=? and idPorudzbine=?");
            PreparedStatement prep2=con.prepareStatement("Update Selektovani set kolicina = kolicina + ? where idArtikla=? and idPorudzbine=?");   
            PreparedStatement prep3=con.prepareStatement("Insert into Selektovani(kolicina,idPorudzbine,idArtikla,popust) values(?,?,?,?)",Statement.RETURN_GENERATED_KEYS);    
            PreparedStatement prep4=con.prepareStatement("Select popust from  Prodavnica join Artikal on(Prodavnica.idProdavnice=Artikal.idProdavnice) where idArtikla=?")  
              ){
            
            prep.setInt(1, i1);
            prep1.setInt(1, i1);
            prep1.setInt(2, i);
           try( ResultSet rs= prep.executeQuery();){
               while(rs.next())
                   if(rs.getInt(1)>= i2){
                       try(ResultSet rs1=prep1.executeQuery()){
                           while(rs1.next()){
                               int idSelektovani=rs1.getInt(1);
                               prep2.setInt(1,i2);
                               prep2.setInt(2, i1);
                               prep2.setInt(3, i);
                               if(prep2.executeUpdate()==1)return idSelektovani;        
                               return -1;
                        }

                       }
                       prep4.setInt(1, i1);
                       try(ResultSet rs4=prep4.executeQuery()){
                           while(rs4.next()){
                               int popust=rs4.getInt(1);
                               prep3.setInt(1, i2);
                               prep3.setInt(2, i);
                               prep3.setInt(3, i1);
                               prep3.setInt(4, popust);
                               if(prep3.executeUpdate()==1){
                               try( ResultSet rs3= prep3.getGeneratedKeys();){
                                    while(rs3.next()){
                                        return rs3.getInt(1);
                                    }
                                }
                              }return -1;
                           }
                       }
                       
                       
                   }
           }
           return -1;
            
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_TransactionOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        
        
    }

    @Override
    public int removeArticle(int i, int i1) {
         try (
            PreparedStatement prep=con.prepareStatement("Update Selektovani set kolicina = 0 where idPorudzbine=? and idArtikla=?");){
        
            prep.setInt(1, i);
            prep.setInt(2, i1);
            
            
            if(prep.executeUpdate()==1)return 1;
            
            return -1;
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_ShopOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
             return -1;

        }
    
    }

    @Override
    public List<Integer> getItems(int i) {
        List<Integer> lista=new ArrayList<>();  
       try (
            PreparedStatement prep=con.prepareStatement("Select idArtikla from Selektovani where idPorudzbine=?");){
        
            prep.setInt(1, i);
             try(ResultSet rs= prep.executeQuery();){
                while(rs.next()){
                    lista.add(rs.getInt(1));
                }
            
             } 
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_ShopOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
            

        }
            
             
             if(lista.isEmpty())return null;
         return lista;
    
    }

    @Override
    public int completeOrder(int i) {
        //provjera jel proizvoda i dalje ima dovoljno 
         try (
            PreparedStatement prep=con.prepareStatement("select idArtikla,kolicina from Selektovani where idPorudzbine=?");     
            PreparedStatement prep1=con.prepareStatement("select kolicina from Artikal where idArtikla=? and kolicina>=?");
            PreparedStatement prep2 = con.prepareStatement("UPDATE Porudzbina SET dodatniPopust = 2 WHERE idPorudzbine IN (SELECT idPorudzbine FROM Transakcije WHERE cijena > 10000 AND DATEDIFF(DAY, GETDATE(), datumTransakcije) < 30)");){
            prep.setInt(1, i);
            try(ResultSet rs=prep.executeQuery()){
                while(rs.next()){
                    int idA=rs.getInt(1);
                    int kol=rs.getInt(2);
                    prep1.setInt(1, idA);
                    prep1.setInt(2, kol);
                    try(ResultSet rs1=prep1.executeQuery()){
                        if(!rs1.next()) return -1;
                    }
                }
            }
           prep2.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_ShopOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
             return -1;

        }
        
       
        
        //pozovi fun za racunanje finalnog prajsa
        BigDecimal finalprice=null;
         try (
            PreparedStatement prep=con.prepareStatement("SELECT\n" +
                "    P.idPorudzbine AS OrderId,\n" +
                "    SUM(A.cijena * S.kolicina * (1 - (P.dodatniPopust / 100.0)) * (1 - (S.popust / 100.0))) AS FinalPrice\n" +
                "FROM\n" +
                "    dbo.Porudzbina P\n" +
                "    INNER JOIN dbo.Selektovani S ON P.idPorudzbine = S.idPorudzbine\n" +
                "    INNER JOIN dbo.Artikal A ON S.idArtikla = A.idArtikla\n" +
                "WHERE\n" +
                "    P.idPorudzbine = ?\n" +
                "GROUP BY\n" +
                "    P.idPorudzbine;");)
         {
        
            prep.setInt(1, i);
            
            try(ResultSet rs=prep.executeQuery()){
                while(rs.next())finalprice= rs.getBigDecimal(2).setScale(3);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_ShopOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
             return -1;

        }
        System.out.println("aaaaaaaa     " + finalprice);
        
        
        
        int idGradaKupca=0;
        //dodaj novu transakciju korisnika i skini mu final price s racuna
        GeneralOperations gop=new pn200680_GeneralOperationsImpl();
        Calendar c= gop.getCurrentTime();
        java.util.Date date=c.getTime();
        Timestamp timestamp = new Timestamp(date.getTime());
        Date d = new Date(timestamp.getTime());

        int sumfordate=0;
        String insertQuery = "INSERT INTO Transakcije (idKupca, cijena, datumTransakcije, idPorudzbine) " +
                    "SELECT P.idKupca, ?, ?, ? " +
                    "FROM Porudzbina P " +
                    "WHERE P.idPorudzbine = ?";
        try( PreparedStatement prep=con.prepareStatement(insertQuery);
             PreparedStatement prep2=con.prepareStatement("Select idGrada from Kupac K join Porudzbina P on K.idKupca = P.idKupca  WHERE P.idPorudzbine = ? and K.pare>=?");
             PreparedStatement prep1=con.prepareStatement("UPDATE K\n" +
                                                            "SET K.pare = K.pare - ?\n" +
                                                            "FROM Kupac K\n" +
                                                            "JOIN Porudzbina P ON K.idKupca = P.idKupca\n" +
                                                            "WHERE P.idPorudzbine = ?;");
                ){
            
            prep2.setInt(1, i);
            prep2.setBigDecimal(2, finalprice);
            try(ResultSet rss=prep2.executeQuery()){
                if(rss.next()){
                    idGradaKupca=rss.getInt("idGrada");
                }
                else return -1;
            }
            
            prep.setBigDecimal(1, finalprice);
        
            prep.setDate(2, d);
            prep.setInt(3, i);
            prep.setInt(4, i);

            if(prep.executeUpdate()!=1)return -1;
            prep1.setBigDecimal(1, finalprice);
            prep1.setInt(2, i);
            if(prep1.executeUpdate()!=1)return -1;

        }
        catch (SQLException ex) {
            Logger.getLogger(pn200680_ShopOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
             return -1;

        }
        
        
        // pozovi dijkstru sacuvaj najblizi grad i dane za asembl
        int najbliziGradId=0;
        int danidoAsembl=0;
        
        try {
            // Fetch the cities and their connections from the 'Linija' table
            Map<Integer, List<dijkstra2.CityConnection>> cityConnections = fetchCityConnections(con);

            // Fetch the shops and their cities from the 'Prodavnica' table
            Map<Integer, Integer> shopCities = fetchShopCities(con);

            // Find the closest city with a shop to the buyer's city
            int closestCityId = findClosestShopCity(cityConnections, shopCities, idGradaKupca);

            if (closestCityId == -1) {
                System.out.println("No city with a shop found.");
            } else {
                System.out.println("Closest city with a shop: " + closestCityId);
                najbliziGradId=closestCityId;
               // Find all the shops included in one order
                List<Integer> selectedShops = findSelectedShops(con);
                System.out.println("Selected shops: " + selectedShops);

                // Calculate the shortest path and distances from each shop's city to the closest city
                danidoAsembl=calculateShortestPaths(cityConnections, closestCityId, shopCities, selectedShops);
             

               
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        
        
        //update dan do asemblovanja,sent date,status, 
         try (
            PreparedStatement prep=con.prepareStatement("UPDATE Porudzbina\n" +
                                        "SET daniDoSastavljanja = ?,\n" +
                                        "    datumSlanja = ?,\n" +
                                        "    status = 'sent'\n" +
                                        "WHERE idPorudzbine = ?;");){
        
            prep.setInt(1,danidoAsembl);
            prep.setDate(2, d);
            prep.setInt(3, i);
            
            if(prep.executeUpdate()!=1)return -1;
            
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_ShopOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
             return -1;

        }
        
        
        //pozovi dijkstru za putanju od mene do grada
        //napravi putanje za tracking i insertaj ih
         try  {
            // Fetch the cities and their connections from the 'Linija' table
            Map<Integer, List<Dijkstra.CityConnection>> cityConnections = Dijkstra.fetchCityConnections(con);

            // Find the shortest path using Dijkstra's algorithm
            List<Integer> shortestPath = Dijkstra.findShortestPath(cityConnections, najbliziGradId, idGradaKupca);

            // Print the shortest path
            if (shortestPath.isEmpty()) {
                System.out.println("No path found between the cities.");
                return -1;
            } else {
                int prev=0;
                int cnt=1;
                System.out.println("Shortest path:");
                for (int cityId : shortestPath) {
                    System.out.println("City ID: " + cityId);
                      if(prev!=0){
                        try(
                                PreparedStatement prep1=con.prepareStatement("Select top 1 daniTransporta from Linija where (idGrada1=? and idGrada2=?) or (idGrada1=? and idGrada2=?) ");
                                PreparedStatement prep=con.prepareStatement("INSERT INTO Pracenje (idGrada, idPorudzbine, preostaloDana,poredak) VALUES (?,?,?,?)");){
                            prep1.setInt(1, prev);
                            prep1.setInt(2, cityId);
                            prep1.setInt(3, cityId);
                            prep1.setInt(4, prev);
                            try(ResultSet rs= prep1.executeQuery()){
                                if(rs.next()){
                                    prep.setInt(1, prev);
                                    prep.setInt(2, i);
                                    int dani= rs.getInt(1);
                                    sumfordate+=dani;
                                    prep.setInt(3, dani+1);
                                    prep.setInt(4, cnt++);
                                    if(prep.executeUpdate()!=1)return -1;
                                }
                            }
                            
                            
                        } catch (SQLException ex) {
                            Logger.getLogger(pn200680_OrderOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
                            return -1;
                        }
                    }
                    prev=cityId;
                }
            }
        } catch (SQLServerException e) {
            e.printStackTrace();
        }
        
        
        
        //dodaj recieve date
//        try (
//            PreparedStatement prep=con.prepareStatement("UPDATE Porudzbina SET datumPrijema = ? WHERE idPorudzbine = ?");){
//            c.add(Calendar.DAY_OF_MONTH, sumfordate);
//            date=c.getTime();
//            timestamp = new Timestamp(date.getTime());
//            Date dat = new Date(timestamp.getTime());
//
//            prep.setDate(1,dat);
//            prep.setInt(2, i);
//            
//            if(prep.executeUpdate()!=1)return -1;
//            
//        } catch (SQLException ex) {
//            Logger.getLogger(pn200680_ShopOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
//             return -1;
//
//        }
        
        //umanji broj poizvoda
         try (
            PreparedStatement prep=con.prepareStatement("UPDATE Artikal SET kolicina = A.kolicina - S.kolicina FROM Artikal A JOIN Selektovani S ON A.idArtikla = S.idArtikla WHERE S.idPorudzbine = ?");){
            
            prep.setInt(1, i);
            
            if(prep.executeUpdate()<1)return -1;
            
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_ShopOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
             return -1;

        }
         
         
        
        return 1;
        
    }

    @Override
    public BigDecimal getFinalPrice(int i) {
         try (
            PreparedStatement prep=con.prepareStatement("SELECT\n" +
                "    P.idPorudzbine AS OrderId,\n" +
                "    SUM(A.cijena * S.kolicina * (1 - (P.dodatniPopust / 100.0)) * (1 - (S.popust / 100.0))) AS FinalPrice\n" +
                "FROM\n" +
                "    dbo.Porudzbina P\n" +
                "    INNER JOIN dbo.Selektovani S ON P.idPorudzbine = S.idPorudzbine\n" +
                "    INNER JOIN dbo.Artikal A ON S.idArtikla = A.idArtikla\n" +
                "WHERE\n" +
                "    P.idPorudzbine = ?\n" +
                "GROUP BY\n" +
                "    P.idPorudzbine;");)
         {
        
            prep.setInt(1, i);
            
            try(ResultSet rs=prep.executeQuery()){
                while(rs.next())return rs.getBigDecimal(2).setScale(3);
            }
            
            return new BigDecimal(-1);
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_ShopOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
             return new BigDecimal(-1);

        }
        
        }

    @Override
    public BigDecimal getDiscountSum(int i) {
         try (
            PreparedStatement prep=con.prepareStatement("SELECT COALESCE(SUM(s.kolicina *a.cijena*s.popust/100.0), 0)  FROM Selektovani s join Porudzbina p on (p.idPorudzbine=s.idPorudzbine) \n" +
"join Artikal  a on (a.idArtikla=s.idArtikla) \n" +
"where s.idPorudzbine = ?");)
         {
        
            prep.setInt(1, i);
            
            try(ResultSet rs=prep.executeQuery()){
                while(rs.next())return rs.getBigDecimal(1).setScale(3);
            }
            
            return new BigDecimal(-1);
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_ShopOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
             return new BigDecimal(-1);

        }
    
    }

    @Override
    public String getState(int i) {
        try (
            PreparedStatement prep=con.prepareStatement("Select status from Porudzbina where idPorudzbine=?");){
        
            prep.setInt(1, i);
            
            
            try(ResultSet rs=prep.executeQuery()){
                while(rs.next())
                {
                  return rs.getString(1);

                }
            }
            
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_ShopOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
             return  null;

        }
    
    }

    @Override
    public Calendar getSentTime(int i) {
         try (
            PreparedStatement prep=con.prepareStatement("Select datumSlanja from Porudzbina where idPorudzbine=? and datumSlanja is not null");){
        
            prep.setInt(1, i);
            
            try(ResultSet rs=prep.executeQuery()){
                while(rs.next())
                {
                  Calendar c =Calendar.getInstance();
                  c.setTime(rs.getDate(1));
                  return c;

                }
            }
            
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_ShopOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
             return null;

        }
    }

    @Override
    public Calendar getRecievedTime(int i) {
        try (
            PreparedStatement prep=con.prepareStatement("Select datumPrijema from Porudzbina where idPorudzbine=? and datumPrijema is not null");){
        
            prep.setInt(1, i);
            
            try(ResultSet rs=prep.executeQuery()){
                while(rs.next())
                {
                  Calendar c =Calendar.getInstance();
                  c.setTime(rs.getDate(1));
                  return c;

                }
            }
            
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_ShopOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
             return null;

        }
    }

    @Override
    public int getBuyer(int i) {
        
        try (
            PreparedStatement prep=con.prepareStatement("Select idKupca from Porudzbina where idPorudzbine=? ");){
        
            prep.setInt(1, i);
            
            try(ResultSet rs=prep.executeQuery()){
                while(rs.next())
                {
                  return rs.getInt(1);

                }
            }
            
            return -1;
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_ShopOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
             return -1;

        }
        
    }

    @Override
    public int getLocation(int i) {
            String query = "select daniDoSastavljanja from Porudzbina \n" +
                            "where idPorudzbine=? and daniDoSastavljanja is not null and (status='sent' or status='arrived')";
            String pracenjeQuery = "SELECT TOP 1 idGrada " +
                                           "FROM Pracenje " +
                                           "WHERE idPorudzbine = ? AND preostaloDana != 0 " +
                                           "ORDER BY poredak";
            
        try (    
            PreparedStatement stmt = con.prepareStatement(query);
            PreparedStatement pracenjeStmt = con.prepareStatement(pracenjeQuery);
            PreparedStatement arrivedStmt=con.prepareStatement("Select idGrada from porudzbina p join kupac k on k.idKupca=p.idKupca where idPorudzbine=? and p.status='arrived'")
            ){
            stmt.setInt(1, i);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int danidoSastavljanja = rs.getInt("danidoSastavljanja");

                if (danidoSastavljanja <= 0) {
                    // Order is assembled, find package location
                   
                    arrivedStmt.setInt(1, i);
                    ResultSet arrivedRS = arrivedStmt.executeQuery();
                    if(arrivedRS.next()){
                         return arrivedRS.getInt(1);
                    }
                    
                    
                    pracenjeStmt.setInt(1, i);
                    ResultSet pracenjeRs = pracenjeStmt.executeQuery();

                    if (pracenjeRs.next()) {
                        return pracenjeRs.getInt("idGrada");
                    }
                } else if (danidoSastavljanja > 0) {
                    
                    try {
                       // Fetch the cities and their connections from the 'Linija' table
                       Map<Integer, List<CityConnection>> cityConnections = fetchCityConnections(con);

                       // Fetch the shops and their cities from the 'Prodavnica' table
                       Map<Integer, Integer> shopCities = fetchShopCities(con);
                       try ( PreparedStatement getGrad = con.prepareStatement("Select idGrada from Kupac K JOIN Porudzbina P ON K.idKupca = P.idKupca WHERE P.idPorudzbine = ?;");
                            ){
                           getGrad.setInt(1, i);
                           try(ResultSet grado= getGrad.executeQuery()){
                               if(grado.next()){
                                   // Find the closest city with a shop to the buyer's city
                                    int closestCityId = findClosestShopCity(cityConnections, shopCities, grado.getInt(1));

                                    if (closestCityId == -1) {
                                        System.out.println("No city with a shop found.");
                                    } else {
                                        return closestCityId;

                       }
                               }
                           }
                       }
                       
                   } catch (SQLException e) {
                       e.printStackTrace();
                       return -1;
                   }
                    
                }

                

               
            }
            return -1;
        
        
        } catch (SQLException ex) {
                Logger.getLogger(pn200680_ShopOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
                
        }
        
        return -1; // Return -1 if failure
    
    }
    
}
