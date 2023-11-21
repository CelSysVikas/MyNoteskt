package me.vikas.mynoteskt.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import me.vikas.mynoteskt.Interface.ItemNoteClickEvents
import me.vikas.mynoteskt.Model.Notes
import me.vikas.mynoteskt.R
import me.vikas.mynoteskt.databinding.ItemNoteBinding

class NotesAdapter(
    private var context: Context,
    private var listner: ItemNoteClickEvents,
    private var list: List<Notes>
) : RecyclerView.Adapter<NotesAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(ItemNoteBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        holder.noteBinding.setNoteTitle(list[position].title)
        holder.noteBinding.setNoteContent(list[position].content)
        holder.noteBinding.setNoteDate(list[position].dateTime)

        if (list[position].colorCode != 0)
            holder.noteBinding.llColorCode.setBackgroundColor(context.resources.getColor(list[position].colorCode))
        if (list[position].isPinned)
            holder.noteBinding.parent.setBackgroundColor(context.getColor(R.color.notePinned))
    }


    inner class viewHolder(itemView: ItemNoteBinding) : ViewHolder(itemView.root) {
        val noteBinding: ItemNoteBinding = itemView

        init {
            itemView.noteDataView.setOnClickListener {
                listner.onItemClick(adapterPosition, list[adapterPosition].id)
            }
            itemView.tvMore.setOnClickListener {
                listner.onMoreTextClick(adapterPosition,itemView.moreOptionsLayout,itemView.tvMore)
            }
            itemView.deleteNote.setOnClickListener {
                listner.onDeleteBtnClick(adapterPosition, list[adapterPosition])
            }
            itemView.ibCopyNote.setOnClickListener {
                listner.onCopyBtnClick(adapterPosition, list[adapterPosition])
            }
            itemView.ibEditNote.setOnClickListener {
                listner.onEditBtnClick(adapterPosition, list[adapterPosition])
            }
            itemView.ibPinNote.setOnClickListener {
                listner.onPinBtnClick(adapterPosition, list[adapterPosition])
            }
        }
    }
}