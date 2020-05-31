package com.accounts.services.accountservice;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public interface GetNumberService {
    String getNumber();
}
