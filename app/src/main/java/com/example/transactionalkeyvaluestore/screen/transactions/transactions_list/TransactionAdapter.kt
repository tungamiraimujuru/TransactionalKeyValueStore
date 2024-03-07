package com.example.transactionalkeyvaluestore.screen.transactions.transactions_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.transactionalkeyvaluestore.databinding.TransactionListItemBinding

class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    private val operationList = mutableListOf<Operation>()

    class TransactionViewHolder(private val binding: TransactionListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(operation: Operation) {
            binding.transactionOperationTextView.text = operation.operationString
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding =
            TransactionListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun getItemCount(): Int = operationList.size

    fun appendOperation(operationString: String) {
        val operation = Operation(operationString)
        operationList.add(operation)
        notifyItemInserted(operationList.size - 1)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(operationList[position])
    }
}