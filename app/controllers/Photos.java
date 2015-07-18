package controllers;

import models.Photo;
import play.mvc.Controller;

/**
 * Created by rafael on 17/07/15.
 */
public class Photos extends Controller {

    public static void delete(long id) {
        Photo p = Photo.findById(id);
        p.delete();
        Gallery.index("newest");
    }
}