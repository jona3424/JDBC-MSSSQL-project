/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.ShopOperations;

/**
 *
 * @author jona
 */
public class pn200680_ShopOperationsImpl implements ShopOperations {
    Connection con= db.getInstance().getConnection();

    @Override
    public int createShop(String string, String string1) {
        try (
            PreparedStatement prep=con.prepareStatement("Insert into Prodavnica(popust,naziv,idGrada) select 0,?,g.idGrada from grad g where g.naziv=?",Statement.RETURN_GENERATED_KEYS);
                ){
            prep.setString(1, string);
            prep.setString(2, string1);
            if(prep.executeUpdate()==1){
              try( ResultSet rs= prep.getGeneratedKeys();){
                  while(rs.next()){
                      return rs.getInt(1);
                  }
              }
            }return -1;
        
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_ShopOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    @Override
    public int setCity(int i, String string) {
        try (
            PreparedStatement prep=con.prepareStatement("Update prodavnica set idGrada = (select g.idGrada from grad g where g.naziv=?) where idProdavnice=?");){
        
            prep.setString(1, string);
            prep.setInt(2, i);
            
            
            if(prep.executeUpdate()==1)return 1;
            
            return -1;
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_ShopOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
             return -1;

        }
    
    
    }

    @Override
    public int getCity(int i) {
           try (
            PreparedStatement prep=con.prepareStatement("Select idGrada from Prodavnica where idProdavnice=?");){
        
            prep.setInt(1, i);
            
            try(ResultSet rs=prep.executeQuery()){
                while(rs.next())return rs.getInt(1);
            }
            
            return -1;
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_ShopOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
             return -1;

        }
    

    }

    @Override
    public int setDiscount(int i, int i1) {
      
     try (
            PreparedStatement prep=con.prepareStatement("Update prodavnica set popust = ? where idProdavnice=?");
             
             ){
        
            prep.setInt(1, i1);
            prep.setInt(2, i);
            
            
            if(prep.executeUpdate()==1)return 1;
            
            return -1;
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_ShopOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
             return -1;

        }
    
    }

    @Override
    public int increaseArticleCount(int i, int i1) {
         try (
            PreparedStatement prep=con.prepareStatement("Update Artikal set kolicina += ? where idArtikla=?");){
        
            prep.setInt(1, i1);
            prep.setInt(2, i);
            
            
            if(prep.executeUpdate()==1){
                return getArticleCount(i);
            }
            
            return -1;
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_ShopOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
             return -1;

        }
    }

    @Override
    public int getArticleCount(int i) {
        
        try (
            PreparedStatement prep=con.prepareStatement("Select kolicina from Artikal where idArtikla=?");){
        
            prep.setInt(1, i);
            
            try(ResultSet rs=prep.executeQuery()){
                while(rs.next())return rs.getInt(1);
            }
            
            return -1;
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_ShopOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
             return -1;

        } 
        
         }

    @Override
    public List<Integer> getArticles(int i) {
      List<Integer> lista=new ArrayList<>();  
       try (
            PreparedStatement prep=con.prepareStatement("Select idArtikla from Artikal where idProdavnice=?");){
        
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
    public int getDiscount(int i) {
       
       try (
            PreparedStatement prep=con.prepareStatement("Select popust from Prodavnica where idProdavnice=?");){
        
            prep.setInt(1, i);
            
            try(ResultSet rs=prep.executeQuery()){
                while(rs.next())return rs.getInt(1);
            }
            
            return -1;
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_ShopOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
             return -1;

        }
    
    }
    
}
