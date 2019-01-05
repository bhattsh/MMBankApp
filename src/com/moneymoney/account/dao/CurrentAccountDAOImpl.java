package com.moneymoney.account.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.moneymoney.account.CurrentAccount;
import com.moneymoney.account.util.DBUtil;
import com.moneymoney.exception.AccountNotFoundException;

public class CurrentAccountDAOImpl implements CurrentAccountDAO {

	public CurrentAccount createNewAccount(CurrentAccount account) throws ClassNotFoundException, SQLException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ACCOUNT( account_hn, account_balance, account_type, odLimit) VALUES(?,?,?,?)");
//		preparedStatement.setInt(1, account.getBankAccount().getAccountNumber());
		preparedStatement.setString(1, account.getBankAccount().getAccountHolderName());
		preparedStatement.setDouble(2, account.getBankAccount().getAccountBalance());
		preparedStatement.setString(3, "CA");
		preparedStatement.setDouble(4, account.getOdLimit());
//		preparedStatement.setInt(6, 1000);   //just to start account id from 1000
		preparedStatement.executeUpdate();
		
		preparedStatement.close();
		
		DBUtil.commit();
		
		return account;
	}

	public List<CurrentAccount> getAllCurrentAccount() throws ClassNotFoundException, SQLException {
		List<CurrentAccount> currentAccounts = new ArrayList<>();
		Connection connection = DBUtil.getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM ACCOUNT");
		while (resultSet.next()) {// Check if row(s) is present in table
			int accountNumber = resultSet.getInt(1);
			String accountHolderName = resultSet.getString("account_hn");
			double accountBalance = resultSet.getDouble(3);
			double odLimit = resultSet.getDouble("odLimit");
			CurrentAccount currentAccount = new CurrentAccount(accountNumber, accountHolderName, accountBalance,odLimit);
			currentAccounts.add(currentAccount);
		}
		DBUtil.commit();
		return currentAccounts;
	}
	
	@Override
	public void updateBalance(int accountNumber, double currentBalance) throws ClassNotFoundException, SQLException {
		Connection connection = DBUtil.getConnection();
		connection.setAutoCommit(false);
		PreparedStatement preparedStatement = connection.prepareStatement
				("UPDATE ACCOUNT SET account_bal=? where account_id=?");
		preparedStatement.setDouble(1, currentBalance);
		preparedStatement.setInt(2, accountNumber);
		preparedStatement.executeUpdate();
	}
	
	@Override
	public CurrentAccount getAccountById(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement
				("SELECT * FROM account where account_id=?");
		preparedStatement.setInt(1, accountNumber);
		ResultSet resultSet = preparedStatement.executeQuery();
		CurrentAccount currentAccount = null;
		if(resultSet.next()) {
			String accountHolderName = resultSet.getString("account_hn");
			double accountBalance = resultSet.getDouble(3);
			double odLimit = resultSet.getDouble("odLimit");
			currentAccount = new CurrentAccount(accountNumber, accountHolderName, accountBalance, odLimit);
			return currentAccount;
		}
		throw new AccountNotFoundException("Account with account number "+accountNumber+" does not exist.");
	}
	

	@Override
	public CurrentAccount updateAccount(CurrentAccount currentAccountToUpdate) throws ClassNotFoundException, SQLException 
	{
		Connection connection = DBUtil.getConnection();
		System.out.println("hello 3");

		PreparedStatement preparedStatement = connection.prepareStatement("UPDATE ACCOUNT SET account_hn=?, account_balance=?, account_isSalary=? WHERE account_id=?");
		preparedStatement.setString(1, currentAccountToUpdate.getBankAccount().getAccountHolderName());
		preparedStatement.setDouble(2, currentAccountToUpdate.getBankAccount().getAccountBalance());
		preparedStatement.setDouble(3, currentAccountToUpdate.getOdLimit());
		preparedStatement.setInt(4, currentAccountToUpdate.getBankAccount().getAccountNumber());
		int result = preparedStatement.executeUpdate();
		System.out.println("hello 4");

		DBUtil.commit();
		
		if(result == 1)		//is this is a right way or i should store values is variables first
			return new CurrentAccount(currentAccountToUpdate.getBankAccount().getAccountNumber(), currentAccountToUpdate.getBankAccount().getAccountHolderName(),
					currentAccountToUpdate.getBankAccount().getAccountBalance(),currentAccountToUpdate.getOdLimit());
		else
			return currentAccountToUpdate;
	
	}

	@Override
	public CurrentAccount deleteAccount(int accountNumber) throws ClassNotFoundException, SQLException {
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
	public CurrentAccount getAccountByName(String nameToSearch) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement
				("SELECT * FROM account where account_hn=?");
		preparedStatement.setString(1, nameToSearch);
		ResultSet resultSet = preparedStatement.executeQuery();
		CurrentAccount currentAccount = null;
		if(resultSet.next()) {
			int accountNumber = resultSet.getInt("account_id");
			double accountBalance = resultSet.getDouble(3);
			double odLimit = resultSet.getDouble("odLimit");
			currentAccount = new CurrentAccount(accountNumber, nameToSearch, accountBalance, odLimit);
			return currentAccount;
		}
		throw new AccountNotFoundException("Account with account holder name "+nameToSearch+" does not exist.");
	}

	@Override
	public List<CurrentAccount> getAccountByBalanceRange(double minimumBalance, double highestBalance) throws ClassNotFoundException, SQLException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM account where account_balance BETWEEN ? AND ?");
		preparedStatement.setDouble(1,minimumBalance);
		preparedStatement.setDouble(2, highestBalance);
		ResultSet resultSet = preparedStatement.executeQuery();
		
		List<CurrentAccount> listOfAccounts = new ArrayList();
		while(resultSet.next())
		{
			int accountNumber = resultSet.getInt("account_id");
			String accountHolderName = resultSet.getString("account_hn");
			double accountBalance = resultSet.getDouble(3);
			double odLimit = resultSet.getDouble("odLimit");
			String accountType = resultSet.getString("account_type");
			CurrentAccount currentAccounts = new CurrentAccount(accountNumber,accountHolderName, accountBalance, odLimit);
			listOfAccounts.add(currentAccounts);
		}
		return listOfAccounts;
	}

	@Override
	public List<CurrentAccount> sort(int choice, int toSortIn) throws ClassNotFoundException, SQLException {
		
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
		
		
		List<CurrentAccount> sortedAccountList = new ArrayList();
		while(resultSet.next())
		{

			int accountNumber = resultSet.getInt("account_id");
			String accountHolderName = resultSet.getString("account_hn");
			double accountBalance = resultSet.getDouble(3);
			double odLimit = resultSet.getDouble("odLimit");
			String accountType = resultSet.getString("account_type");
			CurrentAccount currentAccounts = new CurrentAccount(accountNumber,accountHolderName, accountBalance, odLimit);
			sortedAccountList.add(currentAccounts);
		}
		return sortedAccountList;
	}

}
