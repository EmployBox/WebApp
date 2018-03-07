package dataMapping.mappers;

import dataMapping.utils.MapperSettings;
import model.Account;

public abstract class AccountMapper<T extends Account> extends AbstractMapper<T, Long> {
    private final String SELECT_QUERY = "SELECT AccountID, Email, passwordHash, Rating FROM Account WHERE AccountID = ?";

    public AccountMapper(MapperSettings insertSettings, MapperSettings updateSettings, MapperSettings deleteSettings) {
        super(insertSettings, updateSettings, deleteSettings);
    }
}
