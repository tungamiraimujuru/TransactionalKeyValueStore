package com.example.transactionalkeyvaluestore

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.transactionalkeyvaluestore.databinding.ActivityMainBinding
import com.example.transactionalkeyvaluestore.screen.transactions.TransactionViewModel
import com.example.transactionalkeyvaluestore.screen.transactions.transactions_list.TransactionAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: TransactionViewModel by viewModels()
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        transactionAdapter = TransactionAdapter()
        binding.sendButton.setOnClickListener {
            viewModel.validateAndProcessInput(binding.inputEditText.text.toString())
        }

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.transactionsRecyclerView.layoutManager = layoutManager
        binding.transactionsRecyclerView.adapter = transactionAdapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.clearInput.collect {
                        binding.inputEditText.setText("")
                    }
                }

                launch {
                    viewModel.transaction.collect {
                        transactionAdapter.appendOperation(it)
                    }
                }
            }
        }
    }
}