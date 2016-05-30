package controllers;

import models.Photo;
import models.Tag;
import play.data.validation.Required;
import play.db.jpa.Blob;
import play.mvc.Controller;
import utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        List<String> md5List = Photo.em().createNativeQuery("SELECT md5 FROM photo").getResultList();
        String md5NewFile = FileUtils.getMD5(photo.getBlob().getFile());
        boolean exists = false;
        if (md5List.size() > 0) {
            for (String md5BD : md5List) {
                if (md5BD.equals(md5NewFile)) {
                    exists = true;
                }
            }
        }
        if (!exists) {
            String ids = "";
            if (tags != null) {
                for (String t : tags) {
                    ids += t + ", ";
                }
                ids = ids.substring(0, ids.length() - 2); //SQL INJECTION!!!!
                Set<Tag> tagList = new HashSet<>(Tag.em().createQuery("SELECT t FROM Tag t WHERE id IN (" + ids + ")").getResultList());
                photo.setTags(tagList);
            }
            photo.setMd5(md5NewFile);
            photo.save();
        }
        Application.gallery("newest");
    }

    public static void addFolder(File[] files) {
        checkAuthenticity();
        List<String> md5List = Photo.em().createNativeQuery("SELECT md5 FROM photo").getResultList();
        Tag tag = new Tag("UploadFolder");
        tag.save();
        for (File file : files) {
            if (Files.isRegularFile(Paths.get(file.getAbsolutePath())) && FileUtils.isImage(file.getName())) {
                try {
                    String md5NewFile = FileUtils.getMD5(file);
                    System.out.println(md5NewFile);
                    boolean exists = false;
                    if (md5List.size() > 0) {
                        for (String md5BD : md5List) {
                            if (md5BD.equals(md5NewFile)) {
                                exists = true;
                            }
                        }
                    }
                    if (!exists) {
                        Blob blob = new Blob();
                        blob.set(new FileInputStream(file), "image");
                        Photo p = new Photo();
                        p.setBlob(blob);
                        p.setMd5(md5NewFile);
                        p.save();
                        Set<Tag> tags = new HashSet<>();
                        tags.add(tag);
                        p.setTags(tags);
                        p.save();
                    }
                    md5List.add(md5NewFile);
                    exists = false;
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