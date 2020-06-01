package com.accounts.services.accountservice;

import com.accounts.models.TelephoneNumber;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Scope("prototype")
public interface GetNumberService {
    List<TelephoneNumber> getNumbers();
}
