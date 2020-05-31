package com.accounts.dao;

import com.accounts.models.ProxyServer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelephoneNumberDAO extends JpaRepository<ProxyServer, Long> {
}
