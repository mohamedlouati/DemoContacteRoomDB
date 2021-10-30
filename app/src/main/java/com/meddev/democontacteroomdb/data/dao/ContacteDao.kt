package com.meddev.democontacteroomdb.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.meddev.democontacteroomdb.data.entity.ContacteModel

@Dao
interface ContacteDao {

    //une fonction qui fait la requette sql insert pour insérer les données dans le tableau
    //OnConflictStrategy.REPLACE ---- pour remplacer les données
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContacte(contacteModel: ContacteModel)



    //une fonction pour selecter les données
    @Query("SELECT * FROM datacontacte")
    fun getall(): LiveData<List<ContacteModel>>

}