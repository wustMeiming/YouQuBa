package com.dream4it.youquba.utils;

import com.dream4it.youquba.data.PictureItemData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meiming on 17-2-1.
 */

public class JsoupUtil {

    public static List<PictureItemData> parsePictures(String response) {
        Document document = Jsoup.parse(response);
        Elements elements = document.select("body > div.adtop > div.indexbox > div.box-l > div.box-l-m > ul > li");
        List<PictureItemData> list = new ArrayList<>();
        PictureItemData data;
        for (Element element : elements) {
            data = new PictureItemData();
            data.setId(element.select("a").attr("href").substring(element.attr("href").lastIndexOf("/") + 1));
            data.setTitle(element.select("p > a").attr("title"));
            data.setImage(element.select("a > img").attr("src"));
            list.add(data);
        }

        return list;
    }
}
