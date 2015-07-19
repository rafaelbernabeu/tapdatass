package controllers;

import models.Photo;
import models.Tag;
import play.mvc.Controller;

import java.util.List;

public class Application extends Controller {

    public static void index() {
        flash.success("Bem-Vindo! SEU IP >>> ");
        render();
    }

    public static void gallery(String mode) {
        List<Photo> photos = null;
        switch (mode) {
            case "all" :
                photos = Photo.find("from Photo ORDER BY date DESC").fetch(100);
                break;
            case "hottest" :
                photos = Photo.find("from Photo ORDER BY wins DESC").fetch(10);
                break;
            case "newest" :
                photos = Photo.find("from Photo ORDER BY date DESC").fetch(10);
                break;
        }
        render(photos, mode);
    }

    public static void upload() {
        List<Tag> tags = Tag.findAll();
        render("/Application/upload.html", tags);
    }
}