package com.moneymoney.account.ui;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.moneymoney.account.CurrentAccount;
import com.moneymoney.account.SavingsAccount;
import com.moneymoney.account.service.CurrentAccountService;
import com.moneymoney.account.service.CurrentAccountServiceImpl;
import com.moneymoney.account.service.SavingsAccountService;
import com.moneymoney.account.service.SavingsAccountServiceImpl;
import com.moneymoney.account.util.DBUtil;
import com.moneymoney.exception.AccountNotFoundException;

public class AccountCUI {
	private static Scanner scanner = new Scanner(System.in);
	private static SavingsAccountService savingsAccountService=new SavingsAccountServiceImpl();

	private static CurrentAccountService currentAccountService=new CurrentAccountServiceImpl();
	

	public static void start() {
		
		do {
			System.out.println("****** Welcome to Money Money Bank********");
			System.out.println("1. Open New Savings Account");
			System.out.println("2. Update Account");
			System.out.println("3. Close Account");
			System.out.println("4. Search Account");
			System.out.println("5. Withdraw");
			System.out.println("6. Deposit");
			System.out.println("7. FundTransfer");
			System.out.println("8. Check Current Balance");
			System.out.println("9. Get All Savings Account Details");
			System.out.println("10. Sort Accounts");
			System.out.println("11. Exit");
			System.out.println();
			System.out.println("Make your choice: ");
			
			int choice = scanner.nextInt();
			
			performOperation(choice);
			
		} while(true);
	}

