package com.accounts.services.dataservice;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public interface PutDataService {
    void putFemaleFirstNames(String path);
    void putFemaleLastNames(String path);
    void putMaleFirstNames(String path);
    void putMaleLastNames(String path);
}
