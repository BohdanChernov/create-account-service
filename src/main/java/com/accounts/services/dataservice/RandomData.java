package com.accounts.services.dataservice;

import com.accounts.models.Sex;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public interface RandomData {
    String getFirstName(Sex sex);
    String getLastName(Sex sex);
    String getEmail(String firstName, String lastName);
    String getPassword();
    Sex getState();
}
