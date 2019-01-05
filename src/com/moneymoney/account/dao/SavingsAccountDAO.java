package com.moneymoney.account.dao;

import java.sql.SQLException;
import java.util.List;

import com.moneymoney.account.SavingsAccount;
import com.moneymoney.exception.AccountNotFoundException;

public interface SavingsAccountDAO {
	
	SavingsAccount createNewAccount(SavingsAccount account) throws ClassNotFoundException, SQLException;
//	SavingsAccount updateAccount(SavingsAccount account);
	SavingsAccount getAccountById(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException;
	SavingsAccount deleteAccount(int accountNumber) throws ClassNotFoundException, SQLException;
	List<SavingsAccount> getAllSavingsAccount() throws ClassNotFoundException, SQLException;
	void updateBalance(int accountNumber, double currentBalance) throws ClassNotFoundException, SQLException;
	void commit() throws SQLException;
	
//	SavingsAccount updateAccount(int accountNumber, int toUpdate, String update) throws ClassNotFoundException, SQLException, AccountNotFoundException;
	SavingsAccount getAccountByName(String nameToSearch) throws ClassNotFoundException, SQLException, AccountNotFoundException;
	SavingsAccount updateAccount(SavingsAccount savingAccountToUpdate) throws ClassNotFoundException, SQLException;
	List<SavingsAccount> getAccountByBalanceRange(double minimumBalance,
			double highestBalance) throws ClassNotFoundException, SQLException;


	List<SavingsAccount> sort(int choice, int toSortIn) throws ClassNotFoundException, SQLException;

	//SavingsAccount searchAccount(String accountToSearch) throws ClassNotFoundException, SQLException;

	
	
}
