package com.example.shopping_list

import android.app.AlertDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableTargetMarker
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class Shopping(val id:Int, var name : String,
                    var quantity:Int,
                    var isEditing: Boolean= false){
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun shoppinglist(modifier: Modifier){
    var Isitems by remember { mutableStateOf(listOf<Shopping>()) }
    var showdialogue by remember {mutableStateOf(false)}
    var itemname by remember {mutableStateOf("")}
    var quantity by remember {mutableStateOf("")}
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(top = 20.dp),

        verticalArrangement = Arrangement.Top
    ){
        Button(
            onClick = {showdialogue=true},
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Add Item")
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            items(Isitems) {
                item ->
                if(item.isEditing){
                    ShoppingEdit(item,{
                        editName, editQty -> Isitems = Isitems.map{it.copy(isEditing = false)}
                        val editedItem = Isitems.find { it.id == item.id }
                        editedItem?.let{
                            it.name = editName
                            it.quantity = editQty
                        }
                    })
                }
                else{
                    ShoppingList(item, onEditClick = {Isitems= Isitems.map { it.copy(isEditing = it.id == item.id) }},
                        onDeleteClick ={ Isitems = Isitems - item}
                    )
                }
            }

        }

    }

    if(showdialogue){
        AlertDialog({showdialogue=false},{
            Row (modifier= Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween){
                Button({
                    if(itemname.isNotBlank()){
                        val newItem = Shopping(
                            id = Isitems.size +1,
                            name = itemname,
                            quantity = quantity.toInt()
                        )
                        Isitems = Isitems + newItem
                        showdialogue= false
                        itemname = ""
                        quantity = ""
                    }

                }) {
                    Text("Add")
                }
                Button({
                    showdialogue= false

                }) {
                    Text("Cancel")
                }

            }
        },
            title ={Text("Add Shopping Item")},
            text ={
                Column {
                    OutlinedTextField(itemname, {itemname=it},
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    )
                    OutlinedTextField(quantity, {quantity=it},
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    )
                }

            }
        )

    }

}

@Composable
fun ShoppingList(
    item : Shopping,
    onEditClick:() -> Unit ,
    onDeleteClick:() -> Unit
){
    Row (
        modifier = Modifier.fillMaxWidth().padding(8.dp).border(
            border = BorderStroke(8.dp, Color.Blue,),
            shape= RoundedCornerShape(20),
        ),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(text= item.name, modifier = Modifier.padding(8.dp))
        Text(text= " Qty :${item.quantity}", modifier = Modifier.padding(8.dp))
        Row (modifier = Modifier.padding(8.dp)){
            IconButton(onEditClick) {
                Icon(Icons.Default.Edit,null)
            }
            IconButton(onDeleteClick) {
                Icon(Icons.Default.Delete,null)
            }

        }


    }

}

@Composable
fun ShoppingEdit(item: Shopping, onEditComplete:(String,Int)-> Unit){
    var editName by remember { mutableStateOf(item.name) }
    var editQty by remember { mutableStateOf(item.quantity.toString()) }
    var Isedition by remember {mutableStateOf(item.isEditing)}
    Row (modifier = Modifier.fillMaxWidth().background(Color.White).padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    )
    {
        Column {
            BasicTextField(
                value= editName,
                onValueChange = {editName=it},
                singleLine = true,
                modifier = Modifier.wrapContentSize().padding(8.dp)
            )
            BasicTextField(
                value= editQty,
                onValueChange = {editQty=it},
                singleLine = true,
                modifier = Modifier.wrapContentSize().padding(8.dp)
            )
        }
        Button(
            {
                Isedition= false
                onEditComplete(editName,editQty.toIntOrNull()?:1)
            }
        ) {
            Text("Save")
        }
    }

}