package com.example.app;




public class Deliveryfee {
    final private static double DIST_SHORT = 1.5; //Short distance fee
    final private static double DIST_MED = 3.0; //Medium distance fee
    final private static double DIST_LONG = 5.0; //Long distance fee

    public static double getDeliveryfee(double distance){ // Distance is in km.
        if(distance < 3.0){ //Short Distance
            return DIST_SHORT;
        } else if (distance < 5.0) { //Medium Distance
            return DIST_MED;
        }
        else{ //Long Distance
            return DIST_LONG;
        }
    }

    public static String redeemDistance(double distance){
        if(distance < 3.0){ //Short Distance
            return "Short distance";
        } else if (distance < 5.0) { //Medium Distance
            return "Medium distance";
        }
        else{ //Long Distance
            return "Long distance";
        }
    }

}
