package controllers;

import models.Photo;
import models.PhotoTag;
import models.Tag;
import play.data.validation.Required;
import play.mvc.Controller;

import java.util.List;

/**
 * Created by rafael on 07/07/15.
 */
public class Upload extends Controller {

    public static void index() {
        List<Tag> tags = Tag.findAll();
        render(tags);
    }

    public static void addPhoto(Photo photo, @Required String[] tags) { //receber objeto TAG ao invez de string[]
        checkAuthenticity();
        if(validation.hasErrors()) {
            index();
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
        Gallery.index("newest");
    }
}
