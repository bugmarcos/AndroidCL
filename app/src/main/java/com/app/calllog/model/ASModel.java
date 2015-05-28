package com.app.calllog.model;

import android.content.Context;

import com.app.calllog.database.DBHelper;

/**
 * Created by Akash on 24-May-15.
 */
public abstract class ASModel extends BaseModel {

    public abstract String getModelName();

    public boolean insert(Context ctx) {
        return DBHelper.getInstance(ctx).insert(ctx, this);
    }

    public boolean update(Context ctx) {
        return DBHelper.getInstance(ctx).update(ctx, this);

    }

    public boolean delete(Context ctx) {
        return DBHelper.getInstance(ctx).delete(ctx, this);
    }

    public String getASColumnName() {
        return "id";
    }

    public String getASColumnValue() {
        return String.valueOf(getId());
    }

    public abstract int getId();

}
