package com.example.contacts

import android.annotation.SuppressLint
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
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

private const val CONTACT_ID_INDEX : Int = 0
private const val CONTACT_KEY_INDEX : Int = 1

private val TO_IDS: IntArray = intArrayOf(android.R.id.text1)

class ContactsFragment : Fragment(R.layout.contact_list_fragment), LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener{

    lateinit var contactsList: ListView
    var contactId : Long = 0
    var contactKey : String? = null
    var contactUri : Uri? = null

    private var cursorAdapter: SimpleCursorAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loaderManager.initLoader(0, null, this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.also {
            contactsList = it.findViewById(R.id.fragment_list_toadd) as ListView
            cursorAdapter = SimpleCursorAdapter(it, R.layout.contacts_list_item, null, FROM_COLUMNS, TO_IDS, 0)
            contactsList.adapter = cursorAdapter
        }
        contactsList.onItemClickListener = this
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {

        val searchString : String? = requireArguments().getString("searchName")
        val selectionArgs = arrayOf(searchString)

        Toast.makeText(activity, searchString, Toast.LENGTH_SHORT).show()
        Log.i("myatag", "searchString is : $searchString")
        selectionArgs[0] = "%$searchString%"

        return activity?.let {

            return CursorLoader(it,ContactsContract.Contacts.CONTENT_URI, PROJECTION, SELECTION, selectionArgs, null)
        } ?: throw IllegalStateException()
    }

    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor) {
        cursorAdapter?.swapCursor(cursor)

    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        cursorAdapter?.swapCursor(null)
    }

    override fun onItemClick(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        val cursor : Cursor? = (parent.adapter as? CursorAdapter)?.cursor?.apply {
            moveToPosition(position)
            contactId = getLong(CONTACT_ID_INDEX)
            contactKey = getString(CONTACT_KEY_INDEX)
            contactUri = ContactsContract.Contacts.getLookupUri(contactId, contactKey)
        }

        Log.i("myatag", "An item has been clicked")
    }
}