package com.example.prettynotes

data class NoteItem(val title: String, val description: String) {
    constructor(): this("", "")
}