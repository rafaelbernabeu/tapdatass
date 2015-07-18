package models;

import play.db.jpa.JPABase;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by rafael on 05/07/15.
 */
@Entity
@Table(name = "photo_tag")
public class PhotoTag extends Model {

    public PhotoTag(Photo photo, Tag tag) {
        this.photo = photo;
        this.tag = tag;
    }

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "photo_id", insertable = true, updatable = true)
    public Photo photo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tag_id", insertable = true, updatable = true)
    public Tag tag;

    public void salvar(List<PhotoTag> photosTags) {
        PhotoTag.em().getTransaction().begin();
        PhotoTag.em().joinTransaction();
        for (PhotoTag photoTag : photosTags) {
            PhotoTag.em().persist(photoTag);
        }
        PhotoTag.em().flush();
        PhotoTag.em().getTransaction().commit();
        PhotoTag.em().close();
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
