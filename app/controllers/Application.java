package controllers;

import models.Photo;
import models.Tag;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import play.mvc.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafael bernabeu on 06/07/15.
 */
public class Application extends Controller {

    public static void index() {
        //System.out.println((Log) new Log(request.remoteAddress).save());
        render();

    }

    public static void gallery(String mode) {
        List<Photo> photos = null;
        switch (mode) {
            case "all" :
                photos = Photo.find("from Photo ORDER BY dateUpload DESC").fetch(100);
                break;
            case "hottest" :
                photos = Photo.find("from Photo ORDER BY wins DESC").fetch(10);
                break;
            case "newest" :
                photos = Photo.find("from Photo ORDER BY dateUpload DESC").fetch(10);
                break;
        }
        render(photos, mode);
    }

    public static void uploadFile() {
        List<Tag> tags = Tag.findAll();
        render("/Application/uploadFile.html", tags);
    }

    public static void uploadFolder() {
        List<Tag> tags = Tag.findAll();
        render("/Application/uploadFolder.html", tags);
    }

    public static void uploadWeb(String url) {
        System.out.println(url);
        List<String> photos = new ArrayList<>();
        try {
            url = url.contains("http://") ? url : "http://" + url;
            Document doc = Jsoup.connect(url).get();
            System.out.println(doc.title());
            Elements elements = doc.body().getElementsByTag("img");
            System.out.println(elements.size());
            for (Element e : elements) {
                photos.add(e.attr("src"));
            }
        } catch (IOException io) {
            io.printStackTrace();
        } catch (IllegalArgumentException arg) {
            arg.printStackTrace();
        }
        render("/Application/uploadWeb.html", photos);
    }
}