	private static void performOperation(int choice) {
		switch (choice) {
		case 1:
			System.out.println("Choose what type of account you would like to open: \n 1.Saving \n 2.Current");
			int accountType = scanner.nextInt();
			
			if(accountType == 1)
				acceptInput("SA");
			else if(accountType  == 2)
				acceptInput("CA");
			else
				System.out.println("please choose a correct ");
			//acceptInput("SA");
			break;
		case 2:
			updateAccount();
			break;
		case 3:
			 deleteAccount();
			break;
		case 4:
			searchAccount();
			break;
		case 5:
			withdraw();
			break;
		case 6:
			deposit();
			break;
		case 7:
			fundTransfer();
			break;
		case 8:
			checkBalance();
			break;
		case 9:
			showAllAccounts();
			break;
		case 10:
			sortAccounts();
			break;
		case 11:
			try {
				DBUtil.closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.exit(0);
			break;
		default:
			System.err.println("Invalid Choice!");
			break;
		}		
	}


	private static void checkBalance() {
		System.out.println("Enter Account Number: ");
		int accountNumber = scanner.nextInt();
		
		SavingsAccount account;
		try {
			account = savingsAccountService.getAccountById(accountNumber);
			System.out.println("Balance in your account is: "+account.getBankAccount().getAccountBalance());
		}
		catch (ClassNotFoundException | SQLException | AccountNotFoundException e) {
			e.printStackTrace();
		}
	}


	private static void sortAccounts() {
		do {
			System.out.println("+++++Ways of Sorting+++++++");
			System.out.println("1. Account Number");
			System.out.println("2. Account Holder Name");
			System.out.println("3. Account Balance");
			System.out.println("4. Exit from Sorting");
			int choice = scanner.nextInt();
			
			if(choice ==4 )
				break;
			
			System.out.println("Choose how you want to sort: \n 1.Ascending /n  2.Descending");
			int toSortIn = scanner.nextInt();
			
				//System.out.println(savingsAccountService.sort(choice,toSortIn));
		
			
			switch(choice)
			{
			case 1:
				try {
					List<SavingsAccount> sortedAccounts = savingsAccountService.sort(choice,toSortIn);
					System.out.println(sortedAccounts);
				} 
				catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				System.exit(0);
				break;
			default:
					System.out.println("invalid choice");
			}
		}while(true);	
	}

	private static void searchAccount() {
		System.out.println("Choose how you want to search : \n 1.Using Account Number \n 2.Using Accountholder Name \n 3.Using Account Balance Range");
		int choosedValue = scanner.nextInt();
		
		switch(choosedValue)
		{
		case 1:
			System.out.println("Enter account number to search: ");
			int accountNumber = scanner.nextInt();
			
			SavingsAccount account;
			try {
				account = savingsAccountService.getAccountById(accountNumber);
				System.out.println(account);
			}
			catch (ClassNotFoundException | SQLException | AccountNotFoundException e) {
				e.printStackTrace();
			}
			break;
		
		case 2:
			System.out.println("Enter accountHolderName to search: ");
			String accountToSearch = scanner.nextLine();
			accountToSearch = scanner.nextLine();
			
			try {							
				account = savingsAccountService.getAccountByName(accountToSearch);			//account reference type is already created in 
				System.out.println(account);
			}
			catch (ClassNotFoundException | SQLException | AccountNotFoundException e) {
				e.printStackTrace();
			}
			break;
			
		case 3:
			System.out.println("Enter minimun balance: ");
			double minimumBalance = scanner.nextInt();
			
			System.out.println("Enter highest balance: ");
			double highestBalance = scanner.nextInt();

			try {
				List<SavingsAccount> savingAccount = savingsAccountService.getAccountByBalanceRange(minimumBalance, highestBalance);
				System.out.println(savingAccount);
			}
			catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
			break;
		}
	}	
	/*
	private static void getAccountByName() {
		System.out.println("Enter accountHolderName to search: ");
		String nameToSearch = scanner.nextLine();
		nameToSearch = scanner.nextLine();
		try {
			SavingsAccount account = savingsAccountService.getAccountByName(nameToSearch);
			System.out.println(account);
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		} 
		catch (AccountNotFoundException e) {
			e.printStackTrace();
		}	
		
	}

	private static void getAccountById() {							
		System.out.println("Enter account number: ");
		int accountNumber = scanner.nextInt();
		try {
			SavingsAccount account = savingsAccountService.getAccountById(accountNumber);
			System.out.println(account);
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (AccountNotFoundException e) {
			e.printStackTrace();
		}	
		
	}*/
	
	
	private static void updateAccount() {						//remaining : invalid input conditions
		System.out.println("Enter your account number: ");
		int inputAccountNumber = scanner.nextInt();

		SavingsAccount account;
		try {
			account = savingsAccountService.getAccountById(inputAccountNumber);		//should i forward my request first to serviceImp first 
			System.out.println(account);
			int accountNumber = account.getBankAccount().getAccountNumber();
			double accountBalance = account.getBankAccount().getAccountBalance();
			String accountHolderName = account.getBankAccount().getAccountHolderName();
			boolean isSalaried = account.isSalary(); 
			
			System.out.println("Choose what you want to update: \n 1.Account Holder Name \n 2. Salaried Or not \n 3. Both");
			int optionSelected = scanner.nextInt();
			
			switch(optionSelected)
			{
			case 1:
				System.out.println("Enter new name: ");
				accountHolderName = scanner.nextLine();
				accountHolderName = scanner.nextLine();
				break;
			case 2:
				System.out.println("Select Yes if you want to mnake your account salaried else type No");
				String updateSalaried = scanner.next();
				isSalaried = updateSalaried.equalsIgnoreCase("yes") ? true : false;
				break;
			case 3:
				System.out.println("Enter new name: ");
				accountHolderName = scanner.nextLine();
				accountHolderName = scanner.nextLine();
				
				System.out.println("Select Yes if you want to mnake your account salaried else type No");
				updateSalaried = scanner.next();
				isSalaried = updateSalaried.equalsIgnoreCase("yes") ? true : false;
				break;
			default:
				System.out.println("please enter a valid choice");
			}
			SavingsAccount savingAccountToUpdate = new SavingsAccount(accountNumber, accountHolderName, accountBalance, isSalaried);
		System.out.println("hello");
			SavingsAccount updatedAccount =savingsAccountService.updateAccount(savingAccountToUpdate);
			System.out.println("hello 2");

			if(updatedAccount != null)
				System.out.println(updatedAccount);
			else
				System.out.println("account cannot be updated");
		}
		catch (ClassNotFoundException | SQLException | AccountNotFoundException e1) {
			e1.printStackTrace();
		}
	}


	
	private static void acceptInput(String type) {
		System.out.println("Enter your Full Name: ");
		String accountHolderName = scanner.nextLine();
		accountHolderName = scanner.nextLine();
		System.out.println("Enter Initial Balance(type na for Zero Balance): ");
		String accountBalanceStr = scanner.next();
		double accountBalance=0.0;
		if(!accountBalanceStr.equalsIgnoreCase("na")) {
			accountBalance = Double.parseDouble(accountBalanceStr);
		}

		if(type.equals("SA")) 
		{
			System.out.println("Salaried?(y/n): ");
			boolean salary = scanner.next().equalsIgnoreCase("n")?false:true;
			createSavingsAccount(accountHolderName,accountBalance, salary);
		}
		else
		{
			System.out.println("enter odLimit: ");
			double odLimit = scanner.nextInt();
			createCurrentAccount(accountHolderName, accountBalance, odLimit);
		}
			
	}
	
	
	private static void createCurrentAccount(String accountHolderName, double accountBalance, double odLimit) {
		
		try {
			currentAccountService.createNewAccount(accountHolderName, accountBalance, odLimit);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private static void createSavingsAccount(String accountHolderName, double accountBalance, boolean salary) {
		try {
			savingsAccountService.createNewAccount(accountHolderName, accountBalance, salary);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	private static void deleteAccount() {
		System.out.println("Enter Your Account Number: ");
		int accountNumber = scanner.nextInt();
		
		try {
		SavingsAccount account = savingsAccountService.deleteAccount(accountNumber);
		if(account == null)
			System.out.println("account with account number "+ accountNumber +" closed successfully");
		} catch (ClassNotFoundException | SQLException e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	
	private static void fundTransfer() {
		System.out.println("Enter Account Sender's Number: ");
		int senderAccountNumber = scanner.nextInt();
		System.out.println("Enter Account Receiver's Number: ");
		int receiverAccountNumber = scanner.nextInt();
		System.out.println("Enter Amount: ");
		double amount = scanner.nextDouble();
		try {
			SavingsAccount senderSavingsAccount = savingsAccountService.getAccountById(senderAccountNumber);
			SavingsAccount receiverSavingsAccount = savingsAccountService.getAccountById(receiverAccountNumber);
			savingsAccountService.fundTransfer(senderSavingsAccount, receiverSavingsAccount, amount);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void deposit() {
		System.out.println("Enter Account Number: ");
		int accountNumber = scanner.nextInt();
		System.out.println("Enter Amount: ");
		double amount = scanner.nextDouble();
		SavingsAccount savingsAccount = null;
		try {
			savingsAccount = savingsAccountService.getAccountById(accountNumber);
			savingsAccountService.deposit(savingsAccount, amount);
			DBUtil.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			try {
				DBUtil.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			try {
				DBUtil.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	private static void withdraw() {
		System.out.println("Enter Account Number: ");
		int accountNumber = scanner.nextInt();		
		System.out.println("Enter from which account you would like to withdraw the amount: \n 1.Saving \n 2.Current");
		int accountType = scanner.nextInt();
		
		System.out.println("Enter Amount: ");
		double amount = scanner.nextDouble();
		
		SavingsAccount savingsAccount = null;
		CurrentAccount currentAccount = null;
		
		try {
			if(accountType==1)
			{	
			
				savingsAccount = savingsAccountService.getAccountById(accountNumber);
				savingsAccountService.withdraw(savingsAccount, amount);
				DBUtil.commit();
			}
			else
			{
				currentAccount = currentAccountService.getAccountById(accountNumber);
				currentAccountService.withdraw(currentAccount, amount);
				DBUtil.commit();
			}
		} 
		catch (ClassNotFoundException | SQLException | AccountNotFoundException e) {
			e.printStackTrace();
		}
			
		
		}
	private static void showAllAccounts() {
		List<SavingsAccount> savingsAccounts;
		try {
			savingsAccounts = savingsAccountService.getAllSavingsAccount();
			for (SavingsAccount savingsAccount : savingsAccounts) {
				System.out.println(savingsAccount);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
	}	
	
}

