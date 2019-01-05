package com.moneymoney.account.dao;

import java.sql.SQLException;
import java.util.List;

import com.moneymoney.account.CurrentAccount;
import com.moneymoney.exception.AccountNotFoundException;

public interface CurrentAccountDAO {

	CurrentAccount createNewAccount(CurrentAccount account) throws ClassNotFoundException, SQLException;
	
	CurrentAccount getAccountById(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException;
	CurrentAccount deleteAccount(int accountNumber) throws ClassNotFoundException, SQLException;
	List<CurrentAccount> getAllCurrentAccount() throws ClassNotFoundException, SQLException;
	void updateBalance(int accountNumber, double currentBalance) throws ClassNotFoundException, SQLException;
	void commit() throws SQLException;
	
	CurrentAccount getAccountByName(String nameToSearch) throws ClassNotFoundException, SQLException, AccountNotFoundException;
	List<CurrentAccount> getAccountByBalanceRange(double minimumBalance,
			double highestBalance) throws ClassNotFoundException, SQLException;

	List<CurrentAccount> sort(int choice, int toSortIn) throws ClassNotFoundException, SQLException;

	CurrentAccount updateAccount(CurrentAccount currentAccountToUpdate) throws ClassNotFoundException, SQLException;

}
