package com.accounts.services.accountservice;

import com.accounts.dao.TelephoneNumberDAO;
import com.accounts.models.TelephoneNumber;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Scope("prototype")
public final class GetNumberServiceImpl implements GetNumberService{
    @Value("${url.to.get.numbers}")
    private String urlToConnect;
    @Value("${web.driver.path}")
    private String webDriverPath;
    @Autowired
    private TelephoneNumberDAO telephoneNumberDAO;
    private ChromeOptions options;
    private WebDriver driver;

    @Override
    public List<TelephoneNumber> getNumbers() {
        setUpConnection();
        return null;
    }

    public void parseNumbersToDataBase() {
//        List<WebElement> numbers = driver.findElements()
    }

    public void setUpConnection() {
        System.setProperty("webdriver.chrome.driver", webDriverPath);
        options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--popup-blocking");
        options.addArguments("--disable-infobars");
        driver = new ChromeDriver(options);
        driver.get(urlToConnect);
    }
}
