package com.example.to_do_list_app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.to_do_list_app.Utils.ToDoData
import com.example.to_do_list_app.Utils.TodoAdapter
import com.example.to_do_list_app.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class HomeFragment : Fragment(), AddTodoPopupFragment.DialogNextBtnClickListner,
    TodoAdapter.todoAdapterClicksInterface {

    private lateinit var auth:FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var navController: NavController
    private lateinit var binding:FragmentHomeBinding
    private  var popupFragment: AddTodoPopupFragment?=null
    private lateinit var adapter:TodoAdapter
    private lateinit var mList:MutableList<ToDoData>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHomeBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        regiterEvents()
        getDataFromFirebase()
    }

    private fun getDataFromFirebase() {
        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               mList.clear()
                for(taskSnapshot in snapshot.children){
                    val todoTask=taskSnapshot.key?.let {
                        ToDoData(it,taskSnapshot.value.toString(),false)
                    }
                    if(todoTask!=null){
                        mList.add(todoTask)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,error.message,Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun regiterEvents() {
        binding.addBtn.setOnClickListener{
            if(popupFragment!=null)
                childFragmentManager.beginTransaction().remove(popupFragment!!).commit()
            popupFragment= AddTodoPopupFragment()
            popupFragment?.setListener(this)
            popupFragment?.show(childFragmentManager,AddTodoPopupFragment.TAG)
        }
        binding.signout.setOnClickListener{
            auth.signOut()
            getActivity()?.moveTaskToBack(true);
            getActivity()?.finish();


        }
    }



    private fun init(view: View) {
        navController= Navigation.findNavController(view)
        auth= FirebaseAuth.getInstance()
        databaseReference=FirebaseDatabase.getInstance().reference.child("Tasks").child(auth.currentUser?.uid.toString())
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager=LinearLayoutManager(context)
        mList= mutableListOf()
        adapter= TodoAdapter(mList)
        adapter.setListener(this)
        binding.recyclerView.adapter=adapter

    }

    override fun onSaveTask(todo: String, todoEt: AppCompatEditText) {
        databaseReference.push().setValue(todo).addOnCompleteListener { if(it.isSuccessful){
            Toast.makeText(context,"Todo Saved!!",Toast.LENGTH_SHORT).show()
            todoEt.text=null
        }else{
            Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()
        }
            popupFragment!!.dismiss()

        }
    }

    override fun onUpdateTask(toDoData: ToDoData, todoEt: AppCompatEditText) {
       val map =HashMap<String,Any>()
        map[toDoData.taskId]=toDoData.task
        databaseReference.updateChildren(map).addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(context,"Edited Successfully",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()

            }
            todoEt.text=null

            popupFragment!!.dismiss()
        }
    }

    override fun onDeleteTaskBtnClicked(toDoData: ToDoData) {
        databaseReference.child(toDoData.taskId).removeValue().addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(context,"Deleted Successfully",Toast.LENGTH_SHORT).show()

            }
            else{
                Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun onEditBtnClicked(toDoData: ToDoData) {
        if(popupFragment!=null){
            childFragmentManager.beginTransaction().remove(popupFragment!!).commit()
        }
        popupFragment=AddTodoPopupFragment.newInstance(toDoData.taskId,toDoData.task)
        popupFragment!!.setListener(this)
        popupFragment!!.show(childFragmentManager,AddTodoPopupFragment.TAG)
    }

    override fun onBoxBtnClicked(todoData: ToDoData) {

    }


}