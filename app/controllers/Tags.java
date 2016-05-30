package controllers;

import models.Photo;
import models.Tag;
import play.mvc.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by rafael bernabeu on 06/07/15.
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
        System.out.println(tag);
        Set<Photo> photos = null;
        if (tag != null && !tag.equals("")) {
            checkAuthenticity();
            List<Tag> t = Tag.em().createQuery("SELECT t " +
                    "FROM Tag t " + //SQL INJECTION HERE
                    "WHERE t.id = ?)")
                    .setParameter(1, Long.valueOf(tag))
                    .getResultList();
            photos = t.get(0).getPhotos();
        }
        List<Tag> tags = Tag.findAll();
        render(tags, photos);
    }

    public static void newTag(String tag) {
        if (tag != null) {
            checkAuthenticity();
            Tag t = new Tag(tag);
            t.save();
        }
        String title = "New";
        render("/Tags/edit.html", title);
    }

    public static void edit(long id, String name) {
        Tag tag = null;
        if (id != 0) {
            checkAuthenticity();
            tag = Tag.findById(id);
            if (tag != null && name != null && !name.equals("")) {
                tag.setName(name);
                tag.save();
                list();
            }
        } else {
            tag = new Tag("");
        }
        String title = "Edit";
        render("/Tags/edit.html", title, id, tag);
    }

    public static void delete(long id) {
        checkAuthenticity();
        Tag t = Tag.findById(id);
        for (Photo p : t.getPhotos()) {
            p.delete();
        }
        t.setPhotos(null);
        t.delete();
        list();
    }

}
