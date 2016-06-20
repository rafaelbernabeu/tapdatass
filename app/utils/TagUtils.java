package utils;

import models.Tag;
import org.apache.commons.lang.StringUtils;

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

    public static Tag findOrCreateTag(String tag) {
        if (StringUtils.isNotBlank(tag)) {
            List<Tag> t = Tag.find("byName", tag).fetch();
            if (t == null || t.size() == 0) {
                Tag tg = new Tag(tag);
                tg.save();
                System.out.println(tg);
                return tg;
            } else {
                System.out.println(t.get(0));
                return t.get(0);
            }
        }
        return null;
    }
}
