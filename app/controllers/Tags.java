package controllers;

import models.Photo;
import models.PhotoTag;
import models.Tag;
import play.mvc.Controller;

import java.util.*;

/**
 * Created by rafael on 06/07/15.
 */
public class Tags extends Controller {

    public static void list() {
        List<Object[]> usos = Tag.em().createQuery(
            "SELECT count(p.id), t FROM Tag t LEFT JOIN t.photos p GROUP BY t.id"
        ).getResultList();
        HashMap<Tag,Long> tagsUsos = new HashMap<>();
        for (int i=0 ; i < usos.size() ; i++) {
            tagsUsos.put((Tag) usos.get(i)[1], (Long) usos.get(i)[0]);
        }
        render(tagsUsos);
    }

    public static void search(String tag) {
        List<Photo> photos = new ArrayList<>();
        List<PhotoTag> photoTags;
        List<Tag> tags = Tag.findAll();
        if (tag != null && !tag.equals("")) {
            photoTags = PhotoTag.em().createQuery("SELECT p " +
                    "FROM PhotoTag p " + //SQL INJECTION HERE
                    "WHERE p.tag IN (" + tag + ")")
                    .getResultList();
            photos = new ArrayList<Photo>(photoTags.size());
            for (PhotoTag photoTag : photoTags) {
                photos.add(photoTag.getPhoto());
            }
        }
        render(tags, photos);
    }

    public static void newTag(String tag) {
        if (tag != null) {
            Tag t = new Tag();
            t.setName(tag);
            t.save();
        }
        String title = "New";
        render("/Tags/edit.html", title);
    }

    public static void edit(long id, String name) {
        Tag tag = Tag.findById(id);
        if (id != 0 && name != null && !name.equals("")) {
            tag.setName(name);
            tag.save();
            list();
        }
        String title = "Edit";

        if (tag == null) { tag = new Tag("");}
        render("/Tags/edit.html", title, id, tag);
    }

    public static void delete(long id) {
        Tag t = Tag.findById(id);
        t.delete();
        list();
    }

}
