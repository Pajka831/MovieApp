package com.mpaja.movieapp.ui.movielist.adapter

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import com.mpaja.movieapp.R

class AutoCompleteAdapter(context: Context) :
    ArrayAdapter<String>(context, R.layout.textview_search_dropdown) {

    val items = arrayListOf<String>()

    fun updateItems(stringList: List<String>) {
        clear()
        addAll(stringList)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return NoFilter()
    }

    inner class NoFilter : Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val result = FilterResults()
            result.values = items
            result.count = items.size
            return result
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            notifyDataSetChanged()
        }

    }
}