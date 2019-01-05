package com.moneymoney.account.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.moneymoney.account.SavingsAccount;
import com.moneymoney.account.util.DBUtil;
import com.moneymoney.exception.AccountNotFoundException;

public class SavingsAccountDAOImpl implements SavingsAccountDAO {

	public SavingsAccount createNewAccount(SavingsAccount account) throws ClassNotFoundException, SQLException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ACCOUNT( account_hn, account_balance, account_isSalary, account_type, odLimit) VALUES(?,?,?,?,?)");
//		preparedStatement.setInt(1, account.getBankAccount().getAccountNumber());
		preparedStatement.setString(1, account.getBankAccount().getAccountHolderName());
		preparedStatement.setDouble(2, account.getBankAccount().getAccountBalance());
		preparedStatement.setBoolean(3, account.isSalary());
		preparedStatement.setString(4, "SA");
		preparedStatement.setObject(5, null);
//		preparedStatement.setInt(6, 1000);   //just to start account id from 1000
		preparedStatement.executeUpdate();
		
		preparedStatement.close();
		
		DBUtil.commit();
		
		return account;
	}

	public List<SavingsAccount> getAllSavingsAccount() throws ClassNotFoundException, SQLException {
		List<SavingsAccount> savingsAccounts = new ArrayList<>();
		Connection connection = DBUtil.getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM ACCOUNT");
		while (resultSet.next()) {// Check if row(s) is present in table
			int accountNumber = resultSet.getInt(1);
			String accountHolderName = resultSet.getString("account_hn");
			double accountBalance = resultSet.getDouble(3);
			boolean salary = resultSet.getBoolean("account_isSalary");
			SavingsAccount savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance,
					salary);
			savingsAccounts.add(savingsAccount);
		}
		DBUtil.commit();
		return savingsAccounts;
	}
	@Override
	public void updateBalance(int accountNumber, double currentBalance) throws ClassNotFoundException, SQLException {
		Connection connection = DBUtil.getConnection();
		connection.setAutoCommit(false);
		PreparedStatement preparedStatement = connection.prepareStatement("UPDATE ACCOUNT SET account_balance=? where account_id=?");
		preparedStatement.setDouble(1, currentBalance);
		preparedStatement.setInt(2, accountNumber);
		preparedStatement.executeUpdate();
	}
	
	@Override
	public SavingsAccount getAccountById(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement
				("SELECT * FROM account where account_id=?");
		preparedStatement.setInt(1, accountNumber);
		ResultSet resultSet = preparedStatement.executeQuery();
		SavingsAccount savingsAccount = null;
		if(resultSet.next()) {
			String accountHolderName = resultSet.getString("account_hn");
			double accountBalance = resultSet.getDouble(3);
			boolean salary = resultSet.getBoolean("account_isSalary");
			savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance, salary);
			return savingsAccount;
		}
		throw new AccountNotFoundException("Account with account number "+accountNumber+" does not exist.");
	}
	

	@Override
	public SavingsAccount updateAccount(SavingsAccount savingAccountToUpdate) throws ClassNotFoundException, SQLException 
	{
		Connection connection = DBUtil.getConnection();
		System.out.println("hello 3");

		PreparedStatement preparedStatement = connection.prepareStatement("UPDATE ACCOUNT SET account_hn=?, account_balance=?, account_isSalary=? WHERE account_id=?");
		preparedStatement.setString(1, savingAccountToUpdate.getBankAccount().getAccountHolderName());
		preparedStatement.setDouble(2, savingAccountToUpdate.getBankAccount().getAccountBalance());
		preparedStatement.setBoolean(3, savingAccountToUpdate.isSalary());
		preparedStatement.setInt(4, savingAccountToUpdate.getBankAccount().getAccountNumber());
		int result = preparedStatement.executeUpdate();
		System.out.println("hello 4");

		DBUtil.commit();
		
		if(result == 1)		//is this is a right way or i should store values is variables first
			return new SavingsAccount(savingAccountToUpdate.getBankAccount().getAccountNumber(),savingAccountToUpdate.getBankAccount().getAccountHolderName(),
					savingAccountToUpdate.getBankAccount().getAccountBalance(),savingAccountToUpdate.isSalary());
		else
			return savingAccountToUpdate;
	
	}

	@Override
	public SavingsAccount deleteAccount(int accountNumber) throws ClassNotFoundException, SQLException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM account WHERE account_id=?");
		preparedStatement.setInt(1, accountNumber);
		int result = preparedStatement.executeUpdate();
	//	System.out.println(preparedStatement.executeUpdate());
		if(result == 0)
			System.out.println("account details are invalid");
		DBUtil.commit();
		return null;
	}

	@Override
	public void commit() throws SQLException {
		DBUtil.commit();
	}

	/*@Override																								//to modify search
	public SavingsAccount searchAccount(String nameToSearch) throws ClassNotFoundException, SQLException {
		Connection connection = DBUtil.getConnection();
		
		PreparedStatement preparedStatement = connection.prepareStatement
				("SELECT * FROM account where account_hn=?");
		return null;
	}*/

	@Override
	public SavingsAccount getAccountByName(String nameToSearch) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement
				("SELECT * FROM account where account_hn=?");
		preparedStatement.setString(1, nameToSearch);
		ResultSet resultSet = preparedStatement.executeQuery();
		SavingsAccount savingsAccount = null;
		if(resultSet.next()) {
			int accountNumber = resultSet.getInt("account_id");
			double accountBalance = resultSet.getDouble(3);
			boolean salary = resultSet.getBoolean("account_isSalary");
			savingsAccount = new SavingsAccount(accountNumber, nameToSearch, accountBalance, salary);
			return savingsAccount;
		}
		throw new AccountNotFoundException("Account with account holder name "+nameToSearch+" does not exist.");
	}

	@Override
	public List<SavingsAccount> getAccountByBalanceRange(double minimumBalance, double highestBalance) throws ClassNotFoundException, SQLException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM account where account_balance BETWEEN ? AND ?");
		preparedStatement.setDouble(1,minimumBalance);
		preparedStatement.setDouble(2, highestBalance);
		ResultSet resultSet = preparedStatement.executeQuery();
		
		List<SavingsAccount> listOfAccounts = new ArrayList();
		while(resultSet.next())
		{
			int accountNumber = resultSet.getInt("account_id");
			String accountHolderName = resultSet.getString("account_hn");
			double accountBalance = resultSet.getDouble(3);
			boolean salary = resultSet.getBoolean("account_isSalary");
			String accountType = resultSet.getString("account_type");
			SavingsAccount savingsAccount = new SavingsAccount(accountNumber,accountHolderName, accountBalance, salary);
			listOfAccounts.add(savingsAccount);
		}
		return listOfAccounts;
	}

	@Override
	public List<SavingsAccount> sort(int choice, int toSortIn) throws ClassNotFoundException, SQLException {
		
		Connection connection = DBUtil.getConnection();
		Statement stmt = connection.createStatement();
		ResultSet resultSet = null;
		switch(choice)
		{
		case 1:
			if(toSortIn ==1)
			resultSet = stmt.executeQuery("SELECT * FROM account ORDER BY account_id");
			else
				resultSet = stmt.executeQuery("SELECT * FROM account ORDER BY account_id DESC");
			break;
		case 2:
			if(toSortIn ==1)
				resultSet = stmt.executeQuery("SELECT * FROM account ORDER BY account_hn");
			else
				resultSet = stmt.executeQuery("SELECT * FROM account ORDER BY account_hn DESC");
				break;
		case 3:
			if(toSortIn ==1)
				resultSet = stmt.executeQuery("SELECT * FROM account ORDER BY account_balance");
			else
				resultSet = stmt.executeQuery("SELECT * FROM account ORDER BY account_balance DESC");
				break;
		}
		
		
		List<SavingsAccount> sortedAccountList = new ArrayList();
		while(resultSet.next())
		{

			int accountNumber = resultSet.getInt("account_id");
			String accountHolderName = resultSet.getString("account_hn");
			double accountBalance = resultSet.getDouble(3);
			boolean salary = resultSet.getBoolean("account_isSalary");
			String accountType = resultSet.getString("account_type");
			SavingsAccount savingsAccount = new SavingsAccount(accountNumber,accountHolderName, accountBalance, salary);
			sortedAccountList.add(savingsAccount);
		}
		return sortedAccountList;
	}
}
