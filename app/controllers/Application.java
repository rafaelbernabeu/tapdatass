package controllers;

import models.PhotoTag;
import models.Tag;
import models.Photo;
import play.mvc.Controller;
import utils.Utils;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Blob;
import java.util.*;

public class Application extends Controller {

    private static boolean firstTime = true;

    public static void index() {
        if (firstTime) {
            flash.success("Bem-Vindo Mlk Doido!!! Zoa não... sei teu IP >>> ");
//            firstTime = false;
        }
        render();
    }

    public static void loadPhotos(String path) {

        path = "/home/rafael/Desktop/Search Instagram - FindGram.com_files";

        List<String> photosMd5New = new ArrayList<String>();
        List<String> photosMd5BD = Photo.em().createNativeQuery("SELECT md5 FROM photo").getResultList();

        try {
            Files.walk(Paths.get(path)).forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    if (Utils.isImage(filePath.toString())) {
                        try {
                            String md5 = Utils.getMD5(filePath.toString());
                            System.out.println(md5);
                            int cont = 0;

                            if (photosMd5BD.size() > 0) {
                                for (String md5BD : photosMd5BD) {
                                    if (md5BD.equalsIgnoreCase(md5)) {
                                        cont++;
                                    }
                                }
                            } else {
                                for (String md5New : photosMd5New) {
                                    if (md5New.equalsIgnoreCase(md5)) {
                                        cont++;
                                    }
                                }
                                photosMd5New.add(md5);
                            }
                            if (cont == 0) {
                                byte[] data = Files.readAllBytes(filePath);
                                Blob blob = new SerialBlob(data);
                                play.db.jpa.Blob blob2 = new play.db.jpa.Blob();
                                blob2.set(blob.getBinaryStream(), "image");
                                Photo p = new Photo();
                                p.setPhotoBytes(blob2);
                                p.setMd5(md5);
                                p.save();
                            }
                            cont = 0;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    System.err.println("Not a regular file!");
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        Gallery.index("all");
    }
}