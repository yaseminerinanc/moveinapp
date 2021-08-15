package com.example.moveinapp.view.movie_list

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moveinapp.R
import com.example.moveinapp.data.repository.NetworkState
import com.example.moveinapp.data.value_object.Result
import com.example.moveinapp.view.movie_details.DetaisMovie
import kotlinx.android.synthetic.main.movie_list_item.view.*
import kotlinx.android.synthetic.main.network_state_item.view.*

class MovieListAdapter (public val context: Context):PagedListAdapter<Result,RecyclerView.ViewHolder>(MovieDiffCallBack()) {

    private var networkState: NetworkState? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (getItemViewType(position) == 1) {
            (holder as MovieItemViewHolder).bind(getItem(position), context)
        } else {
            (holder as NetworkStateViewHolder).bind(networkState)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        if (viewType == 1) {
            view = layoutInflater.inflate(R.layout.movie_list_item, parent, false)
            return MovieItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            return NetworkStateViewHolder(view)
        }
    }

    class MovieDiffCallBack : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }

    class MovieItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(result: Result?, context: Context) {
            itemView.cv_mv_title.text = result?.title
            itemView.cv_mv_release_date.text = result?.releaseDate

            val posterUrl = "https://image.tmdb.org/t/p/w500" + result?.posterPath

            Glide.with(itemView.context).load(posterUrl).into(itemView.cv_mv_poster)

            itemView.setOnClickListener {
                val intent = Intent(context, DetaisMovie::class.java)
                intent.putExtra("id", result?.id)
                context.startActivity(intent)

            }
        }

    }

    class NetworkStateViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(networkState: NetworkState?) {
            if (networkState != null && networkState == NetworkState.Loading) {
                itemView.error_pb_item.visibility = View.VISIBLE;
            } else {
                itemView.error_pb_item.visibility = View.GONE;
            }

            if (networkState != null && networkState == NetworkState.Error) {
                itemView.error_msg_item.visibility = View.VISIBLE;
                itemView.error_msg_item.text = networkState.message;
            } else if (networkState != null && networkState == NetworkState.EndOfList) {
                itemView.error_msg_item.visibility = View.VISIBLE;
                itemView.error_msg_item.text = networkState.message;
            } else {
                itemView.error_msg_item.visibility = View.GONE;
            }
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.Loading
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            2
        } else {
            1
        }
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }

    }

}