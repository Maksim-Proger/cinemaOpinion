package com.example.core.utils

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class FirebaseListenerHolder @Inject constructor() {

    private val activeListeners = mutableMapOf<String, Pair<DatabaseReference, ValueEventListener>>()

    fun addListener(key: String, ref: DatabaseReference, listener: ValueEventListener) {
        removeListener(key)
        activeListeners[key] = ref to listener
    }

    fun removeListener(key: String) {
        activeListeners[key]?.let { (ref, listener) ->
            ref.removeEventListener(listener)
        }
        activeListeners.remove(key)
    }

    fun clearAll() {
        activeListeners.forEach { (_, pair) ->
            val (ref, listener) = pair
            ref.removeEventListener(listener)
        }
        activeListeners.clear()
    }

}
