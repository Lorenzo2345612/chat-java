package org.example.proyectofinal.Utils;

public class TransferMetrics {
    public static double calculateTransferRate(long bytes, long time) {
        double bps = ((double) bytes * 8) / time;
        return bps;
    }
    public static double calculateRemainingTime(long remainingBytes, double bps) {
        return (double) (remainingBytes * 8) / bps;
    }
}
