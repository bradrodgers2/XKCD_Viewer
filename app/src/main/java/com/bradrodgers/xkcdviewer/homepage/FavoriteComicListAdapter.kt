package com.bradrodgers.xkcdviewer.homepage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bradrodgers.xkcdviewer.R
import com.bradrodgers.xkcdviewer.domain.ComicInfo

class FavoriteComicListAdapter :
    RecyclerView.Adapter<FavoriteComicListAdapter.ViewHolder>() {

    var data = listOf<ComicInfo>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClick: ((comicInfo: ComicInfo) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.favorite_comic_cardview, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.favoriteComicTitle.text = data[position].title
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(data[position])
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val favoriteComicTitle: TextView = itemView.findViewById(R.id.favoriteComicTitle)
    }
}