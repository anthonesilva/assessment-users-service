package com.assesment.users_service.infra.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.stereotype.Component;

import com.assesment.users_service.domain.ports.out.LoggerPort;

@Component
public class LoggerAdapter implements LoggerPort {

    private static final Logger logger = LoggerFactory.getLogger(LoggerAdapter.class);

    @Override
    public void log(String level, String message) {
        logger.atLevel(Level.valueOf(level)).log(message);
    }

}
