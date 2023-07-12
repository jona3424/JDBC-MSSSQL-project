/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.ArticleOperations;

/**
 *
 * @author jona
 */
public class pn200680_ArticleOperationsImpl implements ArticleOperations {
    Connection con= db.getInstance().getConnection();

    @Override
    public int createArticle(int i, String string, int i1) {
        try (
            PreparedStatement prep=con.prepareStatement("Insert into Artikal(idProdavnice,naziv,cijena,kolicina) values(?,?,?,0)",Statement.RETURN_GENERATED_KEYS);){
            
            prep.setInt(1, i);
            prep.setString(2, string);
            prep.setInt(3, i1);
            
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
    
}
