package core.utils.cmd.kubectl;

import core.utils.cmd.Cmd;
import core.utils.cmd.kubectl.exceptions.KubectlException;
import core.utils.cmd.kubectl.models.Namespace;

import java.util.ArrayList;
import java.util.List;

public class Get {
    private final String entrypoint;

    private final Cmd cmd;

    private Pods pods;

    public Get(String entrypoint, Cmd cmd) {
        this.entrypoint = entrypoint.concat("get")
                .concat(" ");

        this.cmd = cmd;
    }

    public List<Namespace> namespace() {
        var result = new ArrayList<Namespace>();

        var command = entrypoint.concat("namespace");

        var execute = cmd.execute(command);

        var rows = execute.getSuccess()
                .split("\n");

        if (execute.isSuccess() && rows.length > 0) {
            for (int i = 1; i < rows.length; i++) {
                var columns = rows[i].replaceAll(
                        "\\s+",
                        " "
                ).split(" ");

                result.add(generateNamespace(columns));
            }
            return result;
        }

        throw new KubectlException(
                command,
                execute.getError()
        );
    }

    private Namespace generateNamespace(String[] namespaceArrayData) {
        var result = new Namespace();

        result.setName(namespaceArrayData[0]);
        result.setStatus(Namespace.Status.valueOf(namespaceArrayData[1].toUpperCase()));
        result.setAge(namespaceArrayData[2]);

        return result;
    }

    public Pods pods() {
        if (pods == null) {
            pods = new Pods(
                    entrypoint,
                    cmd
            );
        }
        return pods;
    }
}
