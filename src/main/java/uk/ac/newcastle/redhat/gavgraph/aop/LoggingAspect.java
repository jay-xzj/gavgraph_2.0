package uk.ac.newcastle.redhat.gavgraph.aop;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.env.Environment;

@Aspect
public class LoggingAspect {

    private final Environment env;

    public LoggingAspect(Environment env) {
        this.env = env;
    }
}
