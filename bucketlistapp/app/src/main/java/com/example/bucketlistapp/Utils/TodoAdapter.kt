package com.example.to_do_list_app.Utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.to_do_list_app.databinding.EachtododataBinding
import com.example.to_do_list_app.fragments.HomeFragment


class TodoAdapter(private val list: MutableList<ToDoData>) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>(){
    private var listner:todoAdapterClicksInterface?=null
    fun setListener(Listener: HomeFragment){
        this.listner=Listener
    }
    inner class TodoViewHolder(val binding: EachtododataBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding=EachtododataBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        with(holder){
            with(list[position]){
                binding.TodoTask.text=this.task
                binding.Delete.setOnClickListener{
                    listner?.onDeleteTaskBtnClicked(this)
                }
                binding.Edit.setOnClickListener{
                    listner?.onEditBtnClicked(this)
                }
                binding.box.setOnClickListener{
                    listner?.onBoxBtnClicked(this)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface todoAdapterClicksInterface{
        fun onDeleteTaskBtnClicked(toDoData: ToDoData)
        fun onEditBtnClicked(toDoData: ToDoData)
        fun onBoxBtnClicked(todoData:ToDoData)
    }
}