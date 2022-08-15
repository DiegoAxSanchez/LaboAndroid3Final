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
    fun insertItem(item: ItemModel): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues()
        values.put(DBContract.ItemEntry.COLUMN_ID, item.id)
        values.put(DBContract.ItemEntry.COLUMN_NAME, item.name)
        values.put(DBContract.ItemEntry.COLUMN_CATEGORY, item.category)
        values.put(DBContract.ItemEntry.COLUMN_PRICE, item.price)
        values.put(DBContract.ItemEntry.COLUMN_QUANTITY, item.quantity)


        // Insert the new row, returning the primary key value of the new row
        db.insert(DBContract.ItemEntry.TABLE_NAME, null, values)

        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteItem(id: String): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase
        // Define 'where' part of query.
        val selection = DBContract.ItemEntry.COLUMN_ID + " LIKE ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(id)
        // Issue SQL statement.
        db.delete(DBContract.ItemEntry.TABLE_NAME, selection, selectionArgs)

        return true
    }

    fun readItem(id: Int): ArrayList<ItemModel> {
        val items = ArrayList<ItemModel>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DBContract.ItemEntry.TABLE_NAME + " WHERE " + DBContract.ItemEntry.COLUMN_ID + "='" + id + "'", null)
        } catch (e: SQLiteException) {
            // if table not yet present, create it
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var id: Int
        var name: String
        var category: String
        var price : Double
        var quantity: Int



        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                id = cursor.getInt(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_ID))
                name = cursor.getString(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_NAME))
                category = cursor.getString(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_CATEGORY))
                price = cursor.getDouble(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_PRICE))
                quantity = cursor.getInt(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_QUANTITY))

                items.add(ItemModel(id, name, category, price, quantity))
                cursor.moveToNext()
            }
        }
        return items
    }

    fun searchByCategory(category : String): List<ItemModel>{
        val items = ArrayList<ItemModel>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DBContract.ItemEntry.TABLE_NAME + " WHERE " + DBContract.ItemEntry.COLUMN_CATEGORY + " ='"+category+"'", null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var id: Int
        var name: String
        var category: String
        var price : Double
        var quantity: Int
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                id = cursor.getInt(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_ID))
                name = cursor.getString(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_NAME))
                category = cursor.getString(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_CATEGORY))
                price = cursor.getDouble(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_PRICE))
                quantity = cursor.getInt(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_QUANTITY))

                items.add(ItemModel(id, name, category, price, quantity))
                cursor.moveToNext()
            }
        }
        return items
    }

    fun readAllCategories(): ArrayList<String>{
        val categories = ArrayList<String>()
        val db = writableDatabase
        var cursor : Cursor?
            try {
                cursor = db.rawQuery("select DISTINCT " + DBContract.ItemEntry.COLUMN_CATEGORY+ " from " + DBContract.ItemEntry.TABLE_NAME, null)
            } catch (e: SQLiteException) {
                db.execSQL(SQL_CREATE_ENTRIES)
                return ArrayList()
            }

            var category: String

            if (cursor!!.moveToFirst()) {
                while (cursor.isAfterLast == false) {
                    category = cursor.getString(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_CATEGORY))
                    categories.add(category)
                    cursor.moveToNext()
                }
            }
            return categories
        }

    fun readAllItems(): ArrayList<ItemModel> {
        val items = ArrayList<ItemModel>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DBContract.ItemEntry.TABLE_NAME, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var id: Int
        var name: String
        var category: String
        var price : Double
        var quantity: Int
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                id = cursor.getInt(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_ID))
                name = cursor.getString(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_NAME))
                category = cursor.getString(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_CATEGORY))
                price = cursor.getDouble(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_PRICE))
                quantity = cursor.getInt(cursor.getColumnIndex(DBContract.ItemEntry.COLUMN_QUANTITY))

                items.add(ItemModel(id, name, category, price, quantity))
                cursor.moveToNext()
            }
        }
        return items
    }

    fun populateDB(){
        // Gets the data repository in write mode
        val db = writableDatabase
        val item1 = ItemModel(1, "Coke", "Drink", 1.50, 30)
        val item2 = ItemModel(2, "Pepsi", "Drink", 1.45, 25)
        val item3 = ItemModel(3, "Beef", "Food", 6.50, 5)
        val item4 = ItemModel(4, "Fish", "Food", 7.75, 8)
        val item5 = ItemModel(5,"Chicken","Food",4.50,12)
        val item6 = ItemModel(6,"BBQ","Tools", 60.50, 2)

        val initialList : List<ItemModel> = listOf<ItemModel>(item1,item2,item3,item4,item5,item6)

        for (item in initialList) {
            // Create a new map of values, where column names are the keys
            val values = ContentValues()
            values.put(DBContract.ItemEntry.COLUMN_ID, item.id)
            values.put(DBContract.ItemEntry.COLUMN_NAME, item.name)
            values.put(DBContract.ItemEntry.COLUMN_CATEGORY, item.category)
            values.put(DBContract.ItemEntry.COLUMN_PRICE, item.price)
            values.put(DBContract.ItemEntry.COLUMN_QUANTITY, item.quantity)
            // Insert the new row, returning the primary key value of the new row
            db.insertWithOnConflict(DBContract.ItemEntry.TABLE_NAME, null, values, 5)
        }
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "Items"

        private val SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + DBContract.ItemEntry.TABLE_NAME + " (" +
                    DBContract.ItemEntry.COLUMN_ID + " INTEGER PRIMARY KEY," +
                    DBContract.ItemEntry.COLUMN_NAME + " TEXT," +
                    DBContract.ItemEntry.COLUMN_CATEGORY + " TEXT," +
                    DBContract.ItemEntry.COLUMN_PRICE + " REAL," +
                    DBContract.ItemEntry.COLUMN_QUANTITY + " INTEGER," +
                    "UNIQUE ("+DBContract.ItemEntry.COLUMN_ID+") ON CONFLICT REPLACE)"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBContract.ItemEntry.TABLE_NAME
    }

}


