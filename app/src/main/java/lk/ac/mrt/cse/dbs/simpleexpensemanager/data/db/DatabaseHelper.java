package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;


public class DatabaseHelper extends SQLiteOpenHelper {

    public  static final String DB_NAME = "180552R";
    public  static final String TABLE_1 = "account";
    public  static final String TABLE_2 = "transaction_table";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 2);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override

    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String ddl_q_1 = "create table "+TABLE_1+" (accountNo TEXT(50) PRIMARY KEY,bankName TEXT(50),accountHolderName TEXT(50),balance REAL) ";
        String ddl_q_2 =" create table "+TABLE_2+" (accountNo TEXT(50) ,date date, expenseType TEXT(20),amount REAL,FOREIGN KEY (accountNo) REFERENCES "+TABLE_1+"(accountNo))";
        
        sqLiteDatabase.execSQL(ddl_q_1);
        sqLiteDatabase.execSQL(ddl_q_2);
    }

    @Override

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        
        String delete_q1 = "DROP TABLE IF EXISTS "+TABLE_1;
        String delete_q2 ="DROP TABLE IF EXISTS "+TABLE_2;
        
        sqLiteDatabase.execSQL(delete_q1);
        sqLiteDatabase.execSQL(delete_q2);
        onCreate(sqLiteDatabase);
    }

    public boolean insertAccount(Account account){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        
        contentValues.put("accountNo",account.getAccountNo());
        contentValues.put("bankName",account.getBankName());
        contentValues.put("accountHolderName",account.getAccountHolderName());
        contentValues.put("balance",account.getBalance());
        long result = db.insert(TABLE_1,null,contentValues);
        
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }


    public boolean updateAccount(Account account){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("accountNo",account.getAccountNo());
        contentValues.put("bankName",account.getBankName());
        contentValues.put("accountHolderName",account.getAccountHolderName());
        contentValues.put("balance",account.getBalance());
        long result = db.update(TABLE_1,contentValues,"accountNo = ?",new String[]{account.getAccountNo()});
        
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }


    public Account getAccount(String accNo){
        
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_1+" WHERE accountNo = ?",new String[]{accNo});
        Account account = null;
        if(res.getCount() == 0){
            return account;
        }else{
            while(res.moveToNext()){
                String accountNo = res.getString(0);
                String bankName = res.getString(1);
                String accountHolderName = res.getString(2);
                double balance = res.getDouble(3);
                account = new Account(accountNo,bankName,accountHolderName,balance);
            }
            return account;
        }
    }


    public ArrayList<Account> getAllAccounts(){
        
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_1,null);
        ArrayList<Account> accountList=new ArrayList<>();
        if(res.getCount()==0){
            return accountList;
        }else{

            while(res.moveToNext()){
                String accountNo = res.getString(0);
                String bankName = res.getString(1);
                String accountHolderName = res.getString(2);
                double balance = res.getDouble(3);
                accountList.add(new Account(accountNo,bankName,accountHolderName,balance));
            }
            return accountList;
        }
    }


    public boolean deleteAccount(String accountNo){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_1,"accountNo = "+accountNo,null) > 0;

    }


    public boolean logTransaction(Transaction transaction){

        DateFormat format = new SimpleDateFormat("m-d-yyyy", Locale.ENGLISH);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        
        contentValues.put("accountNo",transaction.getAccountNo());
        contentValues.put("date",format.format(transaction.getDate()));
        contentValues.put("expenseType",transaction.getExpenseType().toString());
        contentValues.put("amount",transaction.getAmount());


        long res = db.insert(TABLE_2,null,contentValues);
        if(res == -1){
            return false;
        }else{
            return true;
        }



    }

    public ArrayList<Transaction> getTransactions(){
        
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_2,null);
        return populateTransactions(res);
    }


    public ArrayList<Transaction> getTransactions(int limit){
      
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_2+" LIMIT "+limit,null);
        return populateTransactions(res);
    }



    private ArrayList<Transaction> populateTransactions(Cursor res){

        ArrayList<Transaction> transactionList=new ArrayList<>();
        DateFormat format = new SimpleDateFormat("m-d-yyyy", Locale.ENGLISH);
       
        if(res.getCount()==0){
            return transactionList;
        }else{

            while(res.moveToNext()){
                String accountNo = res.getString(0);
                Date date = new Date();
                ExpenseType expenseType = ExpenseType.valueOf(res.getString(2));
                try {
                    date =  format.parse(res.getString(1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                double amount = res.getDouble(3);
                transactionList.add(new Transaction(date,accountNo,expenseType,amount));
            }
            return transactionList;
        }
        
    }
}
