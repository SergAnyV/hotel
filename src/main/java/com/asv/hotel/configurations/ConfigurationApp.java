package com.asv.hotel.configurations;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class ConfigurationApp {

    @Bean(value = "getMailSession")
    public Session getMailSession(@Value("${mail.smtp.host}") String host,
                                  @Value("${mail.smtp.port}") int port,
                                  @Value("${mail.smtp.auth}") boolean auth,
                                  @Value("${mail.smtp.starttls.enable}") boolean starttls,
                                  @Value("${mail.username}") String username,
                                  @Value("${mail.password}") String password){
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", auth);
        properties.put("mail.smtp.starttls.enable", starttls);
        properties.put("mail.smtp.ssl.trust", host);
return Session.getInstance(properties, new Authenticator() {
    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }
});
    }
}
