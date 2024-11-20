package com.example.calendary

import android.app.Application
import androidx.lifecycle.lifecycleScope

/**
 * Класс, который запускается до запуска всех Активностей. Нуждается в регистрвации в Манифесте
 */
class MyApplication : Application() {

    // Экземпляр базы данных, доступный в приложении
    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        // Инициализируем базу данных при старте приложения
        database = AppDatabase.getDatabase(this)
    }
}