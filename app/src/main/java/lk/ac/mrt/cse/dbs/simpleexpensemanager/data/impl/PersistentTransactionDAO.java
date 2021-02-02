package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;

import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;


public class PersistentTransactionDAO implements TransactionDAO {
    
    private DatabaseHelper dbHelper;

    public  PersistentTransactionDAO(Context context){
        dbHelper = new DatabaseHelper(context);
    }

    @Override
    
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date,accountNo,expenseType,amount);
        if(accountNo != null){
            dbHelper.logTransaction(transaction);
        }

    }

    @Override
    
    public List<Transaction> getAllTransactionLogs() {
        return dbHelper.getTransactions();
    }

    @Override
    
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        return dbHelper.getTransactions(limit);
    }
    
}
