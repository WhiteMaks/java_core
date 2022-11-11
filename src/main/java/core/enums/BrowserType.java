package core.enums;

public enum BrowserType {
    CHROME("chromedriver");

    private final String driverName;

    BrowserType(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverName() {
        return driverName;
    }
}
