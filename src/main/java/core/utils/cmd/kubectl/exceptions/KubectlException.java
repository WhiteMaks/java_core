package core.utils.cmd.kubectl.exceptions;

public class KubectlException extends RuntimeException {

    public KubectlException(String command, String error) {
        super("Error execute command [ " + command + " ]. Description: " + error);
    }
}
