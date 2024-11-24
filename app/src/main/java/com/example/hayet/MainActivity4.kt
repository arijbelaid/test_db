package com.example.hayet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hayet.data.product
import com.example.hayet.ui.theme.HayetTheme

class MainActivity4 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HayetTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GreetingScreen(Modifier.padding(innerPadding))



                }
            }
        }
    }
}

@Composable
fun GreetingScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    // Liste des produits
    val productList = listOf(
        product(name = "Product 1", price = 10.0, description = "Description 1", imageUrl = R.drawable.product),
        product(name = "Product 2", price = 20.0, description = "Description 2", imageUrl = R.drawable.product),
        product(name = "Product 3", price = 30.0, description = "Description 3", imageUrl = R.drawable.product)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(productList) { product ->
                ProductCard(product)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bouton Déconnexion
        Button(onClick = {
            // Déconnecter l'utilisateur
            with(sharedPreferences.edit()) {
                putBoolean("isLoggedIn", false)
                apply()
            }

            // Retourner à l'écran de connexion (MainActivity)
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
            (context as ComponentActivity).finish() // Fermer MainActivity4
        }) {
            Text(text = "Logout") // Texte du bouton en anglais
        }
    }
}

@Composable
fun ProductCard(product: product) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                // Create an Intent to navigate to MainActivity5
                val intent = Intent(context, MainActivity5::class.java).apply {
                    putExtra("productName", product.name)
                    putExtra("productPrice", product.price)
                    putExtra("productDescription", product.description)
                    putExtra("productImage", product.imageUrl)
                }
                context.startActivity(intent)
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Nom du produit au-dessus de l'image
            Text(
                text = product.name,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Image du produit
            Image(
                painter = painterResource(id = product.imageUrl),
                contentDescription = product.name,
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Prix du produit
            Text(
                text = "${product.price} €",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HayetTheme {
        GreetingScreen()
    }
}
