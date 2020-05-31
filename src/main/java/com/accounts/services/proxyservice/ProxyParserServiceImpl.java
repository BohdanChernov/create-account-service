package com.accounts.services.proxyservice;

import com.accounts.dao.ProxyServerDAO;
import com.accounts.models.ProxyServer;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Scope("prototype")
public class ProxyParserServiceImpl implements ProxyParserService {
    private WebDriver driver;

    @Autowired
    private ProxyServerDAO proxyServerDAO;
    @Value("${web.driver.path}")
    private String webDriverPath;
    @Value("${url.to.parse.proxy.connect}")
    private String urlToConnect;

    public List<ProxyServer> getProxies() {
        installConnection();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        changeSizeList();
        scrollDown();

        List<String> rawProxies = new ArrayList<>();
        driver.findElements(By.className("spy14")).forEach(webElement -> rawProxies.add(webElement.getText()));
        List<String> proxies = parseElementsToProsy(rawProxies);

        List<ProxyServer> proxyServers = new ArrayList<>();
        proxies.forEach(s -> proxyServers.add(new ProxyServer(s)));

        Iterator<ProxyServer> iterator = proxyServers.iterator();
        while (iterator.hasNext()) {
            ProxyServer proxyServer = iterator.next();
            try {
                proxyServerDAO.save(proxyServer);
            } catch (Exception e) {
                iterator.remove();
                e.printStackTrace();
            }

        }
        driver.close();
        return proxyServers;
    }

    public ProxyServer getRandomProxy() {
        List<ProxyServer> proxies = proxyServerDAO.findAll();

        if (proxies.size() < 50) {
            getProxies();
        }

        int max = proxies.size() - 1;
        int min = 0;
        int range = max - min + 1;
        int randomNumber = (int) (Math.random() * range) + min;

        return proxies.get(randomNumber);
    }

    public List<String> parseElementsToProsy(List<String> rawProxyElement) {
        List<String> proxies = new ArrayList<>();
        rawProxyElement.forEach(s -> {
            Pattern pattern = Pattern.compile("\\d+[.]\\d+[.]\\d+[.]\\d+[:]\\d+");
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                String proxy = matcher.group(0);
                proxies.add(proxy);
            }
        });
        return proxies;
    }

    public void installConnection() {
        System.setProperty("webdriver.chrome.driver", webDriverPath);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        driver = new ChromeDriver(options);
        driver.get(urlToConnect);
    }

    public void changeSizeList() {
        Select dropDownMenu = new Select(driver.findElement(By.id("xpp")));
        dropDownMenu.selectByVisibleText("500");
        dropDownMenu = new Select(driver.findElement(By.id("xpp")));
        dropDownMenu.selectByVisibleText("500");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void scrollDown() {
        JavascriptExecutor js = ((JavascriptExecutor) driver);
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
