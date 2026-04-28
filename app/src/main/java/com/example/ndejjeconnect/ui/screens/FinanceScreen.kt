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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceScreen(
    authViewModel: AuthViewModel,
    dashboardViewModel: DashboardViewModel,
    onNavigateBack: () -> Unit
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val financeRecord by dashboardViewModel.financeRecord.collectAsState()

    var totalFeesText by remember { mutableStateOf("") }
    var paidFeesText by remember { mutableStateOf("") }

    LaunchedEffect(financeRecord) {
        financeRecord?.let {
            totalFeesText = it.totalFees.toLong().toString()
            paidFeesText = it.amountPaid.toLong().toString()
        }
    }

    val totalAmount = totalFeesText.toDoubleOrNull() ?: 0.0
    val paidAmount = paidFeesText.toDoubleOrNull() ?: 0.0
    val paymentProgress = if (totalAmount > 0) (paidAmount / totalAmount).coerceIn(0.0, 1.0).toFloat() else 0f
    val balanceAmount = (totalAmount - paidAmount).coerceAtLeast(0.0)

    Scaffold(
        topBar = {
            FinanceTopBar(onBackClick = onNavigateBack)
        },
        floatingActionButton = {
            SaveFinanceButton(
                onClick = {
                    currentUser?.let {
                        dashboardViewModel.updateFinance(
                            FinanceRecord(it.regNumber, totalAmount, paidAmount)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Fees Tracking",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )

            FeesInputSection(
                totalFees = totalFeesText,
                onTotalFeesChange = { totalFeesText = it },
                paidFees = paidFeesText,
                onPaidFeesChange = { paidFeesText = it }
            )

            PaymentSummaryCard(
                progress = paymentProgress,
                paid = paidAmount,
                balance = balanceAmount
            )

            FinanceTipsSection()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FinanceTopBar(onBackClick: () -> Unit) {
    TopAppBar(
        title = { Text("Financial Status") },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Navigate Back")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
}

@Composable
private fun SaveFinanceButton(onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White,
        icon = { Icon(Icons.Default.Save, contentDescription = null) },
        text = { Text("Save Records") }
    )
}

@Composable
private fun FeesInputSection(
    totalFees: String,
    onTotalFeesChange: (String) -> Unit,
    paidFees: String,
    onPaidFeesChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(
                value = totalFees,
                onValueChange = onTotalFeesChange,
                label = { Text("Total Semester Fees (UGX)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = paidFees,
                onValueChange = onPaidFeesChange,
                label = { Text("Amount Paid (UGX)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
}

@Composable
private fun PaymentSummaryCard(
    progress: Float,
    paid: Double,
    balance: Double
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Payment Progress",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.size(140.dp),
                    strokeWidth = 12.dp,
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FinanceMetric(label = "Paid", amount = paid, color = Color(0xFF2E7D32))
                FinanceMetric(label = "Balance", amount = balance, color = MaterialTheme.colorScheme.error, isEnd = true)
            }

            Spacer(modifier = Modifier.height(20.dp))
            
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
            )
        }
    }
}

@Composable
private fun FinanceMetric(label: String, amount: Double, color: Color, isEnd: Boolean = false) {
    Column(horizontalAlignment = if (isEnd) Alignment.End else Alignment.Start) {
        Text(text = label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.outline)
        Text(text = "${amount.toLong()} UGX", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = color)
    }
}

@Composable
private fun FinanceTipsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        FinanceBulletPoint("All semester fees must be cleared before the final examination period.")
        FinanceBulletPoint("Registration requirements stipulate a minimum of 50% tuition payment.")
        FinanceBulletPoint("Keep your physical payment receipts safe for verification purposes.")
    }
}

@Composable
private fun FinanceBulletPoint(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "• ",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
