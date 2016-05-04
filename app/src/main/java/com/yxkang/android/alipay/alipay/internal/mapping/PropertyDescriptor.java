package com.yxkang.android.alipay.alipay.internal.mapping;

import java.lang.reflect.Method;

/**
 * Created by yexiaokang on 2016/5/4.
 */
public class PropertyDescriptor {
    private String name;
    private String displayName;
    private Method readMethod;
    private Method writeMethod;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Method getReadMethod() {
        return readMethod;
    }

    public void setReadMethod(Method readMethod) {
        this.readMethod = readMethod;
    }

    public Method getWriteMethod() {
        return writeMethod;
    }

    public void setWriteMethod(Method writeMethod) {
        this.writeMethod = writeMethod;
    }
}