package models;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import play.db.jpa.Model;

import javax.persistence.Entity;
import java.util.Date;

/**
 * Created by rab on 28/05/16.
 */
@Entity
public class Log extends Model{

    public Log(String log) {
        this.log = log;
        this.date = new Date();
    }

    private Date date;
    private String log;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
