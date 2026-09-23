package aji.intern.restapi.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tools.jackson.databind.ObjectMapper;

public class InspectJsonUtil {

    private static final Logger log = LogManager.getLogger(InspectJsonUtil.class);

    public static void inspectJsonWithPrettier(Object value) {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        log.info(json);
    }
}
