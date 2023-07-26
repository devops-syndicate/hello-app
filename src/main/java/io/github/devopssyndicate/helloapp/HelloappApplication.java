package io.github.devopssyndicate.helloapp;

import io.pyroscope.http.Format;
import io.pyroscope.javaagent.EventType;
import io.pyroscope.javaagent.PyroscopeAgent;
import io.pyroscope.javaagent.config.Config;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

@SpringBootApplication
public class HelloappApplication {
    private final Optional<String> pyroscopeUrl;
    private final String applicationName;

    public HelloappApplication(@Value("${PYROSCOPE_URL:#{null}}") Optional<String> pyroscopeUrl, @Value("${spring.application.name}") String applicationName) {
        this.pyroscopeUrl = pyroscopeUrl;
        this.applicationName = applicationName;
    }

    public static void main(String[] args) {
        SpringApplication.run(HelloappApplication.class, args);
    }

    @PostConstruct
    public void initPyroscope() {
        this.pyroscopeUrl.ifPresent(url -> PyroscopeAgent.start(
                new Config.Builder()
                        .setApplicationName(this.applicationName)
                        .setProfilingEvent(EventType.ITIMER)
                        .setFormat(Format.JFR)
                        .setServerAddress(url)
                        .build()
        ));
    }
}
