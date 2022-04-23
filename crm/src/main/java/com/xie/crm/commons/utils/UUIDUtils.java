package com.xie.crm.commons.utils;

import java.util.UUID;

public class UUIDUtils {

    /**
     * 生成UUID
     * @return
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
