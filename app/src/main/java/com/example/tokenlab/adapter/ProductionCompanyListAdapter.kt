package com.example.tokenlab.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tokenlab.R
import com.example.tokenlab.domains.movie.details.production.company.ProductionCompany

class ProductionCompanyListAdapter(var productionCompanyList: List<ProductionCompany>) :
    RecyclerView.Adapter<ProductionCompanyListAdapter.ProductionCompanyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductionCompanyViewHolder {
        return ProductionCompanyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_production_company,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ProductionCompanyViewHolder,
        position: Int
    ) {
        holder.bind(productionCompanyList[position])
    }

    override fun getItemCount(): Int {
        return productionCompanyList.size
    }

    inner class ProductionCompanyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var itemProductionCompanyNameTextView: TextView =
            itemView.findViewById(R.id.itemProductionCompanyNameTextView)
        private var itemProductionCompanyCountryTextView: TextView =
            itemView.findViewById(R.id.itemProductionCompanyCountryTextView)

        fun bind(productionCompany: ProductionCompany) {
            itemProductionCompanyNameTextView.text = productionCompany.name
            itemProductionCompanyCountryTextView.text = productionCompany.originCountry
        }
    }
}