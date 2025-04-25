package com.example.findest.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.findest.data.model.ProductInCart
import com.example.findest.ui.theme.FindestTheme
import com.example.findest.utils.dummyProductsInCart

@Composable
fun CartItem(
    productInCart: ProductInCart,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onClick: () -> Unit
) {
    val product = productInCart.product
    val discountedPrice = product.price - (product.price * product.discount / 100)
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = product.color,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                    if (product.discount > 0) {
                        Text(
                            text = "$${discountedPrice} USD",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Text(
                            text = "$${product.price} USD",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary

                        )
                    }
                }

                IconButton(
                    onClick = { onDecrease() },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Cyan)
                ) {
                    Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease")
                }

                Text(
                    text = productInCart.quantity.toString(),
                    style = MaterialTheme.typography.titleMedium
                )

                IconButton(
                    onClick = { onIncrease() },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Cyan)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Increase")
                }

            }
        }
    }
}


@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
private fun CartItemPreview() {
    FindestTheme {
        CartItem(
            productInCart = dummyProductsInCart[2],
            onClick = {},
            onDecrease = {},
            onIncrease = {}
        )
    }

}