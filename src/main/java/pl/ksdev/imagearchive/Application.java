package pl.ksdev.imagearchive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static pl.ksdev.imagearchive.config.AppProfile.*;

@SpringBootApplication
@Slf4j
public class Application {

    private final Environment env;

    public Application(Environment env) {
        this.env = env;
    }

    @PostConstruct
    void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.containsAll(Arrays.asList(PRODUCTION, DEVELOPMENT))
            || Collections.disjoint(activeProfiles, Arrays.asList(PRODUCTION, DEVELOPMENT, TESTING))
        ) {
            log.error(String.format("Invalid environment (should be '%s' or '%s')", PRODUCTION, TESTING));
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @EventListener
    public void displayGreeting(ApplicationReadyEvent event) throws UnknownHostException {
        ConfigurableApplicationContext context = event.getApplicationContext();

        ConfigurableEnvironment env = context.getEnvironment();

        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) protocol = "https";

        log.info(
            "\n---------------------------------------------------------\n\t"
                + "Application is running! Access URLs:\n\t"
                + "Local: \t\t{}://localhost:{}\n\t"
                + "External: \t{}://{}:{}\n\t"
                + "Profile: \t{}\n"
                + "---------------------------------------------------------",
            protocol,
            env.getProperty("server.port"),
            protocol,
            InetAddress.getLocalHost().getHostAddress(),
            env.getProperty("server.port"),
            env.getActiveProfiles()
        );
    }
}

