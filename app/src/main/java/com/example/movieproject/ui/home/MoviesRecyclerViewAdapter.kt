package com.example.movieproject.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieproject.R
import com.example.movieproject.network.entity.MovieEntity
import javax.inject.Inject


class MoviesRecyclerViewAdapter @Inject constructor(
    private val mContext: Context,
    fragment: HomeFragment
) : RecyclerView.Adapter<MoviesRecyclerViewAdapter.ViewHolder>() {
    val TAG = "MoviesRecyclerView"
    var mResponseList: List<MovieEntity> = ArrayList()
    private val mMovieClickListener: MovieClickListener

    fun setData(responseList: List<MovieEntity>) {
        val oldList = mResponseList

        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            DataDiffUtilCallBack(oldList, responseList)
        )
        mResponseList = responseList
        diffResult.dispatchUpdatesTo(this)
    }

    init {
        mMovieClickListener = fragment
    }


    class DataDiffUtilCallBack(
        private val oldList: List<MovieEntity>,
        private val newList: List<MovieEntity>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].equals(newList[newItemPosition].id)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext)
                .inflate(R.layout.row_main_grid, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val listNote = mResponseList[position]
        val url = "https://image.tmdb.org/t/p/w300${listNote.imagePath}"
        holder.textViewTitle!!.text = listNote.title
        Glide.with(mContext).load(url).into(holder.imageViewPoster!!);
        holder.itemView.setOnClickListener {
            mMovieClickListener.onNoteClickListener(
                listNote,
                position
            )
        }
    }

    override fun getItemCount(): Int {
        return if (mResponseList.isNullOrEmpty()) 0 else mResponseList.size
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var textViewTitle: TextView? = null
        var imageViewPoster: ImageView? = null

        private fun init() {
            textViewTitle = itemView.findViewById(R.id.textViewTitle)
            imageViewPoster = itemView.findViewById(R.id.imageViewPoster)
        }

        init {
            init()
        }
    }
}

