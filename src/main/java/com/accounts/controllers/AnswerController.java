package com.accounts.controllers;

import com.accounts.models.Person;
import com.accounts.models.ProxyServer;
import com.accounts.models.TelephoneNumber;
import com.accounts.services.accountservice.CreateAccountService;
import com.accounts.services.accountservice.CreateAccountServiceImpl;
import com.accounts.services.accountservice.GetNumberService;
import com.accounts.services.dataservice.PutDataService;
import com.accounts.services.proxyservice.ProxyParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
public final class AnswerController {
    @Autowired
    private CreateAccountService createAccountService;
    @Autowired
    private PutDataService putDataService;
    @Autowired
    private ProxyParserService proxyParserService;
    @Autowired
    private GetNumberService getNumberService;
    @Autowired
    private ApplicationContext context;
    private final static int NUMBER_OF_THREADS = 5;
    private final static int TIME_TO_WAIT = 60;

    @Value("${web.driver.path}")
    private String webDriverPath;

    @GetMapping("/putNumbersInDataBase")
    public ResponseEntity<List<TelephoneNumber>> putNumbersInDataBase() {
        List<TelephoneNumber> telephoneNumbers = getNumberService.getNumbers();
        return new ResponseEntity<>(telephoneNumbers, HttpStatus.OK);
    }

    @GetMapping("/getOneAccount")
    public ResponseEntity<Person> getCreatedAccount() {
        Person personData = createAccountService.getPersonData();
        return new ResponseEntity<>(personData, HttpStatus.OK);
    }

    @GetMapping("/getFiveAccounts")
    public ResponseEntity<List<Person>> getTenCreatedAccount() {
        List<Person> persons = new ArrayList<>();

        ExecutorService es = Executors.newCachedThreadPool();

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            es.execute(() -> {
                CreateAccountService accountService = context.getBean(CreateAccountServiceImpl.class);
                Person personData = accountService.getPersonData();
                persons.add(personData);
            });
        }
        es.shutdown();
        try {
            es.awaitTermination(TIME_TO_WAIT, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    @GetMapping("/putData")
    public ResponseEntity putData() {
        putDataService.putFemaleFirstNames("src\\main\\resources\\data\\female_first_names");
        putDataService.putFemaleLastNames("src\\main\\resources\\data\\female_last_names");
        putDataService.putMaleFirstNames("src\\main\\resources\\data\\male_first_names");
        putDataService.putMaleLastNames("src\\main\\resources\\data\\male_last_names");
        return new ResponseEntity<>("Succes", HttpStatus.OK);
    }

    @GetMapping("/getProxyServersList")
    public ResponseEntity<List<ProxyServer>> getProxyServersList() {
        List<ProxyServer> proxyServers = proxyParserService.getProxies();
        return new ResponseEntity<>(proxyServers, HttpStatus.OK);
    }
}
