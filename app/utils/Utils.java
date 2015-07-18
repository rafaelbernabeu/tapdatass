package utils;

import models.Tag;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by rafael on 16/06/15.
 */
public class Utils {

    public static String getMD5(String filePath) {
        try {
            Path path = Paths.get(filePath);
            FileInputStream fileInputStream = new FileInputStream(path.toFile());
            String hash = org.apache.commons.codec.digest.DigestUtils.md5Hex(fileInputStream);
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
                if (extension.contains(".jpg") || extension.contains(".png") || extension.contains(".gif") || extension.contains(".bmp")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String idsFromTagList(List<Tag> tags) {
        StringBuilder sb = new StringBuilder();
        for (Tag tag : tags){
            sb.append(tag.getId());
            sb.append(",");
        }
        return sb.toString().substring(0, sb.length()-1);
    }
}
