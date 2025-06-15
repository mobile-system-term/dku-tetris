package com.dku.tetris.settings.dao

import android.content.Context
import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException

import com.dku.tetris.settings.dto.SettingDTO
import com.dku.tetris.utils.DB

object SettingDAO {
    private var parserSetting: XmlPullParser? = null

    fun getSetting(context: Context): SettingDTO? {
        var setting: SettingDTO? = null
        var bufferedReader: BufferedReader? = null
        try {
            val fileName = "${context.filesDir}/database.xml"
            bufferedReader = BufferedReader(FileReader(fileName))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            bufferedReader = null
        }

        if (bufferedReader != null) {
            try {
                parserSetting = Xml.newPullParser()
                parserSetting?.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                parserSetting?.setInput(bufferedReader)
                parserSetting?.nextTag()
            } catch (e: XmlPullParserException) {
                e.printStackTrace()
                parserSetting = null
            } catch (e: IOException) {
                e.printStackTrace()
                parserSetting = null
            }
            if (parserSetting != null) {
                try {
                    parserSetting?.require(XmlPullParser.START_TAG, null, "Database")
                    while (parserSetting?.next() != XmlPullParser.END_TAG) {
                        if (parserSetting?.eventType == XmlPullParser.START_TAG) {
                            if (parserSetting?.name == "Setting") {
                                setting = readSettingTag()
                            } else {
                                DB.skipParser(parserSetting!!)
                            }
                        }
                    }
                } catch (e: XmlPullParserException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                try {
                    bufferedReader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return setting
    }

    private fun readSettingTag(): SettingDTO? {
        var setting: SettingDTO? = null
        try {
            var scoreLimit: Long = -1
            var lineScore: Long = -1
            var controlScheme: String? = null
            var timeLevels: MutableList<MutableList<Int>>? = null
            var width = -1
            var height = -1
            parserSetting?.require(XmlPullParser.START_TAG, null, "Setting")
            while (parserSetting?.next() != XmlPullParser.END_TAG) {
                if (parserSetting?.eventType == XmlPullParser.START_TAG) {
                    when (parserSetting?.name) {
                        "ScoreLimit" -> {
                            parserSetting?.require(XmlPullParser.START_TAG, null, "ScoreLimit")
                            scoreLimit = parserSetting?.getAttributeValue(null, "value")?.toLong() ?: -1
                            parserSetting?.next()
                            parserSetting?.require(XmlPullParser.END_TAG, null, "ScoreLimit")
                        }
                        "LineScore" -> {
                            parserSetting?.require(XmlPullParser.START_TAG, null, "LineScore")
                            lineScore = parserSetting?.getAttributeValue(null, "value")?.toLong() ?: -1
                            parserSetting?.next()
                            parserSetting?.require(XmlPullParser.END_TAG, null, "LineScore")
                        }
                        "TimeLevel" -> {
                            parserSetting?.require(XmlPullParser.START_TAG, null, "TimeLevel")
                            timeLevels = readTimeLevel()
                            parserSetting?.require(XmlPullParser.END_TAG, null, "TimeLevel")
                        }
                        "BoardSize" -> {
                            parserSetting?.require(XmlPullParser.START_TAG, null, "BoardSize")
                            width = 16
                            height = 30
                            parserSetting?.next()
                            parserSetting?.require(XmlPullParser.END_TAG, null, "BoardSize")
                        }
                    }
                }
            }
            if (scoreLimit != -1L && lineScore != -1L && timeLevels != null
                && width != -1 && height != -1
            ) {
                setting = SettingDTO(lineScore, timeLevels, width, height)
            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return setting
    }

    private fun readTimeLevel(): MutableList<MutableList<Int>> {
        val timeLevel: MutableList<MutableList<Int>> = mutableListOf()
        try {
            while (parserSetting?.next() != XmlPullParser.END_TAG) {
                if (parserSetting?.eventType == XmlPullParser.START_TAG) {
                    val level = mutableListOf<Int>()
                    val tsStr = parserSetting?.getAttributeValue(null, "timestamp")
                    val dsStr = parserSetting?.getAttributeValue(null, "dropspeed")
                    level.add(tsStr?.toInt() ?: 0)
                    level.add(dsStr?.toInt() ?: 0)
                    timeLevel.add(level)
                    parserSetting?.next()
                }
            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return timeLevel
    }
}