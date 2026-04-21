package com.example.ndejjeconnect.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ndejjeconnect.data.local.FinanceRecord
import com.example.ndejjeconnect.viewmodel.AuthViewModel
import com.example.ndejjeconnect.viewmodel.DashboardViewModel

/**
 * FinanceScreen is the student's "Pocket Tracker".
 * It helps them keep track of school fees: what's total, what's paid, and what's left.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceScreen(
    authViewModel: AuthViewModel,
    dashboardViewModel: DashboardViewModel,
    onNavigateBack: () -> Unit
) {
    // 1. GETTING DATA
    // Connect to the "Brains" to get current user and their money records
    val currentUser by authViewModel.currentUser.collectAsState()
    val financeRecord by dashboardViewModel.financeRecord.collectAsState()

    // 2. FORM STATE: Temporary memory for the input boxes
    var totalFees by remember { mutableStateOf("") }
    var paidFees by remember { mutableStateOf("") }

    // When the screen opens, pre-fill the boxes with data already saved in the database
    LaunchedEffect(financeRecord) {
        financeRecord?.let {
            totalFees = it.totalFees.toLong().toString()
            paidFees = it.amountPaid.toLong().toString()
        }
    }

    // 3. CALCULATIONS: Doing the math for progress and balance
    val total = totalFees.toDoubleOrNull() ?: 0.0
    val paid = paidFees.toDoubleOrNull() ?: 0.0
    val progress = if (total > 0) (paid / total).coerceIn(0.0, 1.0).toFloat() else 0f
    val balance = (total - paid).coerceAtLeast(0.0)

    Scaffold(
        topBar = {
            // Header bar with a back button
            TopAppBar(
                title = { Text("Financial Status") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            // The "Save" button that floats at the bottom right
            ExtendedFloatingActionButton(
                onClick = {
                    currentUser?.let {
                        dashboardViewModel.updateFinance(
                            FinanceRecord(it.regNumber, total, paid)
                        )
                    }
                },
                icon = { Icon(Icons.Default.Save, contentDescription = null) },
                text = { Text("Save Status") }
            )
        }
    ) { padding ->
        // The main scrollable content area
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Fees Tracker",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            // INPUT SECTION: Where the student types in their fees info
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = totalFees,
                        onValueChange = { totalFees = it },
                        label = { Text("Total Semester Fees (UGX)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = paidFees,
                        onValueChange = { paidFees = it },
                        label = { Text("Amount Paid (UGX)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // VISUAL SUMMARY: A circle chart showing how much is paid
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Payment Progress", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.size(120.dp),
                            strokeWidth = 12.dp,
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                        Text(
                            text = "${(progress * 100).toInt()}%",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // PAID vs BALANCE numbers
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Paid", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                            Text(text = "${paid.toLong()} UGX", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "Balance", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                            Text(text = "${balance.toLong()} UGX", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // A linear progress bar as an alternative view
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                    )
                }
            }
            
            // Helpful school tips at the bottom
            FinanceBulletPoint("Ensure you clear 100% of the fees to sit for exams.")
            FinanceBulletPoint("Registration is only complete after 50% payment.")
        }
    }
}

/**
 * A small reusable component for showing a bullet point tip.
 */
@Composable
fun FinanceBulletPoint(text: String) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Text("• ", fontWeight = FontWeight.Bold)
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}
