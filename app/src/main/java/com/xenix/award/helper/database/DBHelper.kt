package com.xenix.award.helper.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.xenix.award.model.Awards
import com.xenix.award.model.Filter
import java.lang.Exception

import java.util.ArrayList

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    @Throws(SQLiteConstraintException::class)
    fun insertAward(awards: Awards): Boolean {
        val db = writableDatabase

        val values = ContentValues()
        values.put(DBContract.AwardEntry.COLUMN_AWARD_TYPE, awards.award_type)
        values.put(DBContract.AwardEntry.COLUMN_IMAGE_URL, awards.image_url)
        values.put(DBContract.AwardEntry.COLUMN_NAME, awards.name)
        values.put(DBContract.AwardEntry.COLUMN_POINT_NEEDED, awards.point_needed)

        db.insert(DBContract.AwardEntry.TABLE_NAME, null, values)

        return true
    }

    fun readRewardsWithFilter(filter: Filter): ArrayList<Awards> {
        val awards = ArrayList<Awards>()
        val db = writableDatabase
        val cursor: Cursor?
        try {
            val stringBuilder = StringBuilder()
            stringBuilder.append("SELECT * FROM " + DBContract.AwardEntry.TABLE_NAME + " ")
            if(filter.getPointNeededMin() != 0 || filter.getPointNeededMax() != 0 || !filter.getAwardType().equals("")) {
                stringBuilder.append("WHERE ")
                if(!filter.getAwardType().equals("")) {
                    stringBuilder.append(DBContract.AwardEntry.COLUMN_AWARD_TYPE + "='" + filter.getAwardType() + "' ")
                }

                if(filter.getPointNeededMin() != 0 || filter.getPointNeededMax() != 0) {
                    if(!filter.getAwardType().equals(""))
                        stringBuilder.append("AND ")
                    stringBuilder.append(DBContract.AwardEntry.COLUMN_POINT_NEEDED + ">=" + filter.getPointNeededMin() + " AND " + DBContract.AwardEntry.COLUMN_POINT_NEEDED + "<=" + filter.getPointNeededMax() + " ")
                }
            }
            stringBuilder.append("LIMIT " + filter.getLimit() + " OFFSET " + filter.getOffset())
            cursor = db.rawQuery(stringBuilder.toString(), null)
        } catch (e: SQLiteException) {
            //db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var award_type: String
        var image_url: String
        var name: String
        var poin: Int
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                award_type = cursor.getString(cursor.getColumnIndex(DBContract.AwardEntry.COLUMN_AWARD_TYPE))
                image_url = cursor.getString(cursor.getColumnIndex(DBContract.AwardEntry.COLUMN_IMAGE_URL))
                name = cursor.getString(cursor.getColumnIndex(DBContract.AwardEntry.COLUMN_NAME))
                poin = cursor.getInt(cursor.getColumnIndex(DBContract.AwardEntry.COLUMN_POINT_NEEDED))

                awards.add(Awards(award_type, image_url, name, poin))
                cursor.moveToNext()
            }
        }
        return awards
    }

    fun readAllAwards(): ArrayList<Awards> {
        val awards = ArrayList<Awards>()
        val db = writableDatabase
        val cursor: Cursor?
        try {
            cursor = db.rawQuery("SELECT * FROM " + DBContract.AwardEntry.TABLE_NAME, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var award_type: String
        var image_url: String
        var name: String
        var poin: Int
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                award_type = cursor.getString(cursor.getColumnIndex(DBContract.AwardEntry.COLUMN_AWARD_TYPE))
                image_url = cursor.getString(cursor.getColumnIndex(DBContract.AwardEntry.COLUMN_IMAGE_URL))
                name = cursor.getString(cursor.getColumnIndex(DBContract.AwardEntry.COLUMN_NAME))
                poin = cursor.getInt(cursor.getColumnIndex(DBContract.AwardEntry.COLUMN_POINT_NEEDED))

                awards.add(Awards(award_type, image_url, name, poin))
                cursor.moveToNext()
            }
        }
        return awards
    }

    fun getMinPoin(): Int {
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT * FROM " + DBContract.AwardEntry.TABLE_NAME + " ORDER BY " + DBContract.AwardEntry.COLUMN_POINT_NEEDED + " ASC LIMIT 1", null)
        } catch (e: Exception) {

        }
        if(cursor!!.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndex(DBContract.AwardEntry.COLUMN_POINT_NEEDED))
        }
        return 0
    }

    fun getMaxPoin(): Int {
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT * FROM " + DBContract.AwardEntry.TABLE_NAME + " ORDER BY " + DBContract.AwardEntry.COLUMN_POINT_NEEDED + " DESC LIMIT 1", null)
        } catch (e: Exception) {

        }
        if(cursor!!.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndex(DBContract.AwardEntry.COLUMN_POINT_NEEDED))
        }
        return 0
    }

    companion object {
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "Awards.db"

        private val SQL_CREATE_ENTRIES =
                "CREATE TABLE " + DBContract.AwardEntry.TABLE_NAME + " (" +
                        DBContract.AwardEntry.COLUMN_AWARD_TYPE + " TEXT," +
                        DBContract.AwardEntry.COLUMN_IMAGE_URL + " TEXT," +
                        DBContract.AwardEntry.COLUMN_NAME + " TEXT," +
                        DBContract.AwardEntry.COLUMN_POINT_NEEDED + " INTEGER)"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBContract.AwardEntry.TABLE_NAME
    }

}