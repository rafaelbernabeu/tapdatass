package models;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import play.db.jpa.Blob;
import play.db.jpa.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rafael on 13/06/15.
 */
@Entity
public class Photo extends Model {

    public Photo() {
        this.photoBytes = new Blob();
        this.md5 = "";
        this.wins = 0l;
        this.loses = 0l;
        this.comments = null;
        this.tags = null;
        this.date = new Date();
    }

    private Blob photoBytes;
    private String md5;
    private long wins;
    private long loses;
    private Date date;

    @OneToMany
    private List<Comment> comments;

    @OneToMany(mappedBy = "photo", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<PhotoTag> tags = new ArrayList<PhotoTag>();

    public Blob getPhotoBytes() {
        return photoBytes;
    }

    public void setPhotoBytes(Blob photoBytes) {
        this.photoBytes = photoBytes;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getWins() {
        return wins;
    }

    public void setWins(long wins) {
        this.wins = wins;
    }

    public long getLoses() {
        return loses;
    }

    public void setLoses(long loses) {
        this.loses = loses;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<PhotoTag> getTags() {
        return tags;
    }

    public void setTags(List<PhotoTag> tags) {
        this.tags = tags;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}