package com.example.to_do_list_app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.DialogFragment
import com.example.to_do_list_app.R
import com.example.to_do_list_app.Utils.ToDoData
import com.example.to_do_list_app.databinding.FragmentAddTodoPopupBinding
import com.google.android.material.textfield.TextInputEditText

class AddTodoPopupFragment : DialogFragment() {
    private lateinit var listner:DialogNextBtnClickListner
    private lateinit var binding:FragmentAddTodoPopupBinding
    private var todoData:ToDoData?=null
    fun setListener(Listener:DialogNextBtnClickListner){
        this.listner=Listener
    }

    companion object{
        const val TAG="AddTodoPopUpFragment"
        @JvmStatic
        fun newInstance(taskId:String,task:String)=AddTodoPopupFragment().apply {
            arguments=Bundle().apply {
                putString("taskId",taskId)
                putString("task",task)
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding=FragmentAddTodoPopupBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(arguments!=null){
            todoData=
                ToDoData(arguments?.getString("taskId").toString(),arguments?.getString("task").toString(),false)
            binding.todoEt.setText(todoData?.task)
        }
        registerEvents()
    }

    private fun registerEvents() {
        binding.next.setOnClickListener{
            val todoTask=binding.todoEt.text.toString()
            if(todoTask.isNotEmpty()){
                if(todoData==null){
                    listner.onSaveTask(todoTask,binding.todoEt)

                }else{
                    todoData?.task=todoTask
                    listner.onUpdateTask(todoData!!,binding.todoEt)
                }

            }else{
                Toast.makeText(context,"Please enter some task",Toast.LENGTH_SHORT).show()
            }
        }
        binding.todoClose.setOnClickListener{
            dismiss()
        }
    }
    interface DialogNextBtnClickListner{
        fun onSaveTask(todo:String,todoEt:AppCompatEditText)
        fun onUpdateTask(toDoData: ToDoData,todoEt:AppCompatEditText)
    }


}