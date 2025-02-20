package top.yogiczy.mytv.core.data.utils

import androidx.collection.LruCache

class LruMutableCache<K : Any, V : Any>(maxSize: Int) : LruCache<K, V>(maxSize) {

    fun getOrPut(key: K, defaultValue: () -> V): V {
        return get(key) ?: defaultValue().also { newValue ->
            put(key, newValue)
        }
    }
}
