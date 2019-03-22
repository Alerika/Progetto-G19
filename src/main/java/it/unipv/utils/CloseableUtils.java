package it.unipv.utils;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

public class CloseableUtils {

    public static void flush(Flushable... toFlush) {
        for(Flushable f : toFlush) {
            try {
                if( f != null) {
                    f.flush();
                }
            } catch (IOException e) {
                throw new ApplicationException(e);
            }
        }
    }
    public static void close(Closeable... toClose) {
        for (Closeable c : toClose) {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (Exception e) {
                throw new ApplicationException(e);
            }
        }
    }
}
