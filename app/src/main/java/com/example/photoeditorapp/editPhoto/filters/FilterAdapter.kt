package com.example.photoeditorapp.editPhoto.filters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.photoeditorapp.R
import ja.burhanrashid52.photoeditor.PhotoFilter

class FilterAdapter(private val clickListener: (PhotoFilter) -> Unit) :
    RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {

    private val filterList = listOf(
        FilterModel(PhotoFilter.AUTO_FIX, R.string.auto_fix , R.drawable.auto_fix),
        FilterModel(PhotoFilter.BLACK_WHITE, R.string.black_white , R.drawable.b_n_w)
    )


    class FilterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val filterItem: View = view.findViewById(R.id.filter_item)
        val filterTitle: TextView = view.findViewById(R.id.filter_title)
        val filterImage: ImageView = view.findViewById(R.id.filter_image)

    }
    override fun getItemCount(): Int {
        return filterList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.filter_item, parent, false)
        return FilterViewHolder(layout)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        val item = filterList[position]
        holder.filterTitle.text = holder.filterItem.context.getString(item.title)
        holder.filterImage.setBackgroundResource(item.image)

        holder.filterItem.setOnClickListener { clickListener(item.filter) }
    }
}
