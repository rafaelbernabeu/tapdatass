package utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by rafael on 16/06/15.
 */
public class FileUtils {

    public static String getMD5(String filePath) {
        try {
            Path path = Paths.get(filePath);
            FileInputStream fileInputStream = new FileInputStream(path.toFile());
            String hash = DigestUtils.md5Hex(fileInputStream);
            fileInputStream.close();
            return hash;
        } catch (Exception e) { e.printStackTrace(); }
        return "Erro ao recuperar HashMD5";
    }

    public static String getMD5(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            String hash = DigestUtils.md5Hex(fileInputStream);
            fileInputStream.close();
            return hash;
        } catch (Exception e) { e.printStackTrace(); }
        return "Erro ao recuperar HashMD5";
    }

    public static boolean isImage(String filePath) {
        if (filePath != null) {
            int size = filePath.length();
            if (size >= 5) {
                String extension = filePath.substring(size - 4, size);
                for (TipoImagemEnum tipo : TipoImagemEnum.values()) {
                    if (extension.contains(tipo.getExtensao())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private enum TipoImagemEnum {

        jpg(".jpg"), JPG(".JPG"), png(".png"), PNG(".PNG"), gif(".gif"), GIF(".GIF"), bmp(".bmp"), BPM(".BPM");

        private final String extensao;

        TipoImagemEnum(String extensao) {
            this.extensao = extensao;
        }

        public String getExtensao() {
            return this.extensao;
        }

    }

}
