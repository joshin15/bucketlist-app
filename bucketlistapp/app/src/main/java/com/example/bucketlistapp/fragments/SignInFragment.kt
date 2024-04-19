package com.example.to_do_list_app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.to_do_list_app.R
import com.example.to_do_list_app.databinding.FragmentSignInBinding
import com.example.to_do_list_app.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignInFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentSignInBinding.inflate(inflater,container,false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        registerEvents()
    }

    private fun registerEvents() {
        binding.AuthTextView.setOnClickListener{
            navController.navigate(R.id.action_signInFragment_to_signUpFragment)
        }
        binding.next.setOnClickListener{
            val email=binding.email.text.toString().trim()
            val pass=binding.password.text.toString().trim()
            if(email.isNotEmpty() && pass.isNotEmpty()){
                    binding.progressBar.visibility=View.VISIBLE
                    auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            navController.navigate(R.id.action_signInFragment_to_homeFragment)
                        } else {
                            Toast.makeText(context, it.exception?.message, Toast.LENGTH_LONG).show()
                        }
                        binding.progressBar.visibility=View.GONE
                     }
            }
            else{
                Toast.makeText(context,"Empty fields not allowed",Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun init(view:View){
        navController= Navigation.findNavController(view)
        auth=FirebaseAuth.getInstance()

    }

}

