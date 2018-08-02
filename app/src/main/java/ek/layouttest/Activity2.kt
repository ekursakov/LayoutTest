package ek.layouttest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_2.*

class Activity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)
view_5.layoutManager = LinearLayoutManager(this)
        view_5.adapter = Adapter(LayoutInflater.from(this))
    }

    class Adapter(private val layoutInflater: LayoutInflater) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return Holder(layoutInflater.inflate(R.layout.item_dummy, parent, false))
        }

        override fun getItemCount() = 50

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        }
    }

    class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView)
}