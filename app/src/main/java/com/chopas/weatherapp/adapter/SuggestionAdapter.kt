package com.chopas.weatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.model.AdapterClass
import androidx.recyclerview.widget.RecyclerView
import com.chopas.weatherapp.R
import com.chopas.weatherapp.model.SuggestionData

class SuggestionAdapter(private val suggestionList: ArrayList<SuggestionData>): RecyclerView.Adapter<SuggestionAdapter.ViewHolderClass>() {

    var onItemClick: ((SuggestionData) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.suggestion_item_layout, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return suggestionList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = suggestionList[position]

        holder.suggestionTextView.text = currentItem.suggestionString
        if (currentItem.suggestionImage != null) {
            holder.suggestionImage.setImageResource(currentItem.suggestionImage!!)
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currentItem)
        }
    }

    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView) {
        val suggestionImage: ImageView = itemView.findViewById(R.id.suggestion_image_view)
        val suggestionTextView: TextView = itemView.findViewById(R.id.suggestion_text_view)
    }
}