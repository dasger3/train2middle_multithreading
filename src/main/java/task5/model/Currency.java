package task5.model;

public enum Currency {
    USD("USD"),
    EUR("EUR"),
    UAN("UAN");

    private final String currency;

    Currency(String title) {
        this.currency = title;
    }

    public String getCurrency() {
        return currency;
    }

    /*public static TypeOfATU getTypeByUrl(String url) throws ObjectNotFoundException {
        for (TypeOfATU env : values()) {
            if (env.getTitle().equals(url)) {
                return env;
            }
        }
        throw new ObjectNotFoundException(TypeOfATU.class.getName(), url);
    }*/
}
