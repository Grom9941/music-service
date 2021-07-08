package com.example.musicservice.dialogs

import android.content.Context

interface DialogInterface {
    fun createDialogMusicArtist(context: Context, id: Int)

    fun createDialogPlaylistOrAlbum(context: Context, id: Int, isAlbum: Boolean)

    fun createDialogAddToPlaylist(context: Context, id: Int)
}