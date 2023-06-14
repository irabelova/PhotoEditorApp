package com.example.photoeditorapp.editPhoto.filters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.photoeditorapp.R

class FilterAdapter(private val filterList: List<FilterModel>, private val clickListener: (FilterModel) -> Unit) :
    RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {


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

        holder.filterItem.setOnClickListener { clickListener(item) }
    }
}
