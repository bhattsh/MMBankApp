package com.moneymoney.account;

public class CurrentAccount {

		private double odLimit;
		private BankAccount bankAccount;

		public CurrentAccount(String accountHolderName, double accountBalance, double odLimit) {
			bankAccount = new BankAccount(accountHolderName, accountBalance);
			this.odLimit = odLimit;
		}
		public CurrentAccount(String accountHolderName, double odLimit) {
			bankAccount = new BankAccount(accountHolderName);
			this.odLimit = odLimit;
		}

		public CurrentAccount(int accountNumber, String accountHolderName, double accountBalance, double odLimit) {
			bankAccount = new BankAccount(accountNumber, accountHolderName, accountBalance);
			this.odLimit = odLimit;
		}
		
		public BankAccount getBankAccount() {
			return bankAccount;
		}

		public void setBankAccount(BankAccount bankAccount) {
			this.bankAccount = bankAccount;
		}
		
		/**
		 * @return the odLimit
		 */
		public double getOdLimit() {
			return odLimit;
		}
		/**
		 * @param odLimit the odLimit to set
		 */
		public void setOdLimit(double odLimit) {
			this.odLimit = odLimit;
		}
		
		
		@Override
		public String toString() {
			return "CurrentAccount [odLimit=" + odLimit + ", bankAccount=" + bankAccount + "]";
		}

		
		
}
