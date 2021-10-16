package com.restoo.machinetest.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.restoo.machinetest.Apod
import com.restoo.machinetest.Listner.ApodItemListner
import com.restoo.machinetest.R
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*

class ListAdapter(var apodlist: List<Apod>, var context: Context, listner: ApodItemListner) :
    RecyclerView.Adapter<ListAdapter.ListViewHolder>() {
    var mListner: ApodItemListner = listner

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

         var mImgProfile: CircleImageView
         var mTxtTopic: TextView
         var mTxtDate: TextView
         var mLayout: RelativeLayout

        init {
            mImgProfile = itemView.findViewById(R.id.img_profile)
            mTxtTopic = itemView.findViewById(R.id.txt_topic)
            mTxtDate = itemView.findViewById(R.id.txt_date)
            mLayout = itemView.findViewById(R.id.relative_item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.layout_item,
                parent,
                false
            )

        return ListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        Glide.with(context)
            .load(apodlist.get(position).url)
            .into(holder.mImgProfile);
        var calendar: Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-mm-dd")
        val dateformat1 = SimpleDateFormat("MMM dd, yyyy")
        val parsedDate: Date = dateFormat.parse(apodlist.get(position).date)

        val formatted: String = dateformat1.format(parsedDate)
//
        holder.mTxtTopic.setText(apodlist.get(position).title)
        holder.mTxtDate.setText("Date : " + formatted)

        holder.mImgProfile.setOnClickListener(View.OnClickListener {
            onClickOperation(apodlist.get(position))
        })

        holder.mTxtTopic.setOnClickListener(View.OnClickListener {
            onClickOperation(apodlist.get(position))
        })

        holder.mTxtDate.setOnClickListener(View.OnClickListener {
            onClickOperation(apodlist.get(position))
        })
        holder.mLayout.setOnClickListener(View.OnClickListener {
            onClickOperation(apodlist.get(position))
        })
    }

    private fun onClickOperation(item: Apod) {
        mListner.onItemClick(item)

    }

    override fun getItemCount(): Int {
        return apodlist.size
    }
}