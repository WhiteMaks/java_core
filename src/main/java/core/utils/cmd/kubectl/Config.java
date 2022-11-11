package core.utils.cmd.kubectl;

import core.utils.cmd.Cmd;
import core.utils.cmd.kubectl.exceptions.KubectlException;
import core.utils.cmd.kubectl.models.Context;

import java.util.ArrayList;
import java.util.List;

public class Config {
    private final String entrypoint;

    private final Cmd cmd;

    public Config(String entrypoint, Cmd cmd) {
        this.entrypoint = entrypoint.concat("config")
                .concat(" ");

        this.cmd = cmd;
    }

    public List<Context> getContexts() {
        var result = new ArrayList<Context>();

        var command = entrypoint.concat("get-contexts");

        var execute = cmd.execute(command);

        var rows = execute.getSuccess()
                .split("\n");

        if (execute.isSuccess() && rows.length > 0) {
            for (int i = 1; i < rows.length; i++) {
                var columns = rows[i].replaceAll(
                        "\\s+",
                                " "
                        ).split(" ");

                result.add(generateContext(columns));
            }
            return result;
        }

        throw new KubectlException(
                command,
                execute.getError()
        );
    }

    public boolean useContext(Context context) {
        return useContext(context.getName());
    }

    public boolean useContext(String contextName) {
        var command = entrypoint.concat("use-context")
                .concat(" ")
                .concat(contextName);

        var execute = cmd.execute(command);

        return execute.isSuccess()
                && execute.getSuccess().startsWith("Switched to context \"" + contextName + "\"");
    }

    private Context generateContext(String[] contextArrayData) {
        var result = new Context();

        result.setCurrent(!contextArrayData[0].isEmpty());
        result.setName(contextArrayData[1]);
        result.setCluster(contextArrayData[2]);
        result.setAuthInfo(contextArrayData[3]);

        return result;
    }
}
