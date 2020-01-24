package com.example.pixman.data.prefs

import android.content.SharedPreferences
import com.google.gson.Gson
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PixmanPreferences
@Inject constructor(private val sharedPreferences: SharedPreferences, private val gson: Gson) {

	fun save(key: String, o: Any) {
		sharedPreferences.edit()
				.putString(key, gson.toJson(o))
				.apply()
	}

	fun save(key: String, value: String) {
		sharedPreferences.edit()
				.putString(key, value)
				.apply()
	}

	fun save(key: String, value: Long) {
		sharedPreferences.edit()
				.putLong(key, value)
				.apply()
	}

	fun save(key: String, value: Int) {
		sharedPreferences.edit()
				.putInt(key, value)
				.apply()
	}

	fun save(key: String, value: Boolean) {
		sharedPreferences.edit()
				.putBoolean(key, value)
				.apply()
	}

	operator fun <T> get(key: String, type: Class<T>): T? {
		return gson.fromJson(getString(key), type)
	}

	operator fun <T> get(key: String, type: Type): T? {
		return gson.fromJson(getString(key), type)
	}

	fun getString(key: String): String? {
		return sharedPreferences.getString(key, null)
	}

	fun getString(key: String, defValue: String = ""): String {
		return sharedPreferences.getString(key, defValue) ?: defValue
	}

	fun getInt(key: String): Int {
		return sharedPreferences.getInt(key, 0)
	}

	fun getLong(key: String, defValue: Long = 0): Long {
		return sharedPreferences.getLong(key, defValue)
	}

	fun getBoolean(key: String): Boolean {
		return sharedPreferences.getBoolean(key, false)
	}

	fun isKeyShared(key: String): Boolean {
		return sharedPreferences.contains(key)
	}

	fun <T> getObject(key: String, listType: Type?): T? {
		return gson.fromJson(key, listType)
	}

	fun getAllKeys(): MutableMap<String, *>? {
		return sharedPreferences.all
	}

	fun clearData(key: String) {
		if (isKeyShared(key)) {
			sharedPreferences.edit()
					.remove(key)
					.apply()
		}
	}

	fun clearAllData() {
		sharedPreferences.edit()
				.clear()
				.apply()
	}
}