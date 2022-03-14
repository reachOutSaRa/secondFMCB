package com.iscp.utilities;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:properties/${env}.properties",
        "classpath:properties/environment.properties"
})
//properties/e1.properties  classpath:${env}.properties
public interface EnvironmentData extends Config {

    String webURL();
    String browserName();
    String execution();
}
