package task5.repository;

import org.springframework.stereotype.Repository;
import task5.exception.ObjectNotFoundException;
import task5.model.Account;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Repository
public class AccountRepository {
    private final List<Account> accountList = new CopyOnWriteArrayList<>();

    public List<Account> getAllAccounts() {
        return accountList.stream().sorted(Comparator.comparing(Account::getId)).collect(Collectors.toList());
    }

    public Account findById(Long id) throws ObjectNotFoundException {
        return accountList.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(()-> new ObjectNotFoundException(String.valueOf(Account.class), id));
    }

    public void addAccount(Account account) {
        account.setId(accountList.size()+1L);
        accountList.add(account);
    }

    public void updateAccount(Account account, Long id) {
        Optional<Account> tmp = accountList.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();

        if(tmp.isEmpty()) addAccount(account);
        else {
            accountList.remove(tmp.get());
            account.setId(id);
            accountList.add(account);
        }
    }

    public void deleteAll() {
        accountList.clear();
    }
}
