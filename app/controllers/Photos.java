package controllers;

import models.Photo;
import models.PhotoTag;
import models.Tag;
import play.data.validation.Required;
import play.mvc.Controller;
import utils.Utils;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafael on 17/07/15.
 */
public class Photos extends Controller {

    public static void delete(long id) {
        Photo p = Photo.findById(id);
        p.delete();
        Application.gallery("newest");
    }

    public static void addPhoto(Photo photo, @Required String[] tags) { //receber objeto TAG ao invez de string[]
        checkAuthenticity();
        if(validation.hasErrors()) {
            flash.error("Selecione uma TAG!");
            Application.upload();
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
        photo.save();
        Application.gallery("newest");
    }

    public static void addWin(String photoId){
        Photo photo = Photo.findById(Long.parseLong(photoId));
        photo.setWins(photo.getWins() + 1);
        photo.save();
        renderText(photo.getWins());
    }

    public static void addLose(String photoId){
        Photo photo = Photo.findById(Long.parseLong(photoId));
        photo.setLoses(photo.getLoses() + 1);
        photo.save();
        renderText(photo.getLoses());
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
        Application.gallery("all");
    }
}