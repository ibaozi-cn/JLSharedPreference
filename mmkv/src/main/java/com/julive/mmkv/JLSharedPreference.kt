package com.julive.mmkv

import android.content.Context
import android.content.SharedPreferences
import com.tencent.mmkv.MMKV
import java.lang.NullPointerException


class JLSharedPreference : JLSPInterface {

    private val defaultSP: MMKV by lazy {
        MMKV.defaultMMKV()
    }

    private var sharedPreferences: SharedPreferences

    constructor(sharedPreferences: SharedPreferences) {
        this.sharedPreferences = sharedPreferences
    }

    constructor() {
        this.sharedPreferences = defaultSP
    }

    companion object {

        @JvmStatic
        fun initWithContext(context: Context) {
            MMKV.initialize(context)
        }

        @JvmStatic
        fun initWithRootDir(rootDir: String) {
            MMKV.initialize(rootDir)
        }

        @JvmStatic
        fun defaultSp() = JLSharedPreference()

        @JvmStatic
        fun newSpWithId(spId: String) = JLSharedPreference(MMKV.mmkvWithID(spId))

        @JvmStatic
        fun newMultiProcessSp(spId: String) = JLSharedPreference(MMKV.mmkvWithID(spId,MMKV.MULTI_PROCESS_MODE))
    }

    override fun contains(key: String?): Boolean {
        return sharedPreferences.contains(key)
    }

    override fun getBoolean(key: String?, defValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defValue)
    }

    override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        return sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    override fun getInt(key: String?, defValue: Int): Int {
        return sharedPreferences.getInt(key, defValue)
    }

    override fun getAll(): MutableMap<String, *> {
        return mutableMapOf<String,Any>()
    }

    fun allKeys(): Array<String>{
         val mmkv = sharedPreferences as? MMKV ?: throw NullPointerException("目前只支持MMKV提供的allKeys")
         return mmkv.allKeys()?: arrayOf()
    }

    override fun edit(): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }

    override fun getLong(key: String?, defValue: Long): Long {
        return sharedPreferences.getLong(key, defValue)
    }

    override fun getFloat(key: String?, defValue: Float): Float {
        return sharedPreferences.getFloat(key, defValue)
    }

    override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String>? {
        return sharedPreferences.getStringSet(key, defValues)
    }

    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        return sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun getString(key: String?, defValue: String?): String? {
        return sharedPreferences.getString(key, defValue)
    }

    override fun clear(): SharedPreferences.Editor {
        return sharedPreferences.edit().clear()
    }

    override fun putLong(key: String?, value: Long): SharedPreferences.Editor {
        return sharedPreferences.edit().putLong(key, value)
    }

    override fun putInt(key: String?, value: Int): SharedPreferences.Editor {
        return sharedPreferences.edit().putInt(key, value)
    }

    override fun remove(key: String?): SharedPreferences.Editor {
        return sharedPreferences.edit().remove(key)
    }

    override fun putBoolean(key: String?, value: Boolean): SharedPreferences.Editor {
        return sharedPreferences.edit().putBoolean(key, value)
    }

    override fun putStringSet(key: String?, values: MutableSet<String>?): SharedPreferences.Editor {
        return sharedPreferences.edit().putStringSet(key, values)
    }

    override fun commit(): Boolean {
        return sharedPreferences.edit().commit()
    }

    override fun putFloat(key: String?, value: Float): SharedPreferences.Editor {
        return sharedPreferences.edit().putFloat(key, value)
    }

    override fun apply() {
        return sharedPreferences.edit().apply()
    }

    override fun putString(key: String?, value: String?): SharedPreferences.Editor {
        return sharedPreferences.edit().putString(key, value)
    }

    fun importFromSharedPreferences(preferences: SharedPreferences): Int {
        val mmkv = sharedPreferences as? MMKV ?: throw NullPointerException("目前只支持MMKV迁移")
        return mmkv.importFromSharedPreferences(preferences)
    }
}