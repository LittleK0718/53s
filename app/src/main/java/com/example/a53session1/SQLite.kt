package com.example.a53session1

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*


@Entity(tableName = "account_table")
data class Account(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usermail: String,
    val password: String
)

@Dao
interface AccountDao {
    @Query("SELECT * FROM account_table")
    fun getAllAccounts(): LiveData<List<Account>>

    @Insert
    suspend fun insertAccount(account: Account)

    @Query("SELECT EXISTS(SELECT 1 FROM account_table WHERE usermail = :usermail AND password = :password)")
    suspend fun isAccExists(usermail: String,password: String): Boolean

}

@Database(entities = [Account::class], version = 1)
abstract class AccountDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    companion object {
        @Volatile
        private var INSTANCE: AccountDatabase? = null

        fun getDatabase(context: Context): AccountDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AccountDatabase::class.java,
                    "account_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}