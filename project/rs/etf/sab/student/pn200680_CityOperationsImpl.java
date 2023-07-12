/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.util.List;
import rs.etf.sab.operations.CityOperations;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author jona
 */
public class pn200680_CityOperationsImpl implements CityOperations {
    Connection con= db.getInstance().getConnection();
    @Override
    public int createCity(String string) {
        try ( PreparedStatement ps=con.prepareStatement("Insert into Grad(naziv) values(?)",Statement.RETURN_GENERATED_KEYS);){
            ps.setString(1, string);
            if(ps.executeUpdate()==1){
                try(ResultSet rs=ps.getGeneratedKeys();){
                    if(rs.next()){
                        return rs.getInt(1);
                    }
                    return -1;
                }
            }
            return -1;
            
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_CityOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    @Override
    public List<Integer> getCities() {
        List<Integer> lista=new ArrayList<>();
            try(
                Statement stmt=con.createStatement();
              ResultSet rs=  stmt.executeQuery("Select idGrada from grad");){
                while(rs.next()){
                    lista.add(rs.getInt(1));
                }
            } catch (SQLException ex) {
            Logger.getLogger(pn200680_CityOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
             if(lista.isEmpty())return null;
         return lista;
            

    }

    @Override
    public int connectCities(int i, int i1, int i2) {
         try ( PreparedStatement ps=con.prepareStatement("Insert into Linija(idGrada1,idGrada2,daniTransporta) values(?,?,?)",Statement.RETURN_GENERATED_KEYS);){
            ps.setInt(1, i);
            ps.setInt(2, i1);
            ps.setInt(3, i2);

            if(ps.executeUpdate()==1){
                try(ResultSet rs=ps.getGeneratedKeys();){
                    if(rs.next()){
                        return rs.getInt(1);
                    }
                    return -1;
                }
            }
            return -1;
            
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_CityOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    @Override
    public List<Integer> getConnectedCities(int i) {
        List<Integer> lista=new ArrayList<>();
        try(PreparedStatement prep1=con.prepareStatement("Select idGrada2 from linija where idGrada1=?");
            PreparedStatement prep2=con.prepareStatement("Select idGrada1 from linija where idGrada2=?");){
          prep1.setInt(1, i);
          prep2.setInt(1, i);
          try(ResultSet rs=prep1.executeQuery()) {
              while(rs.next()){
                  lista.add(rs.getInt(1));
              }
          }
          try(ResultSet rs=prep2.executeQuery()) {
              while(rs.next()){
                  lista.add(rs.getInt(1));
              }
          }
            
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_CityOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

         if(lista.isEmpty())return null;
         return lista;
    }

    @Override
    public List<Integer> getShops(int i) {
       List<Integer> lista=new ArrayList<>();
        try(
            PreparedStatement prep=con.prepareStatement("select idProdavnice from prodavnica where idGrada=?");){
             prep.setInt(1, i);
          try(ResultSet rs=prep.executeQuery()) {
              while(rs.next()){
                  lista.add(rs.getInt(1));
              }
          }
            
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_CityOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(lista.isEmpty())return null;
         return lista;
    }
    
}
