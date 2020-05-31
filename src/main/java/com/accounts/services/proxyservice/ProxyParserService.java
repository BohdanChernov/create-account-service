package com.accounts.services.proxyservice;

import com.accounts.models.ProxyServer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Scope("prototype")
public interface ProxyParserService {
    List<ProxyServer> getProxies();
    ProxyServer getRandomProxy();
}
