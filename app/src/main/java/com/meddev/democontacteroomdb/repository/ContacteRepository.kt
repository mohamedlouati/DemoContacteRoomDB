package com.meddev.democontacteroomdb.repository

import androidx.lifecycle.LiveData
import com.meddev.democontacteroomdb.data.dao.ContacteDao
import com.meddev.democontacteroomdb.data.entity.ContacteModel


class ContacteRepository (private val contacteDao: ContacteDao){



    val allItems: LiveData<List<ContacteModel>> = contacteDao.getall()

    //importer les données enregistrer dans la base des données
    fun getall(): LiveData<List<ContacteModel>> {
        return contacteDao.getall()
    }

   // fonction appelle la requette insertion dans le dao
    suspend fun insertContacte(contacteModel: ContacteModel) {
        contacteDao.insertContacte(contacteModel)
    }
}