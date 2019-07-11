package ua.config;

import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScans(value = { @ComponentScan("ua.service"), @ComponentScan("ua") })
@EnableJpaRepositories("ua.repository")
public class AppConfig {

    public final static String SESSION_ATTRIBUTE_LEGAL_ENTITY = "currentLegalEntity";
    public final static String DEFAULT_THEME_CONFIGURATION_NAME = "theme.default";
}
