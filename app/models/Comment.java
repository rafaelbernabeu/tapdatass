package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import java.util.Date;

/**
 * Created by rafael on 13/06/15.
 */
@Entity
public class Comment extends Model {

    public Comment(String comment, String author, String emailAuthor) {
        this.date = new Date();
        this.comment = comment;
        this.author = author;
        this.emailAuthor = emailAuthor;
    }

    public Comment(String comment) {
        this.date = new Date();
        this.comment = comment;
    }

    private Date date;
    private String comment;
    private String author;

    private String emailAuthor;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEmailAuthor() {
        return emailAuthor;
    }

    public void setEmailAuthor(String emailAuthor) {
        this.emailAuthor = emailAuthor;
    }
}
