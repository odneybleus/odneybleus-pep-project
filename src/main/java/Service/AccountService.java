package Service;
import java.util.*;
import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account addAccount(Account account) {
        List<Account> accounts = accountDAO.getAllAccounts();
        for (Account a : accounts){
            if(a.getUsername() == account.getUsername()){
                return null;
            }
        }
        return accountDAO.insertAccount(account);
    }
    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }

    public List<Integer> getAllAccountsByID() {
        return accountDAO.getAccountsID();
    }

    public Account getAccount(Account account){
        return accountDAO.getLogin(account);
    }
}