package com.example.musicservice.dialogs

import android.content.Context

class DialogController constructor(serviceInjection: DialogInterface) : DialogInterface {
    private val service: DialogInterface = serviceInjection

    override fun createDialogMusicArtist(context: Context, id: Int) {
        service.createDialogMusicArtist(context, id)
    }

    override fun createDialogPlaylistOrAlbum(context: Context, id: Int, isAlbum: Boolean) {
        service.createDialogPlaylistOrAlbum(context, id, isAlbum)
    }

    override fun createDialogAddToPlaylist(context: Context, id: Int) {
        service.createDialogAddToPlaylist(context, id)
    }

}