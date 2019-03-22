package it.unipv.utils;

import java.io.Closeable;

public class CloseableUtils {

    public static void close(Closeable... toClose) {
        for (Closeable c : toClose) {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (Throwable t) {
                // ignoro, non mi interessa
            }
        }
    }
}
