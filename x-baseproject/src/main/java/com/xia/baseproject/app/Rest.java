package com.xia.baseproject.app;

import com.xia.baseproject.app.RestConfigKeys.ConfigKey;

/**
 * @author xia
 * @date 2018/7/28.
 */
@SuppressWarnings("WeakerAccess")
public final class Rest {

    public static RestConfigurator init() {
        return RestConfigurator.getInstance();
    }

    public static <T> T getConfiguration(@ConfigKey String configKey) {
        return init().getConfiguration(configKey);
    }
}
