package com.example.app;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RetrieveLocation {
    public static void setimgloc(ImageView img, TextView malllocation ,TextView storeloc, String foodStore) {
        switch (foodStore) {
            case "KOI":
                Koi koi = new Koi();
                img.setImageResource(koi.DrawableImage().get(0));
                malllocation.setText("Mall: " + koi.getmalllocation());
                storeloc.setText("Store Location: " + koi.getstorelocation());
                break;
            case "MAC DONALD":
                Mac mc = new Mac();
                img.setImageResource(mc.DrawableImage().get(0));
                malllocation.setText("Mall: " + mc.getmalllocation());
                storeloc.setText("Store Location: " + mc.getstorelocation());
                break;
            case "Wok Hey":
                Wokhey wok = new Wokhey();
                img.setImageResource(wok.DrawableImage().get(0));
                malllocation.setText("Mall: " + wok.getmalllocation());
                storeloc.setText("Store Location: " + wok.getstorelocation());
                break;
        }
    }

    public static List<Integer> getImage(String foodStore){

            if(foodStore.equals("KOI")) {
                Koi koi = new Koi();
                return koi.DrawableImage();
            }
            else if(foodStore.equals("MAC DONALD")) {
                Mac mc = new Mac();
                return mc.DrawableImage();
            }
            else{
                Wokhey wok = new Wokhey();
                return wok.DrawableImage();
            }
        }
}

