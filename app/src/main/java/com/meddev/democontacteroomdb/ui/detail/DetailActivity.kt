package com.meddev.democontacteroomdb.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.meddev.democontacteroomdb.R
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val data : Intent? = intent
        val firstName = data!!.getStringExtra("firstname")
        val lastName = data.getStringExtra("lastname")
        val phone = data.getStringExtra("phone")

        Log.e("firstName", firstName!!)
        Log.e("lastName", lastName!!)
        Log.e("phone", phone!!)

        tvnom.text=firstName
        tvprenom.text=lastName
        tvphone.text=phone


    }
}