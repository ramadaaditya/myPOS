package com.example.findest.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.findest.ui.theme.FindestTheme

@Composable
fun CategoryRow(
    modifier: Modifier = Modifier,
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(categories) { category ->
            val isSelected = category == selectedCategory
            CategoryChip(
                label = category,
                isSelected = isSelected,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@Composable
fun CategoryChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor =
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val textColor =
        if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)

    Surface(
        modifier = Modifier
            .clickable { onClick() }
            .shadow(
                elevation = if (isSelected) 4.dp else 0.dp,
                shape = RoundedCornerShape(24.dp)
            ),
        shape = RoundedCornerShape(24.dp),
        color = backgroundColor
    ) {
        Text(
            text = label,
            color = textColor,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryScreenPreview() {
    FindestTheme {
        CategoryRow(
            categories = listOf("All", "Audio", "TV", "Mobile", "Gaming", "Appliance"),
            selectedCategory = "Audio",
            onCategorySelected = {}
        )
    }
}