package com.example.contacts

import android.annotation.SuppressLint
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import android.widget.AdapterView
import android.widget.CursorAdapter
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import java.lang.IllegalStateException

@SuppressLint("InlineApi")
private val FROM_COLUMNS: Array<String> = arrayOf(
    if((Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)) {
        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
    }else{
        ContactsContract.Contacts.DISPLAY_NAME
    }
)

@SuppressLint("InlineApi")
private val PROJECTION : Array<out String> = arrayOf(
    ContactsContract.Contacts._ID,
    ContactsContract.Contacts.LOOKUP_KEY,
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
    else
        ContactsContract.Contacts.DISPLAY_NAME
)

@SuppressLint("InlinedApi")
private val SELECTION: String =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} LIKE ?"
    else
        "${ContactsContract.Contacts.DISPLAY_NAME} LIKE ?"

private val searchString : String = TODO()
private val selectionArgs = arrayOf<String>(searchString)



private const val CONTACT_ID_INDEX : Int = 0
private const val CONTACT_KEY_INDEX : Int = 1

private val TO_IDS: IntArray = intArrayOf(android.R.id.text1)

class ContactsFragment : Fragment(),LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener{

    lateinit var contactsList: ListView
    var contactId : Long = 0
    var contactKey : String? = null
    var contactUri : Uri? = null

    private var cursorAdapter: SimpleCursorAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container : ViewGroup?, savedInstanceState: Bundle?
    ) : View?{
        return inflater.inflate(R.layout.contact_list_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.also {
            contactsList = it.findViewById(android.R.id.text1)
            cursorAdapter = SimpleCursorAdapter(
                it, R.layout.contacts_list_item, null, FROM_COLUMNS, TO_IDS, 0
            )
            contactsList.adapter = cursorAdapter
        }

        contactsList.onItemClickListener = this

        loaderManager.initLoader(0,null, this)
    }

    override fun <D : Any?> initLoader(
        id: Int,
        args: Bundle?,
        callback: LoaderManager.LoaderCallbacks<D>
    ): Loader<D> {
        TODO("Not yet implemented")
    }

    override fun <D : Any?> restartLoader(
        id: Int,
        args: Bundle?,
        callback: LoaderManager.LoaderCallbacks<D>
    ): Loader<D> {
        TODO("Not yet implemented")
    }

    override fun destroyLoader(id: Int) {
        TODO("Not yet implemented")
    }

    override fun <D : Any?> getLoader(id: Int): Loader<D>? {
        TODO("Not yet implemented")
    }

    override fun markForRedelivery() {
        TODO("Not yet implemented")
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {

        selectionArgs[0] = "%$searchString%"

        return activity?.let {
            return CursorLoader(it,ContactsContract.Contacts.CONTENT_URI, PROJECTION, SELECTION, selectionArgs, null)
        } ?: throw IllegalStateException()
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        TODO("Not yet implemented")
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val cursor : Cursor? = (parent?.adapter as? CursorAdapter)?.cursor?.apply {
            moveToPosition(position)
            contactId = getLong(CONTACT_ID_INDEX)
            contactKey = getString(CONTACT_KEY_INDEX)
            contactUri = ContactsContract.Contacts.getLookupUri(contactId, contactKey)
        }
    }
}