package com.meddev.democontacteroomdb.ui.list

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.meddev.democontacteroomdb.R
import com.meddev.democontacteroomdb.data.entity.ContacteModel
import com.meddev.democontacteroomdb.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.contacte_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class CustomAdapter internal constructor(context: Context, items: List<ContacteModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    //liste filtrer
    var contacteFilterList = ArrayList<ContacteModel>()
    //liste contacte
    private var itemsList = emptyList<ContacteModel>().toMutableList()

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    //inistialisation des liste
    init {
        itemsList = items as ArrayList<ContacteModel>
        contacteFilterList = itemsList as ArrayList<ContacteModel>
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        (holder as ContacteViewHolder).bind(contacteFilterList.get(position))


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContacteViewHolder {
        val itemView = inflater.inflate(R.layout.contacte_item, parent, false)
        return ContacteViewHolder(itemView)
    }
    override fun getItemCount(): Int {
        return contacteFilterList.size
    }

    inner class ContacteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(contacteModel: ContacteModel)= with(itemView)  {
            nom.text = contacteModel.firstname
            prenom.text = contacteModel.lastname
            phone.text = contacteModel.phone

            itemView.setOnClickListener {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("firstname", contacteModel.firstname)
                intent.putExtra("lastname", contacteModel.lastname)
                intent.putExtra("phone", contacteModel.phone)
                context.startActivity(intent)
            }

        }

    }
  //filtre de recherche multiples
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                Log.e("start", "yes")
                if (charSearch.isEmpty()) {
                    contacteFilterList = itemsList as ArrayList<ContacteModel>
                } else {
                    val resultList = ArrayList<ContacteModel>()
                    for (row in itemsList) {
                        if (row.firstname.lowercase(Locale.getDefault()).contains(constraint.toString()
                                .lowercase(Locale.getDefault()))||
                            row.lastname.lowercase(Locale.getDefault()).contains(constraint.toString()
                                .lowercase(Locale.getDefault()))||
                            row.phone.contains(constraint.toString())) {
                            resultList.add(row)
                        }
                    }
                    contacteFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = contacteFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                contacteFilterList = results?.values as ArrayList<ContacteModel>
                notifyDataSetChanged()
            }
        }
    }

}