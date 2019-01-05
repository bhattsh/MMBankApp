package com.moneymoney.account.service;

import java.sql.SQLException;
import java.util.List;

import com.moneymoney.account.SavingsAccount;
import com.moneymoney.exception.AccountNotFoundException;

public interface SavingsAccountService {

	SavingsAccount createNewAccount(String accountHolderName, double accountBalance, boolean salary) throws ClassNotFoundException, SQLException;

//	SavingsAccount updateAccount(int accountNumber, String toUpdate);

	SavingsAccount getAccountById(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException;

	SavingsAccount deleteAccount(int accountNumber) throws ClassNotFoundException, SQLException;
	
	List<SavingsAccount> getAllSavingsAccount() throws ClassNotFoundException, SQLException;

	void fundTransfer(SavingsAccount sender, SavingsAccount receiver, double amount) throws ClassNotFoundException, SQLException;
	void deposit(SavingsAccount account, double amount) throws ClassNotFoundException, SQLException;
	void withdraw(SavingsAccount account, double amount) throws ClassNotFoundException, SQLException;


//	SavingsAccount updateAccount(int accountNumber, int toUpdate,String update) throws ClassNotFoundException, SQLException, AccountNotFoundException;

	SavingsAccount getAccountByName(String nameToSearch) throws ClassNotFoundException, SQLException, AccountNotFoundException;

	SavingsAccount updateAccount(SavingsAccount savingAccountToUpdate) throws ClassNotFoundException, SQLException;

	List<SavingsAccount> getAccountByBalanceRange(double minimumBalance,
			double highestBalance) throws ClassNotFoundException, SQLException;


	List<SavingsAccount> sort(int choice, int toSortIn) throws ClassNotFoundException, SQLException;

//	SavingsAccount searchAccount(String nameToSearch) throws ClassNotFoundException, SQLException;

	

	
}











