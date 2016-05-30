package models;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import play.db.jpa.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;

/**
 * Created by rafael on 13/06/15.
 */
@Entity
public class Tag extends Model {

    public Tag(String name) {
        this.name = name;
    }

    private String name;

    @ManyToMany(mappedBy = "tags", cascade = CascadeType.REMOVE)
    private Set<Photo> photos;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
