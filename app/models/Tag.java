package models;

import play.db.jpa.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by rafael on 13/06/15.
 */
@Entity
public class Tag extends Model {

    public Tag(String name) {
        this.name = name;
    }

    public Tag() {
        this.name = "";
    }

    private String name;

    @OneToMany(mappedBy = "tag", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<PhotoTag> photos = new ArrayList<PhotoTag>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PhotoTag> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoTag> photos) {
        this.photos = photos;
    }
}