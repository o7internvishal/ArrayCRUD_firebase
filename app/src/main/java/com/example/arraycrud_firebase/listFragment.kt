package com.example.arraycrud_firebase

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.arraycrud_firebase.`interface`.Clickinterface
//import com.example.arraycrud_firebase.`interface`.Clickinterface
import com.example.arraycrud_firebase.adapter.UserAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [listFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class listFragment : Fragment(), Clickinterface {

    lateinit var floatingActionButton: FloatingActionButton
    lateinit var database: DatabaseReference
    lateinit var etFirstname: EditText
    lateinit var etLastname: EditText
    lateinit var recyclerView: RecyclerView
    lateinit var userList: ArrayList<User>
    lateinit var userAdapter: UserAdapter
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        floatingActionButton = view.findViewById(R.id.floatingActionButton)
        recyclerView = view.findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(false)
        userList = arrayListOf<User>()
        getuserData()


        floatingActionButton.setOnClickListener {
            val inflater = LayoutInflater.from(mainActivity)
            val v = inflater.inflate(R.layout.custom_dialogbox, null)
            val addDialog = AlertDialog.Builder(mainActivity)
            addDialog.create()
            addDialog.setView(v)
            etFirstname = v.findViewById(R.id.etFirstname)
            etLastname = v.findViewById(R.id.etLastname)
            database = FirebaseDatabase.getInstance().getReference("User")
            addDialog.setPositiveButton("Add data") { dialog, _ ->
                saveuserdata()
                dialog.dismiss()
            }
            addDialog.setNegativeButton("Cancel") { dialog, _ ->
                Toast.makeText(activity, "Cancel", Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }
            addDialog.show()
        }
        return view
    }

    private fun getuserData() {
        database = FirebaseDatabase.getInstance().getReference("User")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                if (snapshot.exists()) {
                    for (usersnap in snapshot.children) {
                        val userData = usersnap.getValue(User::class.java)
                        userList.add(userData!!)
                    }
                    userAdapter = UserAdapter(userList, this@listFragment)
                    recyclerView.adapter = userAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun saveuserdata() {
        val firstname = etFirstname.text.toString()
        val lastname = etLastname.text.toString()

//        if (firstname.isEmpty()){
//            etFirstname.error="enter your name"
//        }
//        if(lastname.isEmpty()){
//            etLastname.error="enter your last name"
//        }
        val userId = database.push().key
        val user = User(firstname, lastname)
        database.child(userId!!).setValue(user).addOnCompleteListener {
            Toast.makeText(activity, "Insert data", Toast.LENGTH_LONG).show()
        }.addOnFailureListener { err ->
            Toast.makeText(activity, "Failed data", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment listFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) = listFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }

    override fun updateClicked(position: Int) {
        val inflater = LayoutInflater.from(mainActivity)
        val v = inflater.inflate(R.layout.custom_dialogbox, null)
        val addDialog = AlertDialog.Builder(mainActivity)
        addDialog.create()
        addDialog.setView(v)
        etFirstname = v.findViewById(R.id.etFirstname)
        etLastname = v.findViewById(R.id.etLastname)
        etFirstname.setText(userList[position].firstname)
        etLastname.setText(userList[position].lastname)
//        etItem.setText(userList[position].userName)
//        etDescription.setText(userList[position].userMb)
        database = FirebaseDatabase.getInstance().getReference("User")
        addDialog.setPositiveButton("Update data") { dialog, _ ->
//            updateuserdata(position)
            val firstname = etFirstname.text.toString()
            val lastname = etLastname.text.toString()
            userList.set(position, User(firstname,lastname))
            dialog.dismiss()
        }
        addDialog.setNegativeButton("Cancel") { dialog, _ ->
            Toast.makeText(activity, "Cancel", Toast.LENGTH_LONG).show()
            dialog.dismiss()
        }
        addDialog.show()
    }

//    private fun updateuserdata(position: Int) {
//        val firstname = etFirstname.text.toString()
//        val lastname = etLastname.text.toString()
//
////        if (firstname.isEmpty()){
////            etFirstname.error="enter your name"
////        }
////        if(lastname.isEmpty()){
////            etLastname.error="enter your last name"
////        }
//        val userId = database.push().key
//        val user = User(firstname, lastname)
//        database.child(userId!!).setValue(user).addOnCompleteListener {
//            Toast.makeText(activity, "update data", Toast.LENGTH_LONG).show()
//        }.addOnFailureListener { err ->
//            Toast.makeText(activity, "Failed data", Toast.LENGTH_LONG).show()
//        }
//    }

    override fun deleteClicked(position: Int) {

//        userList.removeAt(position)
//        database = FirebaseDatabase.getInstance().getReference("User")
//        database.child(etFirstname.toString()).removeValue().addOnSuccessListener {
//            etFirstname.text.clear()
////            etLastname.text.clear()
//            Toast.makeText(activity,"Successfull deleted",Toast.LENGTH_LONG).show()
//        }.addOnFailureListener{
//            Toast.makeText(activity,"Failed deleted",Toast.LENGTH_LONG).show()
//
//        }
    }

}