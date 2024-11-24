package com.example.hayet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.hayet.ui.theme.HayetTheme
import android.content.ContentValues
import android.os.PersistableBundle
import android.provider.BaseColumns
import androidx.compose.material3.Button


class MainActivity6 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dbHelper= FeedReaderDbHelper(this)

        enableEdgeToEdge()
        setContent {
            HayetTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )

                    ButtonComposable(
                        text = "insert",
                        onClick = { insertDB(dbHelper)

                        }
                    )
                    ButtonComposable(text = "read"){ readDB(dbHelper) }
                    ButtonComposable(text = "delete"){ deleteDB(dbHelper) }
                    ButtonComposable(text = "update"){ updateDB(dbHelper) }


                }
            }
        }
    }
}

fun insertDB(dbHelper: FeedReaderDbHelper){
    // Gets the data repository in write mode
    val db = dbHelper.writableDatabase

// Create a new map of values, where column names are the keys
    val values = ContentValues().apply {
        put(FeedReaderDbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_PRICE, "10.0")
        put(FeedReaderDbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION, "description1")
    }

// Insert the new row, returning the primary key value of the new row
    val newRowId = db?.insert(FeedReaderDbHelper.FeedReaderContract.FeedEntry.TABLE_NAME, null, values)

    println("Inserted row id: $newRowId")

}
//Lire les informations d'une base de données



fun readDB(dbHelper: FeedReaderDbHelper) {
    val db = dbHelper.readableDatabase

// Define a projection that specifies which columns from the database
// you will actually use after this query.
    val projection = arrayOf(
        BaseColumns._ID,
        FeedReaderDbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_PRICE,
        FeedReaderDbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION
    )

// Filter results WHERE "title" = 'My Title'
    val selection = "${FeedReaderDbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_PRICE} = ?"
    val selectionArgs = arrayOf("10.0")

// How you want the results sorted in the resulting Cursor
    val sortOrder = "${FeedReaderDbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION} DESC"

    val cursor = db.query(
        FeedReaderDbHelper.FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
        projection ,           // The array of columns to return (pass null to get all)
        selection,            // The columns for the WHERE clause
        selectionArgs,          // The values for the WHERE clause
        null,                   // don't group the rows
        null,                   // don't filter by row groups
        sortOrder               // The sort order
    )
    val itemIds = mutableListOf<Long>()
    val itemTitles= mutableListOf<String>()
    with(cursor) {
        while (moveToNext()) {
            val itemId = getLong(getColumnIndexOrThrow(BaseColumns._ID))
            val itemTitle =getString(getColumnIndexOrThrow(FeedReaderDbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_PRICE))
            itemIds.add(itemId)
            itemTitles.add(itemTitle)
        }
    }
    cursor.close()
    println("Item IDs:$itemIds")
    println("Item Titles: $itemTitles")
}

//Supprimer des informations d'une base de données
fun deleteDB(dbHelper: FeedReaderDbHelper) {
    val db = dbHelper.writableDatabase

    // Define 'where' part of query.
    val selection = "${FeedReaderDbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_PRICE} LIKE ?"
    // Specify arguments in placeholder order.
    val selectionArgs = arrayOf("30.0")
    // Issue SQL statement.
    val deletedRows = db.delete(FeedReaderDbHelper.FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs)

    println("Deleted rows count: $deletedRows")
}


//Mettre à jour une base de données
fun updateDB(dbHelper: FeedReaderDbHelper){
    val db = dbHelper.writableDatabase

// New value for one column
    val title = "20.0"
    val values = ContentValues().apply {
        put(FeedReaderDbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_PRICE, title)
    }

// Which row to update, based on the title
    val selection = "${FeedReaderDbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_PRICE} LIKE ?"
    val selectionArgs = arrayOf("10.0")
    val count = db.update(
        FeedReaderDbHelper.FeedReaderContract.FeedEntry.TABLE_NAME,
        values,
        selection,
        selectionArgs)
}


@Composable
fun ButtonComposable(modifier: Modifier = Modifier,text:String,onClick:() -> Unit){
    Button(onClick = onClick) {
        Text(text = text)
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    HayetTheme {
        Greeting("Android")
    }
}