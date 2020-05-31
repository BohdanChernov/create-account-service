package com.accounts.services.accountservice;

import com.accounts.models.Person;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public interface CreateAccountService {
    Person getPersonData();
}
