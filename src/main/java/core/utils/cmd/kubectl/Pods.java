package core.utils.cmd.kubectl;

import core.utils.cmd.Cmd;
import core.utils.cmd.kubectl.exceptions.KubectlException;
import core.utils.cmd.kubectl.models.Namespace;
import core.utils.cmd.kubectl.models.Pod;

import java.util.ArrayList;
import java.util.List;

public class Pods {
    private final String entrypoint;

    private final Cmd cmd;

    public Pods(String entrypoint, Cmd cmd) {
        this.entrypoint = entrypoint.concat("pods")
                .concat(" ");

        this.cmd = cmd;
    }

    public List<Pod> inNamespace(Namespace namespace) {
        var result = new ArrayList<Pod>();

        var command = entrypoint.concat("-n")
                .concat(" ")
                .concat(namespace.getName());

        var execute = cmd.execute(command);

        var rows = execute.getSuccess()
                .split("\n");

        if (execute.isSuccess() && rows.length > 0) {
            for (int i = 1; i < rows.length; i++) {
                var columns = rows[i].replaceAll(
                        "\\s+",
                        " "
                ).split(" ");

                result.add(generatePod(columns));
            }
            return result;
        }

        throw new KubectlException(
                command,
                execute.getError()
        );
    }

    public String inNamespace(Namespace namespace, Pod pod) {
        var command = entrypoint.concat("-n")
                .concat(" ")
                .concat(namespace.getName())
                .concat(" ")
                .concat(pod.getName());

        var execute = cmd.execute(command);

        if (execute.isSuccess()) {
            return execute.getSuccess();
        }

        throw new KubectlException(
                command,
                execute.getError()
        );
    }

    private Pod generatePod(String[] podArrayData) {
        var result = new Pod();

        result.setName(podArrayData[0]);
        result.setReady(podArrayData[1]);
        result.setStatus(Pod.Status.valueOf(podArrayData[2].toUpperCase()));
        result.setRestarts(Integer.parseInt(podArrayData[3]));
        result.setAge(podArrayData[4]);

        return result;
    }
}
