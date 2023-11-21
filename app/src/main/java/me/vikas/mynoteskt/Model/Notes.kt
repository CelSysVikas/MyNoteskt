package me.vikas.mynoteskt.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import me.vikas.mynoteskt.Util.Config

@Entity(tableName = Config.TABLE_NAME)
data class Notes(
    @PrimaryKey(autoGenerate = true) val id: Int,

    @ColumnInfo(Config.COLUMN_TITLE)  var title: String,
    @ColumnInfo(Config.COLUMN_CONTENT)  var content: String,
    @ColumnInfo(Config.COLUMN_DATE_TIME) var dateTime: String,
    @ColumnInfo(Config.COLUMN_NOTE_PINNED) var isPinned: Boolean,
    @ColumnInfo(Config.COLUMN_COLOR_CODE) var colorCode: Int
){
    constructor(
        title: String,
        content: String,
        dateTime: String
    ) : this(id=0,title,content,dateTime,isPinned = false,colorCode=0)

    constructor(
        title: String,
        content: String,
        dateTime: String,
        colorCode: Int
    ):this(id=0,title, content, dateTime, isPinned=false, colorCode)
}

