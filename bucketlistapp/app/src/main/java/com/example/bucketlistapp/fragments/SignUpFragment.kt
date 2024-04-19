package com.example.to_do_list_app.fragments

import android.app.Activity
import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.to_do_list_app.R
import com.example.to_do_list_app.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth



class
SignUpFragment : Fragment() {
    private lateinit var auth:FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var binding:FragmentSignUpBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentSignUpBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        registerEvents()
    }

    private fun registerEvents() {
        binding.AuthTextView.setOnClickListener{
            navController.navigate(R.id.action_signUpFragment_to_signInFragment)
        }
        binding.next.setOnClickListener{
        val email=binding.email.text.toString().trim()
        val pass=binding.password.text.toString().trim()
        val verifyPass=binding.repassword.text.toString().trim()
        if(email.isNotEmpty() && pass.isNotEmpty() && verifyPass.isNotEmpty()){
            if(pass == verifyPass){
                binding.progressBar.visibility=View.VISIBLE
                auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(context, "Registered Successfully", Toast.LENGTH_LONG).show()
                        navController.navigate(R.id.action_signUpFragment_to_homeFragment)
                    } else {
                        Toast.makeText(context, it.exception?.message, Toast.LENGTH_LONG).show()
                    }
                } }else{
                Toast.makeText(context,"Passwords don't match",Toast.LENGTH_SHORT).show()
            }
            binding.progressBar.visibility=View.GONE
            }
        }


    }

    private fun init(view:View){
        navController=Navigation.findNavController(view)
        auth=FirebaseAuth.getInstance()

    }

}