package com.example.findest.ui.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.findest.model.ProductInCart
import com.example.findest.ui.component.CartItem
import com.example.findest.ui.component.ConfirmationDialog
import com.example.findest.ui.component.EmptyState
import com.example.findest.ui.component.ErrorState
import com.example.findest.ui.component.LoadingState
import com.example.findest.ui.theme.FindestTheme
import com.example.findest.utils.UiState
import com.example.findest.utils.dummyProductsInCart

@Composable
fun CartScreen(
    viewModel: CartViewModel = hiltViewModel(),
    navigateToDetail: (Int) -> Unit
) {
    val cartState by viewModel.cartItem.collectAsStateWithLifecycle()
    var showDialog by rememberSaveable { mutableStateOf(false) }

    when (val state = cartState) {
        is UiState.Loading -> LoadingState()
        is UiState.Empty -> EmptyState()
        is UiState.Error -> ErrorState(message = state.message)

        is UiState.Success -> {
            CartScreenContent(
                productsInCart = state.data,
                onCheckoutClick = { showDialog = true },
                onDecrease = { viewModel.decreaseQuantity(it) },
                onIncrease = { viewModel.increaseQuantity(it) },
                navigateToDetail = { productId -> navigateToDetail(productId) }
            )
        }
    }

    if (showDialog) {
        ConfirmationDialog(
            title = "Konfirmasi Pembayaran",
            message = "Lanjut untuk membeli ?",
            onConfirm = {
                viewModel.checkout()
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun CartScreenContent(
    productsInCart: List<ProductInCart>,
    onCheckoutClick: () -> Unit,
    navigateToDetail: (Int) -> Unit,
    onDecrease: (ProductInCart) -> Unit,
    onIncrease: (ProductInCart) -> Unit
) {
    val totalPrice = productsInCart.sumOf { it.product.price * it.quantity }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(productsInCart) { cartItem ->
                CartItem(
                    productInCart = cartItem,
                    onClick = { navigateToDetail(cartItem.product.id) },
                    onDecrease = { onDecrease(cartItem) },
                    onIncrease = { onIncrease(cartItem) })
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Total Price",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "$${totalPrice} USD",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { onCheckoutClick() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onBackground)
                    ) {
                        Text(
                            text = "Checkout",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
private fun CartScreenPreview() {
    FindestTheme {
        CartScreenContent(
            productsInCart = dummyProductsInCart,
            onCheckoutClick = {},
            onIncrease = {},
            onDecrease = {},
            navigateToDetail = {}
        )
    }
}
