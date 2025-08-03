package com.example.dailyquiz.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.dailyquiz.models.QuizHistoryEntry

open class HistoryViewModel : ViewModel() {
    private val _entries = mutableStateListOf<QuizHistoryEntry>()
    val entries: List<QuizHistoryEntry> get() = _entries

    fun loadEntries(initialEntries: List<QuizHistoryEntry>) {
        _entries.clear()
        _entries.addAll(initialEntries)
    }


    fun deleteEntry(entry: QuizHistoryEntry) {
        _entries.remove(entry)
    }
}
