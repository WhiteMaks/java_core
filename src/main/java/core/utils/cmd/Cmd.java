package core.utils.cmd;

import core.CoreFactory;
import core.enums.CmdStatus;
import core.supports.CustomLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Cmd {
    private final static CustomLogger logger = CoreFactory.getInstance().createLogger(Cmd.class);

    private final Runtime runtime;

    public Cmd() {
        runtime = Runtime.getRuntime();
    }

    public Output execute(String command) {
        logger.trace("Команда на выполнение [ " + command + " ]");

        Process process;
        try {
            process = runtime.exec(command);
        } catch (IOException ex) {
            return new Output(
                    CmdStatus.ERROR,
                    null,
                    ex.getMessage()
            );
        }

        String successMessage;
        try {
            successMessage = readBuffer(
                    new BufferedReader(
                            new InputStreamReader(process.getInputStream())
                    )
            );
        } catch (IOException ex) {
            return new Output(
                    CmdStatus.ERROR,
                    null,
                    ex.getMessage()
            );
        }

        String errorMessage;
        try {
            errorMessage = readBuffer(
                    new BufferedReader(
                            new InputStreamReader(process.getErrorStream())
                    )
            );
        } catch (IOException ex) {
            return new Output(
                    CmdStatus.ERROR,
                    successMessage,
                    ex.getMessage()
            );
        }

        return new Output(
                CmdStatus.SUCCESS,
                successMessage,
                errorMessage
        );
    }

    private String readBuffer(BufferedReader bufferedReader) throws IOException {
        var result = new StringBuilder();

        String message;
        while ((message = bufferedReader.readLine()) != null) {
            result.append(message)
                    .append("\n");
        }

        return result.toString();
    }
}

