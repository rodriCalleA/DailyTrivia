package es.upm.miw.firebaselogin.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ColorMapper {
    public final static HashMap<String, String> colorMap = new HashMap<>();

    public final static String[] categories = {
            "General Knowledge",
            "Entertainment: Books",
            "Entertainment: Film",
            "Entertainment: Music",
            "Entertainment: Musicals & Theatres",
            "Entertainment: Television",
            "Entertainment: Video Games",
            "Entertainment: Board Games",
            "Science & Nature",
            "Science: Computers",
            "Science: Mathematics",
            "Mythology", "Sports",
            "Geography", "History",
            "Politics",
            "Art",
            "Celebrities",
            "Animals",
            "Vehicles",
            "Entertainment: Comics",
            "Science: Gadgets",
            "Entertainment: Japanese Anime & Manga",
            "Entertainment: Cartoon & Animations"
    };

    static {
        // Asociaciones de tipo a nombre de color
        colorMap.put("General Knowledge", "general_knowledge_color");
        colorMap.put("Entertainment: Books", "entertainment_color");
        colorMap.put("Entertainment: Film", "entertainment_color");
        colorMap.put("Entertainment: Music", "entertainment_color");
        colorMap.put("Entertainment: Musicals & Theatres", "entertainment_color");
        colorMap.put("Entertainment: Television", "entertainment_color");
        colorMap.put("Entertainment: Video Games", "entertainment_color");
        colorMap.put("Entertainment: Board Games", "entertainment_color");
        colorMap.put("Science & Nature", "science_color");
        colorMap.put("Science: Computers", "science_color");
        colorMap.put("Science: Mathematics", "science_color");
        colorMap.put("Mythology", "mythology_color");
        colorMap.put("Sports", "sports_color");
        colorMap.put("Geography", "geography_color");
        colorMap.put("History", "history_color");
        colorMap.put("Politics", "politics_color");
        colorMap.put("Art", "art_color");
        colorMap.put("Celebrities", "celebrities_color");
        colorMap.put("Animals", "animals_color");
        colorMap.put("Vehicles", "science_color");
        colorMap.put("Entertainment: Comics", "entertainment_color");
        colorMap.put("Science: Gadgets", "science_color");
        colorMap.put("Entertainment: Japanese Anime & Manga", "entertainment_color");
        colorMap.put("Entertainment: Cartoon & Animations", "entertainment_color");

    }

    public static String getColorName(String category) {
        return colorMap.get(category);
    }

    public static List<String> getCategories() {
        return new ArrayList<>(colorMap.keySet());
    }
}

