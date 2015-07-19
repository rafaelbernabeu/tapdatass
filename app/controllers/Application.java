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