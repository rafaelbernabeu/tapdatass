package controllers;

import models.Photo;
import play.mvc.Controller;

import java.util.List;

/**
 * Created by rafael on 07/07/15.
 */
public class Gallery extends Controller {

    public static void index(String mode) {
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
}
