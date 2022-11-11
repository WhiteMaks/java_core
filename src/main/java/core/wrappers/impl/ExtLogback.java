package core.wrappers.impl;

import core.wrappers.Logger;
import org.slf4j.LoggerFactory;

public class ExtLogback implements Logger {
    private final org.slf4j.Logger logger;

    public ExtLogback(Class<?> clazz) {
        logger = LoggerFactory.getLogger(clazz);
    }

    @Override
    public void trace(String message) {
        logger.trace(message);
    }

    @Override
    public void trace(String message, Throwable throwable) {
        logger.trace(
                message,
                throwable
        );
    }

    @Override
    public void debug(String message) {
        logger.debug(message);
    }

    @Override
    public void debug(String message, Throwable throwable) {
        logger.debug(
                message,
                throwable
        );
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void info(String message, Throwable throwable) {
        logger.info(
                message,
                throwable
        );
    }

    @Override
    public void warn(String message) {
        logger.warn(message);
    }

    @Override
    public void warn(String message, Throwable throwable) {
        logger.warn(
                message,
                throwable
        );
    }

    @Override
    public void error(String message) {
        logger.error(message);
    }

    @Override
    public void error(String message, Throwable throwable) {
        logger.error(
                message,
                throwable
        );
    }

}
