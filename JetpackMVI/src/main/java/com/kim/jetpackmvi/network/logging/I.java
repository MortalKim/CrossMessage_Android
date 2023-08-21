package com.kim.jetpackmvi.network.logging;

import java.util.logging.Level;

import okhttp3.internal.platform.Platform;

/**
 * @ClassName: I
 * @Description: java类作用描述
 * @Author: kim
 * @Date: 2/18/23 7:56 PM
 */
public class I {
    protected I() {
        throw new UnsupportedOperationException();
    }

    static void log(int type, String tag, String msg) {
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(tag);
        switch (type) {
            case Platform.INFO:
                logger.log(Level.INFO, msg);
                break;
            default:
                logger.log(Level.WARNING, msg);
                break;
        }
    }
}
