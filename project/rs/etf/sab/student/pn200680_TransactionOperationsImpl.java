/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.TransactionOperations;

/**
 *
 * @author jona
 */
public class pn200680_TransactionOperationsImpl implements TransactionOperations {
    Connection con= db.getInstance().getConnection();

    @Override
    public BigDecimal getBuyerTransactionsAmmount(int i) {
        
        try (
            PreparedStatement prep=con.prepareStatement("Select sum(cijena) from Transakcije where idKupca=?");){
            
            prep.setInt(1, i);
           try( ResultSet rs= prep.executeQuery();){
               while(rs.next())
                   return rs.getBigDecimal(1);
           }
           return null;
            
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_TransactionOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    
    }

    @Override
    public BigDecimal getShopTransactionsAmmount(int i) {
            
        try (
            PreparedStatement prep=con.prepareStatement("Select COALESCE(sum(cijena),0) from Transakcije where idProdavnice=?");){
            
            prep.setInt(1, i);
           try( ResultSet rs= prep.executeQuery();){
               while(rs.next())
                   return rs.getBigDecimal(1);
           }
           return new BigDecimal("0").setScale(3);
            
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_TransactionOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
            return new BigDecimal("0").setScale(3);
        }
        
    }

    @Override
    public List<Integer> getTransationsForBuyer(int i) {
        List<Integer> lista= new ArrayList<>();
       try(PreparedStatement prep= con.prepareStatement("Select idTransakcije from Transakcije where idKupca=?");){
            prep.setInt(1, i);
            try( ResultSet rs= prep.executeQuery();){
               while(rs.next())
                   lista.add(rs.getInt(1));
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(pn200680_TransactionOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        if(lista.isEmpty())return null;
         return lista;
       
    }

    @Override
    public int getTransactionForBuyersOrder(int i) {
         try (
            PreparedStatement prep=con.prepareStatement("Select idTransakcije from Transakcije where idPorudzbine=? and idKupca =(select idKupca from porudzbina where idPorudzbine=?)");){
            
            prep.setInt(1, i);
            prep.setInt(2, i);
           try( ResultSet rs= prep.executeQuery();){
               while(rs.next())
                   return rs.getInt(1);
           }
           return -1;
            
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_TransactionOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    @Override
    public int getTransactionForShopAndOrder(int i, int i1) {
         try (
            PreparedStatement prep=con.prepareStatement("Select idTransakcije from Transakcije where idProdavnice=? and idPorudzbine=?");){
             
            prep.setInt(1, i1);
            prep.setInt(2, i);
           try( ResultSet rs= prep.executeQuery();){
               while(rs.next())
                   return rs.getInt(1);
           }
           return -1;
            
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_TransactionOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    @Override
    public List<Integer> getTransationsForShop(int i) {
        List<Integer> lista= new ArrayList<>();
       try(PreparedStatement prep= con.prepareStatement("Select idTransakcije from Transakcije where idProdavnice=?");){
            prep.setInt(1, i);
            try( ResultSet rs= prep.executeQuery();){
               while(rs.next())
                   lista.add(rs.getInt(1));
           }
            
                  }
        catch (SQLException ex) {
            Logger.getLogger(pn200680_TransactionOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
       if(lista.isEmpty())return null;
         return lista;

    }

    @Override
    public Calendar getTimeOfExecution(int i) {
         try (
            PreparedStatement prep=con.prepareStatement("Select datumTransakcije from Transakcije where idTransakcije=?");){
            
            prep.setInt(1, i);
           try( ResultSet rs= prep.executeQuery();){
               while(rs.next()){
                   Calendar c= Calendar.getInstance();
                   c.setTime(rs.getDate(1));
                   return c;
               }
                   
           }
           return null;
            
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_TransactionOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public BigDecimal getAmmountThatBuyerPayedForOrder(int i) {
         try (
            PreparedStatement prep=con.prepareStatement("Select cijena from Transakcije where idPorudzbine=? and idKupca =(select idKupca from porudzbina where idPorudzbine=?)");){
            
            prep.setInt(1, i);
            prep.setInt(2, i);
           try( ResultSet rs= prep.executeQuery();){
               while(rs.next())
                   return rs.getBigDecimal(1);
           }
           return null;
            
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_TransactionOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public BigDecimal getAmmountThatShopRecievedForOrder(int i, int i1) {
        try (
            PreparedStatement prep=con.prepareStatement("Select cijena from Transakcije where idPorudzbine=? and idProdavnice=?");){
            
            prep.setInt(1, i1);
            prep.setInt(2, i);
           try( ResultSet rs= prep.executeQuery();){
               while(rs.next())
                   return rs.getBigDecimal(1);
           }
           return null;
            
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_TransactionOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }

    @Override
    public BigDecimal getTransactionAmount(int i) {
        try (
            PreparedStatement prep=con.prepareStatement("Select cijena from Transakcije where idTransakcije=?");){
            
            prep.setInt(1, i);
           try( ResultSet rs= prep.executeQuery();){
               while(rs.next())
                   return rs.getBigDecimal(1);
           }
           return null;
            
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_TransactionOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public BigDecimal getSystemProfit() {
        try (
            PreparedStatement prep=con.prepareStatement("Select COALESCE(sum(cijena),0) from Transakcije where idProdavnice is null and idKupca is null");){
            
           try( ResultSet rs= prep.executeQuery();){
               while(rs.next())
                   return rs.getBigDecimal(1);
           }
           return null;
            
        } catch (SQLException ex) {
            Logger.getLogger(pn200680_TransactionOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } 
    }
    
}
