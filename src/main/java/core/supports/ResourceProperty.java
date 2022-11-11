package core.supports;

import core.CoreFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Объект класса {@code ResourceProperty} упрощает работу со свойствами
 *
 * @author WhiteMaks
 */
public abstract class ResourceProperty {
    private final static CustomLogger logger = CoreFactory.getInstance().createLogger(ResourceProperty.class);

    private final Properties properties;

    public ResourceProperty(String propertyName) {
        properties = initProperties(propertyName);
    }

    /**
     *
     * @param propertyName имя файла с параметрами, который находиться в папке properties
     * @return загруженный объект класса {@code Properties} параметрами, которые есть в файле для дальнейшего использования
     */
    private Properties initProperties(String propertyName) {
        Properties properties = new Properties();
        InputStream fileInputStream;
        try {
            fileInputStream = getClass().getClassLoader().getResourceAsStream("properties/".concat(propertyName));
            properties.load(fileInputStream);
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            logger.info("Файл [ " + propertyName + " ] содержащий свойства успешно проинициализирован. Найдено [ " + properties.size() + " ] параметров");
        } catch (IOException ex) {
            logger.warn("Произошла ошибка при чтении файла [ " + propertyName + " ] со свойствами", ex);
        }
        return properties;
    }

    protected String getProperty(String key) {
        String property = properties.getProperty(key);
        logger.debug("Найдено значение [ " + property + " ] у параметра [ " + key + " ]");
        return property;
    }
}
