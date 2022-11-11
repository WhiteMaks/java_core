package core.utils.cmd.kubectl.models;

import java.util.Objects;

public class Namespace {
    private String name;
    private Status status;
    private String age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public enum Status {
        ACTIVE,
        TERMINATING
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        var namespace = (Namespace) o;

        return Objects.equals(name, namespace.name)
                && status == namespace.status
                && Objects.equals(age, namespace.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                name,
                status,
                age
        );
    }
}
