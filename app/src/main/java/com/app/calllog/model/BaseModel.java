package com.app.calllog.model;

import android.content.Context;

/**
 * Created by Akash on 24-May-15.
 */
public abstract class BaseModel {

    public String getModelName() {
        return getClass().getSimpleName().toLowerCase();
    }

    public abstract boolean insert(Context ctx);

    public abstract boolean update(Context ctx);

    public abstract boolean delete(Context ctx);

}