package com.mrebhan.paprika;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mrebhan.paprika.internal.SqlScripts;

import java.util.List;
import java.util.Map;

import static com.mrebhan.paprika.consts.Constants.PAPRIKA_PACKAGE;
import static com.mrebhan.paprika.consts.Constants.PAPRIKA_SQL_SCRIPTS_CLASS_NAME;

public class PaprikaDataHelper extends SQLiteOpenHelper {

    private final SqlScripts sqlScripts;
    private SQLiteDatabase writableDatabase;

    public PaprikaDataHelper(Context context, String name, SqlScripts sqlScripts) {
        super(context, name, null, sqlScripts.getVersion());
        this.sqlScripts = sqlScripts;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String script : sqlScripts.getCreateScripts()) {
            db.execSQL(script);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Map<Integer, List<String>> upgradeMap = sqlScripts.getUpgradeScripts();
        for (int version : upgradeMap.keySet()) {
            if (version > oldVersion) {
                for (String script : upgradeMap.get(version)) {
                    db.execSQL(script);
                }
            }
        }
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        if (writableDatabase == null) {
            writableDatabase = super.getWritableDatabase();
        }

        return writableDatabase;
    }
}
