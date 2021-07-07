package com.example.tokenlab.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tokenlab.R

class GenderListAdapter(private var genderList: List<String>) :
    RecyclerView.Adapter<GenderListAdapter.GenderListViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GenderListViewHolder {
        return GenderListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_gender,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: GenderListViewHolder, position: Int) {
        holder.bind(genderList[position])
    }

    override fun getItemCount(): Int {
        return genderList.size
    }

    inner class GenderListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var itemGenderTextView: TextView = itemView.findViewById(R.id.itemGenderTextView)

        fun bind(genderList: String) {
            itemGenderTextView.text = genderList
        }
    }
}