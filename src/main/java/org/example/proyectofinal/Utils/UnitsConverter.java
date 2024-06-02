package org.example.proyectofinal.Utils;

public class UnitsConverter {
    public static String convertbpsToHumanReadable(double bps) {
            if (bps > 1000000000){
                bps = bps / 1000000000;
                return String.format("%.2f", bps)+" Gbps";
            } else if (bps > 1000000){
                bps = bps / 1000000;
                return String.format("%.2f", bps)+" Mbps";
            } else if (bps > 1000){
                bps = bps / 1000;
                return String.format("%.2f", bps)+" Kbps";
            } else if (bps > 0){
                return String.format("%.2f", bps)+" bps";
            } else {
                return "Invalid bps value";
            }
    }

    public static String convertSecondsToHumanReadable(double seconds) {
        if (seconds > 3600){
            double hours = seconds / 3600;
            return String.format("%.2f", hours)+" h";
        } else if (seconds > 60){
            double minutes = seconds / 60;
            return String.format("%.2f", minutes)+" m";
        } else if (seconds > 0){
            return String.format("%.2f", seconds)+" s";
        } else {
            return "Invalid seconds value";
        }
    }
}
