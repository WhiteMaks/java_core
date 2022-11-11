package core.utils.cmd.kubectl.models;

public class Pod {
    private String name;
    private String ready;
    private Status status;
    private String age;

    private int restarts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReady() {
        return ready;
    }

    public void setReady(String ready) {
        this.ready = ready;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getRestarts() {
        return restarts;
    }

    public void setRestarts(int restarts) {
        this.restarts = restarts;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public enum Status {
        COMPLETED,
        RUNNING,
        TERMINATING
    }
}
