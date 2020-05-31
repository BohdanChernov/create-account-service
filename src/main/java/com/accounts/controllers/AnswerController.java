package com.accounts.controllers;

import com.accounts.models.Person;
import com.accounts.models.ProxyServer;
import com.accounts.services.accountservice.CreateAccountService;
import com.accounts.services.accountservice.CreateAccountServiceImpl;
import com.accounts.services.proxyservice.ProxyParserService;
import com.accounts.services.dataservice.PutDataService;
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
public class AnswerController {
    @Autowired
    private CreateAccountService createAccountService;
    @Autowired
    private PutDataService putDataService;
    @Autowired
    private ProxyParserService proxyParserService;
    @Autowired
    private ApplicationContext context;

    @Value("${web.driver.path}")
    private String webDriverPath;

    @GetMapping("/getOneAccount")
    public ResponseEntity<Person> getCreatedAccount() {
        Person personData = createAccountService.getPersonData();
        ResponseEntity<Person> jsonPersonData = new ResponseEntity<>(personData, HttpStatus.OK);
        return jsonPersonData;
    }

    @GetMapping("/getFiveAccounts")
    public ResponseEntity<List<Person>> getTenCreatedAccount() {
        List<Person> persons = new ArrayList<>();

        ExecutorService es = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++)
            es.execute(() -> {
                CreateAccountService accountService = context.getBean(CreateAccountServiceImpl.class);
                Person personData = accountService.getPersonData();
                persons.add(personData);
            });
        es.shutdown();
        try {
            boolean finished = es.awaitTermination(60, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ResponseEntity<List<Person>> jsonPersonData = new ResponseEntity<>(persons, HttpStatus.OK);
        return jsonPersonData;
    }

    @GetMapping("/putData")
    public ResponseEntity putData() {
        putDataService.putFemaleFirstNames("src\\main\\resources\\data\\female_first_names");
        putDataService.putFemaleLastNames("src\\main\\resources\\data\\female_last_names");
        putDataService.putMaleFirstNames("src\\main\\resources\\data\\male_first_names");
        putDataService.putMaleLastNames("src\\main\\resources\\data\\male_last_names");
        ResponseEntity<String> jsonSuccess = new ResponseEntity<>("Succes", HttpStatus.OK);
        return jsonSuccess;
    }

    @GetMapping("/getProxyServersList")
    public ResponseEntity<List<ProxyServer>> getProxyServersList() {
        List<ProxyServer> proxyServers = proxyParserService.getProxies();
        ResponseEntity<List<ProxyServer>> jsonSuccess = new ResponseEntity<>(proxyServers, HttpStatus.OK);
        return jsonSuccess;
    }
}
