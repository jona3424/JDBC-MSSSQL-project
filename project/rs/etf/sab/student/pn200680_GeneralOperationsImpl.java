/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.sql.*;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.GeneralOperations;

/**
 *
 * @author jona
 */
public class pn200680_GeneralOperationsImpl implements GeneralOperations {
    Connection con= db.getInstance().getConnection();
    private static Calendar cal;
    @Override
    public void setInitialTime(Calendar clndr) {
        cal=Calendar.getInstance();
        cal.setTime(clndr.getTime());
        
    }

    @Override
    public Calendar time(int i) {
     
        try {
            //update kalendar  i citavu bazu za pracenje
            updateOrderStatus(con,cal.getTime(),i);
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_GeneralOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
            cal.add(Calendar.DAY_OF_MONTH, i);

                    return cal;

        }
private static void updateOrderStatus(Connection connection, java.util.Date newDate,int daysPassed) throws SQLException {
    java.util.Date currentDate = new java.util.Date();

    String selectSql = "SELECT idPorudzbine,danidosastavljanja FROM Porudzbina WHERE status = 'sent'";
    String updateDanidosastavljanjaSql = "UPDATE Porudzbina SET danidosastavljanja = CASE WHEN danidosastavljanja > ? THEN danidosastavljanja - ? ELSE 0 END WHERE idPorudzbine = ?";
    String updatePracenjeSql = "UPDATE Pracenje\n" +
        "SET preostaloDana = CASE WHEN preostaloDana > 0 THEN preostaloDana - 1 ELSE 0 END\n" +
        "WHERE idPracenja = (SELECT TOP 1 idPracenja FROM Pracenje WHERE idPorudzbine = ? AND preostaloDana > 0 ORDER BY poredak)";
    String updatePorudzbinaSql = "UPDATE Porudzbina SET status = 'arrived', datumPrijema=? WHERE idPorudzbine = ? AND (SELECT COUNT(*) FROM Pracenje WHERE idPorudzbine = ? AND preostaloDana > 0) = 0";

    try (Statement selectStatement = connection.createStatement();
         PreparedStatement updateDanidosastavljanjaStatement = connection.prepareStatement(updateDanidosastavljanjaSql);
         PreparedStatement updatePracenjeStatement = connection.prepareStatement(updatePracenjeSql);
         PreparedStatement updatePorudzbinaStatement = connection.prepareStatement(updatePorudzbinaSql);
         PreparedStatement getDaniStatement=connection.prepareStatement("Select preostaloDana from Pracenje where idPracenja=(SELECT TOP 1 idPracenja FROM Pracenje WHERE idPorudzbine = ? AND preostaloDana > 0 ORDER BY poredak)")
            
            ) {

        ResultSet resultSet = selectStatement.executeQuery(selectSql);

        while (resultSet.next()) {
            int orderId = resultSet.getInt("idPorudzbine");
            int danidosastavljanja = resultSet.getInt(2);
            int remainingDays = daysPassed;

            updateDanidosastavljanjaStatement.setLong(1, remainingDays);
            updateDanidosastavljanjaStatement.setLong(2, remainingDays);
            updateDanidosastavljanjaStatement.setInt(3, orderId);
            updateDanidosastavljanjaStatement.executeUpdate();
            
            remainingDays-=danidosastavljanja;
            if(remainingDays<=0)continue;
            updatePracenjeStatement.setInt(1, orderId);
            updatePracenjeStatement.executeUpdate();


            while (remainingDays > 0) {
                getDaniStatement.setInt(1, orderId);
                try(ResultSet rs=getDaniStatement.executeQuery()){
                    if(!rs.next()){
                        Calendar cc=cal;
                        //odje sam doda -2 zasto boga pitaj iskreno?!?!?!?!?
                        System.out.println("Koliko je ovo dana: " + (daysPassed-remainingDays+1));
                        cc.add(Calendar.DAY_OF_MONTH, daysPassed-remainingDays+1);
                        Timestamp timestamp = new Timestamp(cc.getTime().getTime());
                        Date dat = new Date(timestamp.getTime());
                        updatePorudzbinaStatement.setDate(1, dat);
                        updatePorudzbinaStatement.setInt(2, orderId);
                        updatePorudzbinaStatement.setInt(3, orderId);
                        updatePorudzbinaStatement.executeUpdate();
                        break;
                    }
                }
                updatePracenjeStatement.setInt(1, orderId);
                updatePracenjeStatement.executeUpdate();

                remainingDays--;

                
            }
        }
    }
}
    
        
    @Override
    public Calendar getCurrentTime() {
       
        return cal;
    }

    @Override
    public void eraseAll() {
        
   

            try (  Statement stmt = con.createStatement();) {
                
                
                stmt.execute("DELETE FROM pracenje");
                stmt.execute("DELETE FROM selektovani DBCC CHECKIDENT ('SabProjekat.dbo.selektovani', RESEED, 0)");
                stmt.execute("DELETE FROM transakcije DBCC CHECKIDENT ('SabProjekat.dbo.transakcije', RESEED, 0)");
                stmt.execute("DELETE FROM artikal DBCC CHECKIDENT ('SabProjekat.dbo.artikal', RESEED, 0)");
                stmt.execute("DELETE FROM porudzbina DBCC CHECKIDENT ('SabProjekat.dbo.porudzbina', RESEED, 0)");
                stmt.execute("DELETE FROM prodavnica DBCC CHECKIDENT ('SabProjekat.dbo.prodavnica', RESEED, 0)");
                stmt.execute("DELETE FROM kupac DBCC CHECKIDENT ('SabProjekat.dbo.kupac', RESEED, 0)");
                stmt.execute("DELETE FROM linija ");
                stmt.execute("DELETE FROM grad DBCC CHECKIDENT ('SabProjekat.dbo.grad', RESEED, 0)");
                
                
               
                
            
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_GeneralOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
}
