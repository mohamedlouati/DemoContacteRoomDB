package com.meddev.democontacteroomdb.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.meddev.democontacteroomdb.data.database.ContacteDB
import com.meddev.democontacteroomdb.data.entity.ContacteModel
import com.meddev.democontacteroomdb.repository.ContacteRepository
import kotlinx.coroutines.launch

class ContacteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ContacteRepository
    val allItems: LiveData<List<ContacteModel>>

    //inistialisation repository
    init {

        val dao = ContacteDB.getDatabase(application).contacteDao()
        repository = ContacteRepository(dao)
        allItems = repository.allItems
    }

    suspend fun insert(contacteModel: ContacteModel) = viewModelScope.launch {
        repository.insertContacte(contacteModel)
    }

    fun getAll() = viewModelScope.launch {
        repository.getall()
    }

}