package ua.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;
import java.util.TimeZone;

import static org.hibernate.cfg.AvailableSettings.*;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class DatabaseConfig {

    private static final String ENTITY_MANAGER_PACKAGES_TO_SCAN = "ua.model";

    private static Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    private Environment environment;

    @Autowired
    public void setEnvironment(Environment environment){
        this.environment = environment;
    }

    @Bean
    public DriverManagerDataSource targetDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(environment.getRequiredProperty("db.driver"));
        dataSource.setUrl(environment.getRequiredProperty("db.url"));
        dataSource.setUsername(environment.getRequiredProperty("db.username"));
        dataSource.setPassword(environment.getRequiredProperty("db.password"));

        return dataSource;
    }

//    @Bean(destroyMethod = "close")
//    public ComboPooledDataSource targetDataSource(){
//        ComboPooledDataSource dataSource = new ComboPooledDataSource();
//        String driver = environment.getRequiredProperty("db.driver");
//        try {
//            dataSource.setDriverClass(driver);
//        } catch (PropertyVetoException pve){
//            logger.debug("Cannot load datasource driver (" + driver +") : " + pve.getMessage());
//            return null;
//        }
//        dataSource.setJdbcUrl(environment.getRequiredProperty("db.url"));
//        dataSource.setUser(environment.getRequiredProperty("db.username"));
//        dataSource.setPassword(environment.getRequiredProperty("db.password"));
//
//        //dataSource.setPreferredTestQuery( "SELECT 1" );
//        dataSource.setMinPoolSize(Integer.parseInt(environment.getRequiredProperty("c3p0.minPoolSize")));
//        dataSource.setMaxPoolSize(Integer.parseInt(environment.getRequiredProperty("c3p0.maxPoolSize")));
//        dataSource.setMaxStatements(Integer.parseInt(environment.getRequiredProperty("c3p0.maxStatements")));
//        dataSource.setMaxStatementsPerConnection(Integer.parseInt(environment.getRequiredProperty("c3p0.maxStatementsPerConnection")));
//        dataSource.setIdleConnectionTestPeriod(Integer.parseInt(environment.getRequiredProperty("c3p0.idleConnectionTestPeriod")));
//        dataSource.setCheckoutTimeout(Integer.parseInt(environment.getRequiredProperty("c3p0.checkoutTimeout")));
//        dataSource.setTestConnectionOnCheckin(Boolean.parseBoolean(environment.getRequiredProperty("c3p0.testConnectionOnCheckin")));
//        return dataSource;
//    }

    @Bean
    public DataSource dataSource(){
        LazyConnectionDataSourceProxy dataSourceProxy = new LazyConnectionDataSourceProxy();
        dataSourceProxy.setTargetDataSource(targetDataSource());
        return dataSourceProxy;
    }

    private Properties properties(){
        Properties properties = new Properties();
        properties.put(DIALECT, environment.getRequiredProperty("hibernate.dialect"));
        properties.put(SHOW_SQL, environment.getRequiredProperty("hibernate.show_sql"));
        return properties;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource());
        entityManagerFactory.setPackagesToScan(ENTITY_MANAGER_PACKAGES_TO_SCAN);
        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactory.setJpaProperties(properties());
        return entityManagerFactory;
    }

    @Bean
    public JpaTransactionManager transactionManager(){
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return jpaTransactionManager;
    }

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
