package com.accounts.services.accountservice;

import com.accounts.dao.PersonDAO;
import com.accounts.dao.ProxyServerDAO;
import com.accounts.models.Person;
import com.accounts.models.ProxyServer;
import com.accounts.models.Sex;
import com.accounts.services.dataservice.RandomData;
import com.accounts.services.proxyservice.ProxyParserService;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Scope("prototype")
public class CreateAccountServiceImpl implements CreateAccountService {
    private WebDriver driver;
    private List<WebElement> inputsToFill;
    private ChromeOptions options;
    private Proxy proxy;
    private ProxyServer proxyServer;
    private LogEntries logs;

    @Autowired
    private RandomData randomData;
    @Autowired
    private PersonDAO personDAO;
    @Autowired
    private ProxyServerDAO proxyServerDAO;
    @Autowired
    ProxyParserService proxyParserService;
    @Autowired
    GetNumberService getNumberService;

    @Value("${web.driver.path}")
    private String webDriverPath;
    @Value("${google.identifying.class.inputs}")
    private String googleIdentifyingClassInputs;
    @Value("${google.identifying.class.next.button.}")
    private String googleIdentifyingClassNextButton;
    @Value("${url.to.connect}")
    private String urlToConnect;

    public Person getPersonData() {
        installConnection();
        Person personToSend = putDataIntoFieldsAndSaveData();


        return personToSend;
    }

    public Person putDataIntoFieldsAndSaveData() {
        Sex sex = randomData.getState();
        String firstName = randomData.getFirstName(sex);
        String lastName = randomData.getLastName(sex);
        String email = randomData.getEmail(firstName, lastName);
        String password = randomData.getPassword();

        writeAllData(firstName, lastName, email, password);
        int i = 0;
        clickNextButton();

        while (isNameTaken() || isNameTooShort()) {
            if (isNameTooShort()) {
                lastName = randomData.getLastName(sex);
                email = randomData.getEmail(firstName, lastName);
                writeLastName(lastName);
                writeEmail(email);
            }
            if (isNameTaken()) {
                email = randomData.getEmail(firstName, lastName);
                writeEmail(email);
            }
            clickNextButton();
        }
        String emailGmail = email + "@gmail.com";
        Person personToSend = new Person(firstName, lastName, emailGmail, password, sex);
        personDAO.save(personToSend);
        return personToSend;
    }

    public void installConnection() {
        setUpConnection();
        setNewProxy();

        driver = new ChromeDriver(options);
        driver.get(urlToConnect);
        logs = driver.manage().logs().get("browser");

        while (!isProxyServerWorking()) {
            driver.close();
            proxyServerDAO.delete(proxyServer);
            setNewProxy();
            driver = new ChromeDriver(options);
            driver.get(urlToConnect);
            logs = driver.manage().logs().get("browser");
        }

        getInputsToFill();
        int i = 0;
    }

    public void setUpConnection() {
        System.setProperty("webdriver.chrome.driver", webDriverPath);
        options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--popup-blocking");
        options.addArguments("--disable-infobars");
    }

    public void setNewProxy() {
        proxy = new Proxy();
        proxyServer = proxyParserService.getRandomProxy();
        proxy.setHttpProxy(proxyServer.getAddress());
        proxy.setSslProxy(proxyServer.getAddress());
        options.setProxy(proxy);
    }

    public boolean isProxyServerWorking() {
        List<WebElement> werErrorElements = new ArrayList<>();
        werErrorElements = driver.findElements(By.id("main-frame-error"));
        boolean isWorking = false;
        if (werErrorElements.size() == 0 && isPageDownloaded() && noProxyAuthentication())
            isWorking = true;
        return isWorking;
    }

    public boolean noProxyAuthentication() {
        boolean noAuthentication = true;
        try {
            WebElement emptyHtml = driver.findElement(By.tagName("html"));
            String emptyHtmlText = emptyHtml.getText();
            if (emptyHtmlText.equals(""))
                noAuthentication = false;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("The request to login is absent");
        }
        return noAuthentication;
    }

    public boolean isPageDownloaded() {
        List<LogEntry> logEntries = logs.getAll();
        boolean noError = true;
        for (LogEntry logEntry : logEntries) {
            String message = logEntry.getMessage();
            if (message.contains("Failed to load resource")) {
                noError = false;
            }
        }
        return noError;
    }


    public boolean isNameTaken() {
        Optional<WebElement> webElement = Optional.empty();
        try {
            WebElement nameIsTaken = driver.findElement(By.xpath("//*[text()='That username is taken. Try another.']"));
            webElement = Optional.ofNullable(nameIsTaken);
        } catch (Exception e) {
            System.out.println("Элемент: \"That username is taken. Try another.\" - не найдет");
        }
        boolean nameIsTaken = webElement.isPresent();
        return nameIsTaken;
    }

    public boolean isNameTooShort() {
        Optional<WebElement> webElement = Optional.empty();
        try {
            WebElement nameIsTooShort = driver.findElement(By.xpath("//*[text()='Sorry, your username must be between 6 and 30 characters long.']"));
            webElement = Optional.ofNullable(nameIsTooShort);
        } catch (Exception e) {
            System.out.println("Элемент: \"Sorry, your username must be between 6 and 30 characters long.\" - не найдет");
        }
        boolean nameIsTooShort = webElement.isPresent();
        return nameIsTooShort;
    }

    public void writeFirstName(String firstName) {
        inputsToFill.get(0).clear();
        inputsToFill.get(0).sendKeys(firstName);
    }

    public void writeLastName(String lastName) {
        inputsToFill.get(1).clear();
        inputsToFill.get(1).sendKeys(lastName);
    }

    public void writeEmail(String email) {
        inputsToFill.get(2).clear();
        inputsToFill.get(2).sendKeys(email);
    }

    public void writePassword(String password) {
        inputsToFill.get(3).clear();
        inputsToFill.get(3).sendKeys(password);
        inputsToFill.get(4).clear();
        inputsToFill.get(4).sendKeys(password);
    }

    public void writeAllData(String firstName, String lastName, String email, String password) {
        writeFirstName(firstName);
        writeLastName(lastName);
        writeEmail(email);
        writePassword(password);
    }

    public void clickNextButton() {
        WebElement nextButton = driver.findElement(By.className(googleIdentifyingClassNextButton));
        nextButton.click();
    }

    public void getInputsToFill() {
        inputsToFill = driver.findElements(By.className(googleIdentifyingClassInputs));
    }

}
