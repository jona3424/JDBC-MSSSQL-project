package rs.etf.sab.student;


import rs.etf.sab.tests.TestHandler;
import rs.etf.sab.tests.TestRunner;

import java.util.Calendar;
import rs.etf.sab.operations.ArticleOperations;
import rs.etf.sab.operations.BuyerOperations;
import rs.etf.sab.operations.CityOperations;
import rs.etf.sab.operations.GeneralOperations;
import rs.etf.sab.operations.OrderOperations;
import rs.etf.sab.operations.ShopOperations;
import rs.etf.sab.operations.TransactionOperations;

public class StudentMain {

    public static void main(String[] args) {

        ArticleOperations articleOperations = new pn200680_ArticleOperationsImpl();
        BuyerOperations buyerOperations = new pn200680_BuyerOperationsImpl();
        CityOperations cityOperations = new pn200680_CityOperationsImpl();
        GeneralOperations generalOperations = new pn200680_GeneralOperationsImpl();
        OrderOperations orderOperations = new pn200680_OrderOperationsImpl();
        ShopOperations shopOperations = new pn200680_ShopOperationsImpl();
        TransactionOperations transactionOperations = new pn200680_TransactionOperationsImpl();

        TestHandler.createInstance(
                articleOperations,
                buyerOperations,
                cityOperations,
                generalOperations,
                orderOperations,
                shopOperations,
                transactionOperations
        );

        TestRunner.runTests();
    }
}
