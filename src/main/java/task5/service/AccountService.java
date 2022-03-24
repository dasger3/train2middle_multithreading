package task5.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import task5.exception.ObjectNotFoundException;
import task5.model.Account;
import task5.model.Currency;
import task5.model.Wallet;
import task5.repository.AccountRepository;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Map;

@Service
public class AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository, Environment environment) {
        this.accountRepository = accountRepository;
        initFromListOfFiles(environment.getRequiredProperty("template.account.files", String[].class));
    }

    public Account findAccountById(Long id) throws ObjectNotFoundException {
        return accountRepository.findById(id);
    }

    public void updateWalletById(Wallet wallet, Long accountId) throws ObjectNotFoundException {
        Account account = findAccountById(accountId);
        account.setWallet(wallet);
        accountRepository.updateAccount(account, accountId);
    }

    public void initFromListOfFiles(String[] files) {
        accountRepository.deleteAll();
        for (String file : files) {
            initAccountFromFile("accounts/" + file);
        }
    }

    public void initAccountFromFile(String filename) {
        try {
            String[] lines = FileService.readFromFile(filename).split("\n");
            String[] nameSurname = lines[0].split(" ");

            Account account = new Account();
            account.setName(nameSurname[0]);
            account.setSurname(nameSurname[1]);
            Wallet wallet = new Wallet();

            for (int i = 1; i < lines.length; i++) {
                String[] line = lines[i].split(" ");
                wallet.getBalance().put(Currency.valueOf(line[0]), BigDecimal.valueOf(Double.parseDouble(line[1])));
            }
            account.setWallet(wallet);

            accountRepository.addAccount(account);

        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
        }
    }

    public void printAccounts() {
        System.out.println("--------------------");
        for (Account account : accountRepository.getAllAccounts()) {
            System.out.println("Account");
            System.out.println("ID: " + account.getId());
            System.out.println("Name: " + account.getName());
            System.out.println("Surname: " + account.getSurname());
            System.out.println("Wallet: ");
            for (Map.Entry<Currency, BigDecimal> entry : account.getWallet().getBalance().entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            System.out.println("---");
        }
        System.out.println("-------------------");
    }
}
