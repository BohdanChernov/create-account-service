package com.accounts.services.accountservice;

import com.accounts.dao.TelephoneNumberDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class GetNumberServiceImpl implements GetNumberService{
    @Value("${url.to.get.numbers}")
    private String urlToConnect;
    @Autowired
    private TelephoneNumberDAO telephoneNumberDAO;

    @Override
    public String getNumber() {
        return null;
    }

    public void parseNumbersToDataBase() {

    }
}
