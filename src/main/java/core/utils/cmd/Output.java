package core.utils.cmd;

import core.CoreFactory;
import core.enums.CmdStatus;
import core.supports.CustomLogger;

public class Output {
    private final static CustomLogger loger = CoreFactory.getInstance().createLogger(Output.class);

    private final CmdStatus status;
    private final String success;
    private final String error;

    public Output(CmdStatus status) {
        this(status, null, null);
    }

    public Output(CmdStatus status, String success, String error) {
        this.status = status;
        this.error = error;
        this.success = success;

        loger.trace("Статус выполнения команды [ " + status + " ]");

        if (status == CmdStatus.SUCCESS) {
            loger.trace("Сообщение [ " + success.replaceAll("\n", "=>").replaceAll("\\s+", " ") + " ]");
        } else {
            loger.warn("Ошибка [ " + error.replaceAll("\n", "=>").replaceAll("\\s+", " ") + " ]");
        }
    }

    public String getSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }

    public boolean isSuccess() {
        return status == CmdStatus.SUCCESS;
    }
}
