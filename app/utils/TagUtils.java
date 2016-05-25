package utils;

import models.Tag;

import java.util.List;

/**
 * Created by rab on 25/05/16.
 */
public class TagUtils {

    public static String idsFromTagList(List<Tag> tags) {
        StringBuilder sb = new StringBuilder();
        for (Tag tag : tags){
            sb.append(tag.getId());
            sb.append(",");
        }
        return sb.toString().substring(0, sb.length()-1);
    }
}
