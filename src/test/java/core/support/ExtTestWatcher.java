package core.support;

import core.CoreFactory;
import core.supports.CustomLogger;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.util.Optional;

public class ExtTestWatcher implements TestWatcher {
    private final static CustomLogger logger = CoreFactory.getInstance().createLogger(ExtTestWatcher.class);

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {

    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        logger.info("[SUCCESSFUL]" + " " + context.getDisplayName());
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {

    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        logger.error(
                "[FAILED]" + " " + context.getDisplayName(),
                cause
        );
    }
}
