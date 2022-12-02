# Java Core

## _Setup maven project_

Add dependency to pom.xml.

```xml
<repositories>
    <repository>
        <id>java_core-mvn-repo</id>
        <url>https://raw.github.com/WhiteMaks/java_core/mvn-repo/</url>
        <snapshots>
            <enabled>true</enabled>
            <updatePolicy>always</updatePolicy>
        </snapshots>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>ru.whitemaks</groupId>
        <artifactId>java_core</artifactId>
        <version>1.0.2</version>
    </dependency>
</dependencies>
```
