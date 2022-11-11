package core.utils;

import core.CoreFactory;
import core.enums.OSType;
import core.supports.CustomLogger;
import core.utils.cmd.Cmd;

public class OperationSystem {
    private final static CustomLogger logger = CoreFactory.getInstance().createLogger(OperationSystem.class);

    private final OSType type;
    private final Cmd cmd;

    public OperationSystem() {
        type = defineOSType();

        logger.debug("Операционная система идентифицирована как [ " + type + " ]");

        cmd = new Cmd();
    }

    public OSType getType() {
        return type;
    }

    public Cmd getCmd() {
        return cmd;
    }

    private OSType defineOSType() {
        var osName = System.getProperty("os.name")
                .toLowerCase();

        logger.trace("Найден параметр для os.name = [ " + osName + " ]");

        if (osName.contains("win")) {
            return OSType.WINDOWS;
        }

        if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            return OSType.LINUX;
        }

        if (osName.contains("mac")) {
            return OSType.MAC_OS;
        }

        throw new RuntimeException("Невозможно определить операционную систему! os.name = " + osName);
    }
}
