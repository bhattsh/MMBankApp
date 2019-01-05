package com.moneymoney.account.service;

import java.sql.SQLException;
import java.util.List;

import com.moneymoney.account.CurrentAccount;
import com.moneymoney.account.dao.CurrentAccountDAO;
import com.moneymoney.account.dao.CurrentAccountDAOImpl;
import com.moneymoney.account.factory.AccountFactory;
import com.moneymoney.account.util.DBUtil;
import com.moneymoney.exception.AccountNotFoundException;
import com.moneymoney.exception.InsufficientFundsException;
import com.moneymoney.exception.InvalidInputException;

public class CurrentAccountServiceImpl implements CurrentAccountService {

	private AccountFactory factory;
	private CurrentAccountDAO currentAccountDAO;

	public CurrentAccountServiceImpl() {
		factory = AccountFactory.getInstance();
		currentAccountDAO = new CurrentAccountDAOImpl();
	}

	@Override
	public CurrentAccount createNewAccount(String accountHolderName, double accountBalance, double odLimit)throws ClassNotFoundException, SQLException {
		CurrentAccount account = factory.createNewCurrentAccount(accountHolderName, accountBalance, odLimit);
		currentAccountDAO.createNewAccount(account);
		return null;
	}

	@Override
	public List<CurrentAccount> getAllCurrentAccount() throws ClassNotFoundException, SQLException {
		return currentAccountDAO.getAllCurrentAccount();
	}

	@Override
	public void deposit(CurrentAccount account, double amount) throws ClassNotFoundException, SQLException {
		if (amount > 0) {
			double currentBalance = account.getBankAccount().getAccountBalance();
			currentBalance += amount;
//			savingsAccountDAO.updateBalance(account.getBankAccount().getAccountNumber(), currentBalance);
			//savingsAccountDAO.commit();
		}else {
			throw new InvalidInputException("Invalid Input Amount!");
		}
	}
	@Override
	public void withdraw(CurrentAccount account, double amount) throws ClassNotFoundException, SQLException {
		double currentBalance = account.getBankAccount().getAccountBalance();
		double odLimit = account.getOdLimit();
		if ((amount > 0) && ((currentBalance+odLimit) >= amount)) {
			currentBalance -= amount;
			currentAccountDAO.updateBalance(account.getBankAccount().getAccountNumber(), currentBalance);
			//savingsAccountDAO.commit();
		} else {
			throw new InsufficientFundsException("Invalid Input or Insufficient Funds!");
		}
	}

	@Override
	public void fundTransfer(CurrentAccount sender, CurrentAccount receiver, double amount)
			throws ClassNotFoundException, SQLException {
		try {
			withdraw(sender, amount);
			deposit(receiver, amount);
			DBUtil.commit();
		} catch (InvalidInputException | InsufficientFundsException e) {
			e.printStackTrace();
			DBUtil.rollback();
		} catch(Exception e) {
			e.printStackTrace();
			DBUtil.rollback();
		}
	}

/*	@Override
	public SavingsAccount updateAccount(int accountNumber, int toUpdate, String update) throws ClassNotFoundException, SQLException, AccountNotFoundException 
	{
		return savingsAccountDAO.updateAccount(accountNumber, toUpdate, update);
	}
*/
	
	@Override
	public CurrentAccount updateAccount(CurrentAccount currentAccountToUpdate) throws ClassNotFoundException, SQLException {
		 return currentAccountDAO.updateAccount(currentAccountToUpdate);
	}

	
	@Override
	public CurrentAccount getAccountById(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		return currentAccountDAO.getAccountById(accountNumber);
	}

	@Override
	public CurrentAccount getAccountByName(String nameToSearch) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		
		return currentAccountDAO.getAccountByName(nameToSearch);
	}
	
	
	@Override
	public CurrentAccount deleteAccount(int accountNumber) throws ClassNotFoundException, SQLException {
		currentAccountDAO.deleteAccount(accountNumber);
		return null;
	}
/*
	@Override
	public SavingsAccount searchAccount(String nameToSearch) throws ClassNotFoundException, SQLException {
		return savingsAccountDAO.searchAccount(nameToSearch);
	}

*/

	@Override
	public List<CurrentAccount> getAccountByBalanceRange(double minimumBalance, double highestBalance) throws ClassNotFoundException, SQLException {
		return currentAccountDAO.getAccountByBalanceRange(minimumBalance, highestBalance);
	}

	@Override
	public List<CurrentAccount> sort(int choice, int toSortIn) throws ClassNotFoundException, SQLException {
		return currentAccountDAO.sort(choice,toSortIn);
		
	}
	
}
