package core.utils;

import core.CoreFactory;
import core.supports.CustomLogger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

public class ZIPFile {
    private final static CustomLogger logger = CoreFactory.getInstance().createLogger(ZIPFile.class);

    private final ZipInputStream zipInputStream;

    public ZIPFile(InputStream inputStream) {
        zipInputStream = new ZipInputStream(inputStream);
    }

    public void unpackFile(String pathFile) {
        try {
            logger.debug("Начат процесс распаковки файла из архива в директорию [ " + pathFile + " ]");

            new File(pathFile)
                    .delete();

            var entry = zipInputStream.getNextEntry();

            if (entry != null) {
                var fileOutputStream = new FileOutputStream(pathFile);
                var fileName = entry.getName();
                var buffer = new byte[8192];
                var fileSize = entry.getSize();
                var lastPersint = 0;
                var size = 0;
                int len;
                while ((len = zipInputStream.read(buffer)) != -1) {
                    fileOutputStream.write(
                            buffer,
                            0,
                            len
                    );

                    size = size + len;

                    var percent = (double) size/fileSize * 100;

                    var countPercent = (int) percent;

                    if (lastPersint < countPercent) {
                        lastPersint = countPercent;
                        if (lastPersint % 10 == 0) {
                            logger.trace("Распаковка файла " + fileName + " [ " + lastPersint +  " % ]");
                        }
                    }
                }
                fileOutputStream.flush();
                zipInputStream.closeEntry();
                fileOutputStream.close();
            } else {
                logger.warn("В архиве не найдено ни одного файла!");
            }
            logger.debug("Процесс распаковки завершен");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
