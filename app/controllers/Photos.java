package controllers;

import models.Photo;
import models.PhotoTag;
import models.Tag;
import play.data.validation.Required;
import play.db.jpa.Blob;
import play.mvc.Controller;
import utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafael bernabeu on 17/07/15.
 */
public class Photos extends Controller {

    public static void delete(long id) {
        Photo p = Photo.findById(id);
        p.delete();
        Application.gallery("newest");
    }

    public static void addPhoto(Photo photo, @Required String[] tags) { //receber objeto TAG ao invez de string[]
        checkAuthenticity();
        if (validation.hasErrors()) {
            flash.error("Selecione uma TAG!");
            Application.uploadFile();
        }
        photo.save();
        String ids = "";
        if (tags != null) {
            for (String t : tags) {
                ids += t + ", ";
            }
            ids = ids.substring(0, ids.length() - 2); //SQL INJECTION!!!!
            List<Tag> tagList = Tag.em().createQuery("SELECT t FROM Tag t WHERE id IN (" + ids + ")").getResultList();
            for (Tag tag : tagList) {
                new PhotoTag(photo, tag).save();
            }
        }
        //photo.save();
        Application.gallery("newest");
    }

    public static void addFolder(File[] files) {
        List<String> photosMd5New = new ArrayList<>();
        List<String> photosMd5BD = Photo.em().createNativeQuery("SELECT md5 FROM photo").getResultList();
        for (File file : files) {
            if (Files.isRegularFile(Paths.get(file.getAbsolutePath())) && FileUtils.isImage(file.getName())) {
                try {
                    String md5 = FileUtils.getMD5(file);
                    //System.out.println(md5);
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
                    if (cont == -10) {
                        Blob blob = new Blob();
                        blob.set(new FileInputStream(file), "image");
                        Photo p = new Photo();
                        p.setPhotoBytes(blob);
                        p.setMd5(md5);
                        p.save();
                    }
                    cont = 0;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.err.println("Not a regular file!" + file.getAbsolutePath());
            }
        }
        renderText(files.length);
    }

    public static void addWin(String photoId) {
        Photo photo = Photo.findById(Long.parseLong(photoId));
        photo.setWins(photo.getWins() + 1);
        photo.save();
        renderText(photo.getWins());
    }

    public static void addLose(String photoId) {
        Photo photo = Photo.findById(Long.parseLong(photoId));
        photo.setLoses(photo.getLoses() + 1);
        photo.save();
        renderText(photo.getLoses());
    }
}