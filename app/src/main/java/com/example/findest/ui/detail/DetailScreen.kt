package com.example.findest.ui.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.findest.R
import com.example.findest.data.model.Product
import com.example.findest.ui.component.EmptyState
import com.example.findest.ui.component.ErrorState
import com.example.findest.ui.component.LoadingState
import com.example.findest.ui.home.ProductViewModel
import com.example.findest.ui.theme.FindestTheme
import com.example.findest.utils.UiState
import com.example.findest.utils.dummyProductsInCart

@Composable
fun DetailScreen(
    viewModel: ProductViewModel = hiltViewModel(),
    productId: Int
) {
    val productState by viewModel.detail.collectAsStateWithLifecycle()
    var quantity by remember { mutableIntStateOf(1) }
    LaunchedEffect(productId) {
        viewModel.getDetailProduct(productId)
    }

    when (val state = productState) {
        is UiState.Loading -> LoadingState()

        is UiState.Empty -> EmptyState()

        is UiState.Error -> ErrorState(message = state.message)

        is UiState.Success -> DetailContent(
            product = state.data,
            quantity = quantity,
            onQuantityChange = { quantity = it },
            onAddToCart = {
                viewModel.addToCart(product = state.data, quantity = quantity)
            }
        )
    }
}

@Composable
fun DetailContent(
    product: Product,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    onAddToCart: () -> Unit
) {
    val discountedPrice = product.price - (product.price * product.discount / 100)
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
                .padding(horizontal = 16.dp)
        ) {
            item {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(product.imageUrl)
                        .build(),
                    placeholder = painterResource(R.drawable.placeholder),
                    error = painterResource(R.drawable.error),
                    contentDescription = product.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            item {
                Text(
                    text = product.categoryName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$ $discountedPrice",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    if (product.discount > 0) {
                        Text(
                            text = "$ ${product.price}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier
                        )
                        Text(
                            text = " (${product.discount}% OFF)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                Text(
                    text = "Description:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            item {
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            IconButton(
                onClick = {
                    if (quantity > 1) onQuantityChange(quantity - 1)
                },
                colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Cyan)
            ) {
                Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease")
            }

            Text(text = quantity.toString(), style = MaterialTheme.typography.titleMedium)

            IconButton(
                onClick = { onQuantityChange(quantity + 1) },
                colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Cyan)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Increase")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = onAddToCart,
                modifier = Modifier
                    .height(50.dp)
                    .weight(1f)
            ) {
                Text(text = "Add to Cart")
            }
        }
    }
}


@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
private fun DetailScreenPreview() {
    val dummy = dummyProductsInCart[1]
    FindestTheme {
        DetailContent(
            product = dummy.product,
            quantity = dummy.quantity,
            onQuantityChange = {},
            onAddToCart = {}
        )
    }
}