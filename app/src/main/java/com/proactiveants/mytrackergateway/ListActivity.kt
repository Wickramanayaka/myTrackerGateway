package com.proactiveants.mytrackergateway

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity() {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter : CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val list: ArrayList<User> = ArrayList<User>()
        list.add(User("Chamal Ayesh","0770219031","371, Mahawewa, Sri Lanka","2021/09/18 02:00PM"))
        list.add(User("John Smith","077172000","50/2, Colombo, Sri Lanka","2021/09/18 02:50PM"))


        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        adapter = CustomAdapter(list)
        recyclerView.adapter = adapter



    }
}