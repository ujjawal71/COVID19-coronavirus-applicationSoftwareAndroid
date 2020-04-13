package com.csecoder.covid19.secondscreencode.mainscreencode.maincode.fetchdataallcountry

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.csecoder.covid19.R
import com.csecoder.covid19.data.database.model.CoronaEntity
import kotlinx.android.synthetic.main.item_country_second.view.*
import java.util.*

class CoronaRecyclerViewAdapter(
    private val context: Context
) :
    RecyclerView.Adapter<CoronaRecyclerViewAdapter.CardViewViewHolder>(), Filterable {

    private var coronaData: MutableList<CoronaEntity> = mutableListOf()

    var coronaDataFilterList: MutableList<CoronaEntity> = mutableListOf()

    init {
        coronaDataFilterList = coronaData
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()

                coronaDataFilterList = if (charSearch.isEmpty()) {
                    coronaData
                } else {
                    val resultList: MutableList<CoronaEntity> = mutableListOf()
                    for (row in coronaData) {
                        if (row.info.country?.toLowerCase(Locale.ROOT)!!.contains(
                                charSearch.toLowerCase(
                                    Locale.ROOT
                                )
                            )) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = coronaDataFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                coronaDataFilterList = results?.values as MutableList<CoronaEntity>
                notifyDataSetChanged()
            }

        }
    }

    fun setEntity(entryList: List<CoronaEntity>) {
        coronaData.clear()
        coronaData.addAll(entryList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CardViewViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_country_second, viewGroup, false)
        return CardViewViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewViewHolder, position: Int) {
        holder.bind(coronaDataFilterList[position])
    }

    override fun getItemCount(): Int = coronaDataFilterList.size

    inner class CardViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(entry: CoronaEntity) {
            try {
                itemView.tvTitle.text = entry.info.country
                itemView.tvCases.text = entry.info.case_confirms.toString()
                itemView.tvDeathCase.text = entry.info.case_deaths.toString()
                itemView.tvRecoverCase.text = entry.info.case_recovered.toString()
            } catch (e: Exception) {

            }
        }
    }
}
