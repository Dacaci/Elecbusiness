package com.electricitybusiness.api.util;

import java.util.ArrayList;
import java.util.List;

import com.electricitybusiness.api.model.Borne;

public class BorneUtils {

    private static double distance(double longitude, double latitude, double longitude2, double latitude2) {
        double R = 6371e3; // metres

        double latitude_radian = latitude * Math.PI/180; // φ, λ in radians
        double latitude_radian2 = latitude2 * Math.PI/180;
        double difference_latitude = (latitude2-latitude) * Math.PI/180;
        double difference_longitude = (longitude2-longitude) * Math.PI/180;

        double a = Math.sin(difference_latitude/2) * Math.sin(difference_latitude/2) +
                  Math.cos(latitude_radian) * Math.cos(latitude_radian2) *
                  Math.sin(difference_longitude/2) * Math.sin(difference_longitude/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double d = R * c; // in metres

        return d;
    }

    // Position Utilisateur, Liste de Borne, Rayon
    // Renvoie la liste des bornes à proximité de l'utilisateur (compris dans le rayon)
    public static List<Borne> getNearbyBornes(List<Borne> bornes, double longitude, double latitude, double rayon) {
        List<Borne> bornesProches = new ArrayList<>();

        for (Borne borne : bornes) {
            double dist = distance(longitude, latitude, borne.getLongitude().doubleValue(), borne.getLatitude().doubleValue());
            if (dist <= rayon) {
                bornesProches.add(borne);
            }
        }

        return bornesProches;
    }
} 