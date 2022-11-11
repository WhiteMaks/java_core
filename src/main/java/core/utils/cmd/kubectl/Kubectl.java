package core.utils.cmd.kubectl;

import core.utils.cmd.Cmd;

public class Kubectl {
    private final String entrypoint;

    private final Cmd cmd;

    private Describe describe;
    private Config config;
    private Get get;

    public Kubectl(Cmd cmd) {
        this.cmd = cmd;

        entrypoint = "kubectl ";
    }

    public Config config() {
        if (config == null) {
            config = new Config(
                    entrypoint,
                    cmd
            );
        }
        return config;
    }

    public Get get() {
        if (get == null) {
            get = new Get(
                    entrypoint,
                    cmd
            );
        }
        return get;
    }

    public Describe describe() {
        if (describe == null) {
            describe = new Describe(
                    entrypoint,
                    cmd
            );
        }
        return describe;
    }
}
