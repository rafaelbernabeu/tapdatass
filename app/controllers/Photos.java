package controllers;

import models.Photo;
import models.Tag;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import play.data.validation.Required;
import play.db.jpa.Blob;
import play.mvc.Controller;
import utils.FileUtils;
import utils.TagUtils;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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

    public static void uploadPhoto(Photo photo, @Required String[] tags) { //receber objeto TAG ao invez de string[]
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

    public static void uploadFolder(File[] files) {
        checkAuthenticity();
        //List<String> md5List = Photo.em().createNativeQuery("SELECT md5 FROM photo").getResultList();
        Tag tag = TagUtils.findOrCreateTag("FolderUpload");
        for (File file : files) {
            if (Files.isRegularFile(Paths.get(file.getAbsolutePath())) && FileUtils.isImage(file.getName())) {
                try {
                    String md5NewFile = FileUtils.getMD5(file);
                    if (FileUtils.checkIfNotExists(md5NewFile)) {
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.err.println("Not a regular file!" + file.getAbsolutePath());
            }
        }
        Application.gallery("newest");
    }

    public static void uploadWeb(String url, String switcher) {
        Tag tag = TagUtils.findOrCreateTag(url);
        List<Photo> photos = new ArrayList<>();
        List<String> photosUrl = new ArrayList<>();
        if (StringUtils.isNotBlank(url)) {
            try {
                url = url.contains("http://") ? url : "http://" + url;
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.body().getElementsByTag("img");
                for (Element e : elements) {
                    photosUrl.add(e.attr("src"));
                    if (switcher != null && switcher.equals("on")) { //on = autosave
                        InputStream is = new URL(e.attr("src")).openStream();
                        File file = new File("/tmp/web.jpg");
                        OutputStream os = new FileOutputStream(file);
                        byte[] b = new byte[2048];
                        int length;
                        while ((length = is.read(b)) != -1) {
                            os.write(b, 0, length);
                        }
                        is.close();
                        os.close();

                        Photo photo = new Photo();
                        photo.setMd5(FileUtils.getMD5(file));
                        if (FileUtils.checkIfNotExists(photo.getMd5())) {
                            Blob blob = new Blob();
                            blob.set(new FileInputStream(file), "image");
                            photo.setBlob(blob);
                            Set<Tag> tags = new HashSet<>();
                            tags.add(tag);
                            photo.setTags(tags);
                            photo.save();
                            photos.add(photo);
                        }
                    }

                }

            } catch (IOException io) {
                io.printStackTrace();
            } catch (IllegalArgumentException arg) {
                arg.printStackTrace();
            }

        }
        render("/Application/uploadWeb.html", photosUrl);
    }

    public static void uploadServer() {
        Tag tag = TagUtils.findOrCreateTag("FolderUpload");
        try {
            Files.walk(Paths.get("/opt/workspace/play1/tapdatass/date/attachments2")).forEach(path -> {
                File file = path.toFile();
                    String md5NewFile = FileUtils.getMD5(file);
                    if (FileUtils.checkIfNotExists(md5NewFile)) {
                        try {
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
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        }
                    }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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