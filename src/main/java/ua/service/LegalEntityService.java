package ua.service;

import ua.model.Home;
import ua.model.LegalEntity;

import javax.validation.constraints.NotNull;

public interface LegalEntityService {

    LegalEntity getCurrent();

    LegalEntity getBySubDomain(@NotNull String subdomain);

    LegalEntity update(LegalEntity legalEntity);

    Home setDefaultHome(Home home);
}
