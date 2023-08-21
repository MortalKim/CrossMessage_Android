package com.kim.jetpackmvi.network.logging;

import okhttp3.internal.platform.Platform;

/**
 * @ClassName: Logger
 * @Description: java类作用描述
 * @Author: kim
 * @Date: 2/18/23 7:57 PM
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public interface Logger {
    void log(int level, String tag, String msg);

    Logger DEFAULT = new Logger() {
        @Override
        public void log(int level, String tag, String message) {
            Platform.get().log(message, level , null);
        }
    };
}
