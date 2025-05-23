package com.example.medivial.Adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.medivial.FireStore.di.FirebaseModule.userDitails
import com.example.medivial.data_Model.Nutrient
import com.example.medivial.databinding.FoodListBinding
import java.time.LocalDate

class FoodNameAdapter(val foodlist:ArrayList<Nutrient>,val context:Context):
    RecyclerView.Adapter<FoodNameAdapter.MyViewHolder>() {
    inner class MyViewHolder(val binding: FoodListBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = FoodListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return foodlist.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder){
            with(foodlist[position]){
                binding.itemName.text = this.foodName
                binding.calo.text = this.calories
                binding.amount.text = this.quantity
                binding.unit.text = this.unit
            }
        }
    }

    private val touchHelper=object :ItemTouchHelper.SimpleCallback(
        0,ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
    ){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            try {
                val position=viewHolder.adapterPosition
                val item=foodlist[position]
                // Delete item from Firestore
                userDitails.collection("Meals").document(LocalDate.now().toString()).collection("time")
                    .document(item.foodName!!)
                    .delete()
                    .addOnSuccessListener {
                        // Show snackbar or toast message here to notify user
                        Toast.makeText(context, "${item.foodName}deleted successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        // Show error message here
                    }
                foodlist.removeAt(position)
                notifyItemRemoved(position)
            }catch(_:IndexOutOfBoundsException){}

        }
    }
    fun attachswipetoDelete(rec_v:RecyclerView)
    {
        val touchHelper=ItemTouchHelper(touchHelper)
        touchHelper.attachToRecyclerView(rec_v)
    }
}