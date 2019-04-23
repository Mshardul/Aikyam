
package com.example.aikyam;

import java.util.ArrayList;

public class DataModel {

    String venue;
    String date;
    String desc;
    int image;
    String e_id;

    public DataModel(String venue,String desc, String date, int image,String e_id) {
        this.venue = venue;
        this.date = date;
        this.image=image;
        this.desc=desc;
        this.e_id=e_id;
    }

    public String getVenue() {

        return venue;
    }

    public String getDate() {
        return date;
    }
    public String getId() {
        return e_id;
    }

    public String getDesc() {
        return desc.substring(0,50)+"....";
    }

    public int getImage() {
        return image;
    }
    public String fullDesc()
    {
        return desc;
    }

}
