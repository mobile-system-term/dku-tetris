package com.dku.tetris.utils

import android.content.Context
import com.dku.tetris.R
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.*

object DB {

    @JvmStatic
    fun createDatabaseIfNotExists(context: Context) {
        try {
            val file = File(context.filesDir, "database.xml")

            if (file.exists()) {
                if (file.length() == 0L) {
                    println("database.xml is empty. Creating a new one.")
                    createDatabase(context)
                }
            } else {
                println("database.xml does not exist. Creating a new one.")
                createDatabase(context)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            println("Error checking or creating database.xml: ${e.message}")
        }
    }

    private fun createDatabase(context: Context) {
        println("Creating new database.xml at ${context.filesDir}")

        val databaseFile = File(context.filesDir, "database.xml")

        try {
            context.resources.openRawResource(R.raw.database).use { inputStream ->
                FileOutputStream(databaseFile).use { outputStream ->
                    val buff = ByteArray(1024)
                    var length: Int
                    while (inputStream.read(buff).also { length = it } > 0) {
                        outputStream.write(buff, 0, length)
                    }
                }
            }

            println("database.xml created successfully.")
        } catch (e: IOException) {
            e.printStackTrace()
            println("Failed to create database.xml: ${e.message}")
        }
    }

    @JvmStatic
    @Throws(XmlPullParserException::class, IOException::class)
    fun skipParser(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException("Expected a start tag but found something else.")
        }

        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}
