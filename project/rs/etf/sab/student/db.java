/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jona
 */
public class db {
    private static final String username="sa";
    private static final String password="123";
    private static final int port=1433;
    private static final String database="SabProjekat";
    private static final String server="localhost";
   
    private static final String connurl="jdbc:sqlserver://"+server+":"+port+";DatabaseName=" + database + ";encrypt=true;trustServerCertificate=true";

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    private static db klasica;
    private db(){
        try {
            connection=DriverManager.getConnection(connurl,username,password);
            System.out.println(connection);
        } catch (SQLException ex) {
            Logger.getLogger(db.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static db getInstance(){
        if(klasica==null){
            klasica=new db();
        }
        return klasica;
    }
}
