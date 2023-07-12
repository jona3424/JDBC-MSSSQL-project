/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.BuyerOperations;

/**
 *
 * @author jona
 */
public class pn200680_BuyerOperationsImpl implements BuyerOperations {
    Connection con= db.getInstance().getConnection();

    @Override
    public int createBuyer(String string, int i) {
        
    try (
            PreparedStatement prep=con.prepareStatement("Insert into Kupac(idGrada,naziv,pare) values(?,?,0)",Statement.RETURN_GENERATED_KEYS);){
            
            prep.setString(2, string);
            prep.setInt(1, i);
            
            if(prep.executeUpdate()==1){
               try (ResultSet rs= prep.getGeneratedKeys();){
                   while(rs.next())return rs.getInt(1);
               }
            }
            return -1;
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_ArticleOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    
    
    }

    @Override
    public int setCity(int i, int i1) {
        
         try (
            PreparedStatement prep=con.prepareStatement("Update Kupac set idGrada = ? where idKupca=?");){
        
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
    public int getCity(int i) {
       
        try (
            PreparedStatement prep=con.prepareStatement("Select idGrada from Kupac where idKupca=?");){
        
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
    public BigDecimal increaseCredit(int i, BigDecimal bd) {
        
        try (
            PreparedStatement prep=con.prepareStatement("Update Kupac set pare += ? where idKupca=?");){
        
            prep.setBigDecimal(1, bd);
            prep.setInt(2, i);
            
            
            if(prep.executeUpdate()==1){
                return getCredit(i);
            }
            
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_ShopOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
             return null;

        }
        
        
    }

    @Override
    public int createOrder(int i) {
            
       try (
            PreparedStatement prep=con.prepareStatement("Insert into Porudzbina(idKupca,dodatniPopust) values(?,0)",Statement.RETURN_GENERATED_KEYS);
                ){
            prep. setInt(1,i);
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
    public List<Integer> getOrders(int i) {
        List<Integer> lista=null;  
       try (
            PreparedStatement prep=con.prepareStatement("Select idPorudzbine from Porudzbina where idKupca=?");){
        
            prep.setInt(1, i);
             try(ResultSet rs= prep.executeQuery();){
                 lista=new ArrayList<>();
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
    public BigDecimal getCredit(int i) {
       try (
            PreparedStatement prep=con.prepareStatement("Select pare from Kupac where idKupca=?");){
        
            prep.setInt(1, i);
            
            try(ResultSet rs=prep.executeQuery()){
                while(rs.next())return rs.getBigDecimal(1);
            }
            
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_ShopOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
             return null;

        } 
        
    }
    
}
