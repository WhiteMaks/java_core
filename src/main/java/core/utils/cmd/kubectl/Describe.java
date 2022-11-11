package core.utils.cmd.kubectl;

import core.utils.cmd.Cmd;

public class Describe {
    private final String entrypoint;

    private final Cmd cmd;

    private Pods pods;

    public Describe(String entrypoint, Cmd cmd) {
        this.entrypoint = entrypoint.concat("describe")
                .concat(" ");

        this.cmd = cmd;
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
