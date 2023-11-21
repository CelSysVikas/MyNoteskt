package me.vikas.mynoteskt.Interface

import android.widget.LinearLayout
import android.widget.TextView
import me.vikas.mynoteskt.Model.Notes

interface ItemNoteClickEvents {
    fun onItemClick(position: Int, noteId:Int)
    fun onMoreTextClick(position: Int, exposeLayout: LinearLayout, tvInfo:TextView)
    fun onPinBtnClick(position: Int,note: Notes)
    fun onCopyBtnClick(position: Int,note: Notes)
    fun onDeleteBtnClick(position: Int,note: Notes)
    fun onEditBtnClick(position: Int,note: Notes)
}