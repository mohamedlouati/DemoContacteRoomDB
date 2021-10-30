package com.meddev.democontacteroomdb.ui.list

import android.Manifest.permission.READ_CONTACTS
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.meddev.democontacteroomdb.R
import com.meddev.democontacteroomdb.data.database.ContacteDB
import com.meddev.democontacteroomdb.data.entity.ContacteModel
import com.meddev.democontacteroomdb.viewmodels.ContacteViewModel
import kotlinx.android.synthetic.main.activity_contacte_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent
import android.net.Uri
import android.provider.Settings


class ContacteListActivity : AppCompatActivity() {

    //declarations pour l'adpter , base de données et viewModel

    private lateinit var instance: ContacteDB
    private lateinit var customAdapter: CustomAdapter
    private lateinit var contacteViewModel: ContacteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacte_list)

        //instance de base de données et viewModel
        instance = ContacteDB.getDatabase(this@ContacteListActivity)
        contacteViewModel = ViewModelProvider(this).get(ContacteViewModel::class.java)


        // edittext appliquer le filtre de l'adapter
        etSearch.addTextChangedListener { charSequence ->
            try {
                customAdapter.filter.filter(charSequence)
            } catch (error: Throwable) {
                Log.e("error", error.message.toString())
            }
        }

        //afficher la liste de contacte
        contacteViewModel.allItems.observe(this, Observer { items ->
            items?.let {
                if (it.isNotEmpty()) {
                    customAdapter = CustomAdapter(this@ContacteListActivity, it)
                    val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
                        this@ContacteListActivity,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    lvData.layoutManager = mLayoutManager
                    lvData.adapter = customAdapter
                }
            }
        })

        //si la permission n'est pas garantie alors l'app demande la permission, sinon l'app importe les contacts
        requestPermission()

    }

    //importer les contactes
    private fun importContacts() {
        val arrayList = ArrayList<ContacteModel>()
        val readContactsUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val cursor = contentResolver.query(readContactsUri, null, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val idIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
                val id = cursor.getInt(idIndex)
                val displayNameIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val userDisplayName = cursor.getString(displayNameIndex)
                val phoneNumberIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val phoneNumber = cursor.getString(phoneNumberIndex)
                val contact = ContacteModel(id.toLong(), userDisplayName, "prenom", phoneNumber)
                arrayList.add(contact)
            }
            cursor.close()
            saveContacts(arrayList)
        }
    }

    //sovgarder les contactes dans notre room database
    private fun saveContacts(arrayList: ArrayList<ContacteModel>) {
        if (arrayList.isNotEmpty()) {
            for (contateModel in arrayList) {
                CoroutineScope(Dispatchers.Main).launch {
                    contacteViewModel.insert(contateModel)
                }
            }
        }

    }

    //lire la permission pour importer les contacts
    private fun requestPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(READ_CONTACTS), 1)
        } else {
            importContacts()
        }
    }

    //get request read contacts permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1
            && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            importContacts()
        } else {
            //demonder la permission dans une alertDialog
            val alertDialog = AlertDialog.Builder(this@ContacteListActivity)
            alertDialog.setTitle("Contacts Permissions")
            alertDialog.setMessage("please give permissions")
            alertDialog.setNegativeButton("not now") { dialog, which ->
                alertDialog.setCancelable(true)
            }
            alertDialog.setPositiveButton("ok") { dialog, which ->
                val i = Intent()
                i.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                i.addCategory(Intent.CATEGORY_DEFAULT)
                i.data = Uri.parse("package:" + getPackageName())
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                startActivity(i)
            }
            alertDialog.show()
        }
    }


}