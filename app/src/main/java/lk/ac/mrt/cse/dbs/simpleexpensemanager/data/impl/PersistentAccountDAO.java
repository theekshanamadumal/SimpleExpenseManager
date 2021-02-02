package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;


public class PersistentAccountDAO implements AccountDAO {

    private DatabaseHelper dbHelper;

    public  PersistentAccountDAO(Context context){
        dbHelper = new DatabaseHelper(context);
    }

    @Override

    public List<String> getAccountNumbersList() {
        ArrayList<String> accountNumbers = new ArrayList<>();
        ArrayList<Account> accounts = dbHelper.getAllAccounts();
        if(accounts.size()==0){
            return accountNumbers;
        }else {
            for(Account a:accounts){
                accountNumbers.add(a.getAccountNo());
            }
        }
        return accountNumbers;
    }

    @Override

    public List<Account> getAccountsList() {
        return dbHelper.getAllAccounts();
    }

    @Override

    public Account getAccount(String accountNo) throws InvalidAccountException {
        return null;
    }

    @Override

    public void addAccount(Account account) {
        dbHelper.insertAccount(account);
    }

    @Override

    public void removeAccount(String accountNo) throws InvalidAccountException {
        dbHelper.deleteAccount(accountNo);
    }

    @Override

    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        if(accountNo ==null){
            throw new InvalidAccountException("Invalid Account Number");

        }
        Account account = dbHelper.getAccount(accountNo);
        double balance = account.getBalance();
        if(expenseType == ExpenseType.INCOME){
            account.setBalance(balance+amount);
        }else if (expenseType == ExpenseType.EXPENSE){
            account.setBalance(balance-amount);

        }
        if(account.getBalance()<0 ){
            throw new InvalidAccountException("Insufficient credit");
        }

        else{
            dbHelper.updateAccount(account);
        }

    }
    
}
