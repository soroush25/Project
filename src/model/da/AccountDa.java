package src.model.da;

import lombok.extern.log4j.Log4j;
import src.model.entity.Account;
import src.model.entity.Customer;
import src.model.entity.enums.BankAccountTypes;
import src.model.entity.enums.Banks;
import src.model.tools.CRUD;
import src.model.tools.ConnectionProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class AccountDa implements AutoCloseable, CRUD<Account> {
    private final Connection connection;
    private PreparedStatement preparedStatement;

    public AccountDa() throws SQLException {
        connection = ConnectionProvider.getConnectionProvider().getConnection();
    }

    @Override
    public Account save(Account account) throws Exception {
        account.setAccountNumber(ConnectionProvider.getConnectionProvider().getNextId("account_seq"));
        preparedStatement = connection.prepareStatement(
                "INSERT INTO ACCOUNT (accountNumber, balance, customer_id, bank, accountTypes) VALUES (?,?,?,?,?)"
        );
        preparedStatement.setInt(1, account.getAccountNumber());
        preparedStatement.setInt(2, account.getBalance());
        preparedStatement.setInt(3, account.getCustomer().getId());
        preparedStatement.setString(4, String.valueOf(account.getBank()));
        preparedStatement.setString(5, String.valueOf(account.getAccountTypes()));
        preparedStatement.execute();
        return account;
    }

    @Override
    public Account edit(Account account) throws Exception {
        preparedStatement = connection.prepareStatement(
                "UPDATE ACCOUNT SET balance = ?, customer_id = ?, bank = ?, accountTypes = ? WHERE AccountNumber = ?"
        );
        preparedStatement.setInt(1, account.getBalance());
        preparedStatement.setInt(2, account.getCustomer().getId());
        preparedStatement.setString(3, String.valueOf(account.getBank()));
        preparedStatement.setString(4, String.valueOf(account.getAccountTypes()));
        preparedStatement.setInt(5, account.getAccountNumber());
        preparedStatement.execute();
        return account;
    }

    @Override
    public Account remove(int id) throws Exception {
        preparedStatement = connection.prepareStatement(
                "DELETE FROM ACCOUNT WHERE AccountNumber = ?"
        );
        preparedStatement.setInt(1, id);
        preparedStatement.execute();
        return null;
    }

    @Override
    public List<Account> findAll() throws Exception {
        List<Account> accountList = new ArrayList<>();
        preparedStatement = connection.prepareStatement("SELECT * FROM ACCOUNT ORDER BY AccountNumber");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Account account = Account
                    .builder()
                    .accountNumber(resultSet.getInt("AccountNumber"))
                    .balance(resultSet.getInt("Balance"))
                    .customer(Customer.builder().id(resultSet.getInt("Customer_id")).build())
                    .bank(Banks.valueOf(resultSet.getString("Bank")))
                    .accountTypes(BankAccountTypes.valueOf(resultSet.getString("AccountTypes")))
                    .build();
            accountList.add(account);
        }
        return accountList;
    }

    @Override
    public Account findById(int id) throws Exception {
        preparedStatement = connection.prepareStatement("SELECT * FROM ACCOUNT WHERE AccountNumber = ?");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        Account account = null;
        if (resultSet.next()) {
            account = Account
                    .builder()
                    .accountNumber(resultSet.getInt("AccountNumber"))
                    .balance(resultSet.getInt("Balance"))
                    .customer(Customer.builder().id(resultSet.getInt("Customer_id")).build())
                    .bank(Banks.valueOf(resultSet.getString("Bank")))
                    .accountTypes(BankAccountTypes.valueOf(resultSet.getString("AccountTypes")))
                    .build();
        }
        return account;
    }

    public Account findByCustomerId(String customer) throws Exception {
        preparedStatement = connection.prepareStatement("SELECT * FROM ACCOUNT WHERE customer_id = ? ORDER BY accountNumber");
        preparedStatement.setString(1, customer);
        ResultSet resultSet = preparedStatement.executeQuery();
        Account account = null;
        if (resultSet.next()) {
            account = Account
                    .builder()
                    .accountNumber(resultSet.getInt("AccountNumber"))
                    .balance(resultSet.getInt("Balance"))
                    .customer(Customer.builder().id(resultSet.getInt("Customer_id")).build())
                    .bank(Banks.valueOf(resultSet.getString("Bank")))
                    .accountTypes(BankAccountTypes.valueOf(resultSet.getString("AccountTypes")))
                    .build();
        }
        return account;
    }

    public List<Account> findByBankName(String bank) throws Exception {
        List<Account> accountList = new ArrayList<>();
        preparedStatement = connection.prepareStatement("SELECT * FROM ACCOUNT WHERE bank LIKE ? ORDER BY accountNumber");
        preparedStatement.setString(1, bank + "%");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Account account = Account
                    .builder()
                    .accountNumber(resultSet.getInt("AccountNumber"))
                    .balance(resultSet.getInt("Balance"))
                    .customer(Customer.builder().id(resultSet.getInt("Customer_id")).build())
                    .bank(Banks.valueOf(resultSet.getString("Bank")))
                    .accountTypes(BankAccountTypes.valueOf(resultSet.getString("AccountTypes")))
                    .build();
            accountList.add(account);
        }
        return accountList;
    }

    public List<Account> findByAccountType(String types) throws Exception {
        List<Account> accountList = new ArrayList<>();
        preparedStatement = connection.prepareStatement("SELECT * FROM ACCOUNT WHERE accountTypes LIKE ? ORDER BY accountNumber");
        preparedStatement.setString(1, types + "%");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Account account = Account
                    .builder()
                    .accountNumber(resultSet.getInt("AccountNumber"))
                    .balance(resultSet.getInt("Balance"))
                    .customer(Customer.builder().id(resultSet.getInt("Customer_id")).build())
                    .bank(Banks.valueOf(resultSet.getString("Bank")))
                    .accountTypes(BankAccountTypes.valueOf(resultSet.getString("AccountTypes")))
                    .build();
            accountList.add(account);
        }
        return accountList;
    }

    @Override
    public void close() throws Exception {
        preparedStatement.close();
        connection.close();
    }
}
