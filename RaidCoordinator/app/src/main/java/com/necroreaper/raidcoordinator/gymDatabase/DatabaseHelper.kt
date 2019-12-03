package com.necroreaper.raidcoordinator.gymDatabase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by zhitingz on 9/9/16.
 */
class DatabaseHelper(private val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, 1) {
    private val db: SQLiteDatabase? = null

    init {
        DB_PATH = context.applicationInfo.dataDir + "/databases/"
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {

    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {

    }

    @Throws(IOException::class)
    private fun copyDatabase() {
        val input = context.assets.open(DB_NAME)
        val outFileName = DB_PATH + DB_NAME
        val output = FileOutputStream(outFileName)

        val buf = ByteArray(4096)
        var len = input.read(buf)
        while (len > 0) {
            output.write(buf, 0, len)
            len = input.read(buf)
        }
        output.flush()
        output.close()
        input.close()
    }


    @Throws(IOException::class)
    fun createDatabase() {
        this.readableDatabase
        try {
            copyDatabase()
        } catch (e: IOException) {
            throw IOException("Fail to copy database")
        } finally {
            this.close()
        }
    }

    @Synchronized
    override fun close() {
        db?.close()
        super.close()
    }

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        db.execSQL("PRAGMA foreign_keys=ON")
    }

    companion object {
        private val DB_NAME = "gyms.db"
        private lateinit var DB_PATH: String
    }
}