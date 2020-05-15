package uk.ac.newcastle.redhat.gavgraph;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.neo4j.ogm.config.ClasspathConfigurationSource;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import uk.ac.newcastle.redhat.gavgraph.aop.LoggingAspect;
import uk.ac.newcastle.redhat.gavgraph.common.GavGraphConstants;

import java.io.IOException;

@Configuration
@EnableAspectJAutoProxy
@EnableTransactionManagement
@EnableNeo4jRepositories(basePackages= GavGraphConstants.BASE_PACKAGE)
@SpringBootApplication(scanBasePackages = GavGraphConstants.BASE_PACKAGE)
public class GavgraphApplication {
    private static Logger logger = LogManager.getLogger(GavgraphApplication.class.getName());
    private static final Marker SERVER_START_MARKER = MarkerManager.getMarker("SERVER_START");


    public static void main(String[] args) {
        logger.info(SERVER_START_MARKER,"===========GAVGraphApplication is starting!===========");
        SpringApplication.run(GavgraphApplication.class, args);
        logger.info(SERVER_START_MARKER,"=============GAVGraphApplication started!=============");
    }

    @Value("${server.port}")
    private String port;

    @EventListener({ApplicationReadyEvent.class})
    public void ready() {
        System.out.println("Application is almost started ... opening the browser");
        //Homepage URL link here
        String url = "http://localhost:"+port+ GavGraphConstants.SLASH+"swagger-ui.html";
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Value("${spring.data.neo4j.uri}")
    private String databaseUrl;

    @Value("${spring.data.neo4j.username}")
    private String userName;

    @Value("${spring.data.neo4j.password}")
    private String password;

    /******Session Factory*******/
    @Bean
    public SessionFactory sessionFactory() {
        org.neo4j.ogm.config.Configuration configuration = new org.neo4j.ogm.config.Configuration.Builder()
                .uri(databaseUrl)
                .build();
        return new SessionFactory(configuration, GavGraphConstants.BASE_PACKAGE);
    }

    @Bean
    public Neo4jTransactionManager transactionManager() {
        return new Neo4jTransactionManager(sessionFactory());
    }

    /*********AspectJConfig*********/
    @Bean
    @Profile(GavGraphConstants.SPRING_PROFILE_DEVELOPMENT)
    public LoggingAspect loggingAspect(Environment env) {
        return new LoggingAspect(env);
    }


}
