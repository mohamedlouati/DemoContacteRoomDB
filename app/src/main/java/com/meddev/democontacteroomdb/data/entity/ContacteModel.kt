package com.meddev.democontacteroomdb.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
/**
 * "datacontacte" c'est le nom de la table dans la base de données
 * @author Mohamed Louati
 * */
@Entity(tableName = "datacontacte")
data class ContacteModel(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val firstname: String,
    val lastname: String,
    val phone :String

)
