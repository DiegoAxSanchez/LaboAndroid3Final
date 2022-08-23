package com.example.inventory.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper


class ItemsDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
        populateDB(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    @Throws(SQLiteConstraintException::class)
    fun insertItem(item: VolModel): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues()
        values.put(DBContract.ItemEntry.COLUMN_CODE, item.code)
        values.put(DBContract.ItemEntry.COLUMN_DEPART, item.depart)
        values.put(DBContract.ItemEntry.COLUMN_DESTINATION, item.destination)
        values.put(DBContract.ItemEntry.COLUMN_TRANSPORTEUR, item.transporteur)


        // Insert the new row, returning the primary key value of the new row
        db.insert(DBContract.ItemEntry.TABLE_NAME, null, values)

        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteItem(id: String): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase
        // Define 'where' part of query.
        val selection = DBContract.ItemEntry.COLUMN_CODE + " LIKE ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(id)
        // Issue SQL statement.
        db.delete(DBContract.ItemEntry.TABLE_NAME, selection, selectionArgs)

        return true
    }

    fun readItem(id: Int): ArrayList<VolModel> {
        val items = ArrayList<VolModel>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DBContract.ItemEntry.TABLE_NAME + " WHERE " + DBContract.ItemEntry.COLUMN_CODE + "='" + id + "'", null)
        } catch (e: SQLiteException) {
            // if table not yet present, create it
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var code: Int
        var destination: String
        var depart : String
        var transporteur: String




        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                code = cursor.getInt(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_CODE))
                depart = cursor.getString(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_DEPART))
                destination = cursor.getString(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_DESTINATION))
                transporteur = cursor.getString(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_TRANSPORTEUR))

                items.add(VolModel(code, depart,  destination, transporteur))
                cursor.moveToNext()
            }
        }
        return items
    }

    fun searchByCategory(category : String): List<VolModel>{
        val items = ArrayList<VolModel>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DBContract.ItemEntry.TABLE_NAME + " WHERE " + DBContract.ItemEntry.COLUMN_TRANSPORTEUR + " ='"+category+"'", null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var code: Int
        var depart : String
        var destination: String
        var transporteur: String

        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                code = cursor.getInt(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_CODE))
                depart = cursor.getString(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_DEPART))
                destination = cursor.getString(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_DESTINATION))
                transporteur = cursor.getString(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_TRANSPORTEUR))

                items.add(VolModel(code, depart, destination, transporteur))
                cursor.moveToNext()
            }
        }
        return items
    }
    fun searchByDepart(category : String): List<VolModel>{
        val items = ArrayList<VolModel>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DBContract.ItemEntry.TABLE_NAME + " WHERE " + DBContract.ItemEntry.COLUMN_DEPART + " ='"+category+"'", null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var code: Int
        var depart : String
        var destination: String
        var transporteur: String

        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                code = cursor.getInt(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_CODE))
                depart = cursor.getString(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_DEPART))
                destination = cursor.getString(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_DESTINATION))
                transporteur = cursor.getString(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_TRANSPORTEUR))

                items.add(VolModel(code, depart, destination, transporteur))
                cursor.moveToNext()
            }
        }
        return items
    }

    fun readAllCategories(): ArrayList<String>{
        val categories = ArrayList<String>()
        val db = writableDatabase
        val cursor : Cursor?
            try {
                cursor = db.rawQuery("select DISTINCT " + DBContract.ItemEntry.COLUMN_TRANSPORTEUR+ " from " + DBContract.ItemEntry.TABLE_NAME, null)
            } catch (e: SQLiteException) {
                db.execSQL(SQL_CREATE_ENTRIES)
                return ArrayList()
            }

            var category: String

            if (cursor!!.moveToFirst()) {
                while (!cursor.isAfterLast) {
                    category = cursor.getString(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_TRANSPORTEUR))
                    categories.add(category)
                    cursor.moveToNext()
                }
            }
            return categories
        }

    fun readAllItems(): ArrayList<VolModel> {
        val items = ArrayList<VolModel>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DBContract.ItemEntry.TABLE_NAME, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var code: Int
        var depart : String
        var destination: String
        var transporteur: String

        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                code = cursor.getInt(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_CODE))
                depart = cursor.getString(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_DEPART))
                destination = cursor.getString(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_DESTINATION))
                transporteur = cursor.getString(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_TRANSPORTEUR))

                items.add(VolModel(code, depart, destination, transporteur))
                cursor.moveToNext()
            }
        }
        return items
    }

    private fun populateDB(db : SQLiteDatabase){
        // Gets the data repository in write mode

        val item1 = VolModel(1, "Montreal", "Quebec", "Limocar Laurentides")
        val item2 = VolModel(2, "Montreal", "Paris", "Air France")
        val item3 = VolModel(3, "Montreal", "New York", "Air Canada")
        val item4 = VolModel(4, "Varsovie", "Montreal", "LOT")
        val item5 = VolModel(5,"Montreal","New York","Air Canada")
        val item6 = VolModel(6, "Montreal", "Paris", "Air Canada")
        val item7 = VolModel(7, "Montreal", "New York", "Air Canada" )
        val item8 = VolModel(8,"Montreal", "New York", "Pan Am")
        val item9 = VolModel(8, "New York", "Montreal", "Voyageur")

        val initialList : List<VolModel> = listOf(item1,item2,item3,item4,item5,item6,item7,item8,item9)

        for (item in initialList) {
            // Create a new map of values, where column names are the keys
            val values = ContentValues()
            values.put(DBContract.ItemEntry.COLUMN_CODE, item.code)
            values.put(DBContract.ItemEntry.COLUMN_DEPART, item.depart)
            values.put(DBContract.ItemEntry.COLUMN_DESTINATION, item.destination)
            values.put(DBContract.ItemEntry.COLUMN_TRANSPORTEUR, item.transporteur)
            // Insert the new row, returning the primary key value of the new row
            db.insertWithOnConflict(DBContract.ItemEntry.TABLE_NAME, null, values, 5)
        }
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "voyagesDB"

        private val SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + DBContract.ItemEntry.TABLE_NAME + " (" +
                    DBContract.ItemEntry.COLUMN_CODE + " INTEGER," +
                    DBContract.ItemEntry.COLUMN_DEPART + " TEXT,"+
                    DBContract.ItemEntry.COLUMN_DESTINATION + " TEXT," +
                    DBContract.ItemEntry.COLUMN_TRANSPORTEUR + " TEXT)"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBContract.ItemEntry.TABLE_NAME
    }

}


