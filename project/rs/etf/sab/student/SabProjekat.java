/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package rs.etf.sab.student;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.ArticleOperations;
import rs.etf.sab.operations.BuyerOperations;
import rs.etf.sab.operations.CityOperations;
import rs.etf.sab.operations.GeneralOperations;
import rs.etf.sab.operations.OrderOperations;
import rs.etf.sab.operations.ShopOperations;
import rs.etf.sab.operations.TransactionOperations;

/**
 *
 * @author jona
 */
public class SabProjekat {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        GeneralOperations gop=new pn200680_GeneralOperationsImpl();
        gop.eraseAll();
        gop.setInitialTime(Calendar.getInstance(Locale.ITALY));
      
        CityOperations cop=new pn200680_CityOperationsImpl();
        cop.createCity("B");
        cop.createCity("C1");
        cop.createCity("C5");
        cop.createCity("A");
        cop.createCity("C2");
        cop.createCity("C3");
        cop.createCity("C4");
        cop.connectCities(1, 2, 8);
        cop.connectCities(1, 3, 2);
        cop.connectCities(2, 4, 10);
        cop.connectCities(3, 4, 15);
        cop.connectCities(4, 5, 3);
        cop.connectCities(4, 7, 3);
        cop.connectCities(6, 5, 2);
        cop.connectCities(6, 7, 1);
        
        System.out.println(cop.getConnectedCities(1));
        System.out.println(cop.getConnectedCities(2));
        System.out.println(cop.getCities());
        System.out.println(cop.getShops(1));
        
        ShopOperations sop=new  pn200680_ShopOperationsImpl();
        sop.createShop("vanjyshop", "A");
        sop.createShop("nikolyshop", "C2");
        sop.createShop("nikolyshop11", "C3");

        ArticleOperations aop=new pn200680_ArticleOperationsImpl();
        aop.createArticle(2, "macky", 100);
        aop.createArticle(3,"lopta", 10);

        
        System.out.println(sop.increaseArticleCount(1,20));
        System.out.println(sop.increaseArticleCount(2,20));

   
          
        BuyerOperations bop= new pn200680_BuyerOperationsImpl();
        System.out.println(bop.createBuyer("Nikoly",1)); 
          
        System.out.println(bop.createOrder(1));          
        System.out.println(bop.increaseCredit(1,new BigDecimal(6900.69)));


       
        OrderOperations oop=new pn200680_OrderOperationsImpl();
 
        System.out.println(oop.addArticle(1, 1, 6));
        System.out.println(oop.addArticle(1, 2, 5));
 
        System.out.println(oop.completeOrder(1));
        System.out.println(gop.time(20));
        System.out.println(oop.getState(1));
        
        
        TransactionOperations top=new pn200680_TransactionOperationsImpl();
        System.out.println(top.getTransationsForBuyer(1));
        System.out.println(top.getSystemProfit());
        System.out.println(top.getShopTransactionsAmmount(2));

    }
    
}
