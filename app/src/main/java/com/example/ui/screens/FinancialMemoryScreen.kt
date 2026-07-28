package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.TransactionEntity
import com.example.ui.LinkViewModel
import com.example.ui.components.TransactionDetailDialog
import com.example.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@Composable
fun FinancialMemoryScreen(
    viewModel: LinkViewModel
) {
    val transactions by viewModel.transactions.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedTxForDetail by remember { mutableStateOf<TransactionEntity?>(null) }

    val currentSelectedTx = selectedTxForDetail
    if (currentSelectedTx != null) {
        TransactionDetailDialog(
            transaction = currentSelectedTx,
            onDismiss = { selectedTxForDetail = null }
        )
    }

    val filteredList = remember(searchQuery, transactions) {
        if (searchQuery.isBlank()) transactions
        else transactions.filter {
            it.recipient.contains(searchQuery, ignoreCase = true) ||
            it.category.contains(searchQuery, ignoreCase = true) ||
            it.purpose.contains(searchQuery, ignoreCase = true) ||
            it.sourceAccount.contains(searchQuery, ignoreCase = true)
        }
    }

    val currencyFormatter = NumberFormat.getNumberInstance(Locale.US)

    val sampleQueries = listOf(
        "When did I pay my landlord?",
        "How much did I spend on food this month?",
        "Show my biggest expenses",
        "How many eCash transfers in French?"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(LinkNavyBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = "Memory",
                        tint = LinkElectricCyan
                    )
                    Text(
                        text = "AI Financial Memory",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = LinkElectricCyan
                    )
                }
                Text(
                    text = "Ask natural questions about your past spending and account history stored in Room DB.",
                    style = MaterialTheme.typography.bodySmall,
                    color = LinkTextSecondary
                )
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Query financial memory...", color = LinkTextSecondary) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = LinkElectricCyan) },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = "Clear", tint = LinkTextSecondary)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_memory_search"),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = LinkElectricCyan,
                    unfocusedBorderColor = LinkSurfaceBorder,
                    focusedTextColor = LinkTextPrimary,
                    unfocusedTextColor = LinkTextPrimary
                )
            )
        }

        // Quick Sample AI Questions
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Sample Memory Queries:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = LinkTextSecondary
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    sampleQueries.take(2).forEach { q ->
                        Button(
                            onClick = {
                                searchQuery = q
                                viewModel.updateInput(q)
                                viewModel.processNaturalLanguageInput(q)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = LinkSurfaceCard),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = q,
                                style = MaterialTheme.typography.labelSmall,
                                color = LinkElectricCyan,
                                maxLines = 2,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // Summary Stats Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = LinkSurfaceCard),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Local Database Persistence (Room DB)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = LinkTextPrimary
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("TOTAL RECORDS", style = MaterialTheme.typography.labelSmall, color = LinkTextSecondary)
                            Text("${transactions.size} Items", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = LinkElectricCyan)
                        }
                        Column {
                            Text("LARGEST EXPENSE", style = MaterialTheme.typography.labelSmall, color = LinkTextSecondary)
                            Text("150,000 RWF", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = LinkEmeraldGreen)
                        }
                        Column {
                            Text("PERSISTENCE", style = MaterialTheme.typography.labelSmall, color = LinkTextSecondary)
                            Text("SQLite Local", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = LinkAmberGold)
                        }
                    }
                }
            }
        }

        // Transaction History Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Memory Log (${filteredList.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = LinkTextPrimary
                )
            }
        }

        // Transactions
        items(filteredList) { tx ->
            TransactionItemRow(
                tx = tx,
                currencyFormatter = currencyFormatter,
                onClick = { selectedTxForDetail = tx }
            )
        }
    }
}
