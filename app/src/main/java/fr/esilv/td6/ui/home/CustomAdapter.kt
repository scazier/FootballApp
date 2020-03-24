package fr.esilv.td6.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.esilv.td6.R
/*
class CustomAdapter(val itemList: List<SearchItem>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_layout, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CustomAdapter.ViewHolder, position: Int) {
        holder.bindItems(itemList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return itemList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(item: SearchItem) {
            val snippet: Snippet = item.snippet
            val textViewName = itemView.findViewById<TextView>(R.id.textViewTitle)
            val textViewDesc  = itemView.findViewById<TextView>(R.id.textViewDescription)
            val imageViewThumbnail = itemView.findViewById<ImageView>(R.id.imageThumbnail)
            textViewName.text = snippet.title
            textViewDesc.text = snippet.description
            Glide.with(itemView).load(snippet.thumbnails.default.url).into(imageViewThumbnail)

            itemView.setOnClickListener(View.OnClickListener {
                fun onClick(v: View){
                    //DetailActivity().start(v.getContext(), item.id.videoId)

                }
            })

        }
    }
}

 */