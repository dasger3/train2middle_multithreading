package task5.model;

import lombok.Data;

@Data
public class Account {
    private Long id;
    private String name;
    private String surname;
    private Wallet wallet;
}
