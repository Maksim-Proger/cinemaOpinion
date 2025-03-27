package com.pozmaxpav.cinemaopinion.data.listeners

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class FirebaseListenerHolder @Inject constructor(
    private val databaseReference: DatabaseReference
) {
    private val activeListeners = mutableMapOf<String, ValueEventListener>()

    fun addListener(key: String, listener: ValueEventListener) {
        removeListener(key)
        activeListeners[key] = listener
    }

    fun removeListener(key: String) {
        activeListeners[key]?.let {
            databaseReference.removeEventListener(it)
            activeListeners.remove(key)
        }
    }

    fun clearAll() {
        activeListeners.forEach { (_, listener) ->
            databaseReference.removeEventListener(listener)
        }
        activeListeners.clear()
    }
}