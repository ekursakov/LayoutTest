package com.github.ekursakov.headerslayout.example

import android.os.Bundle
import android.support.transition.TransitionManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.github.ekursakov.headerslayout.HeadersLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        applyScene1()

        addScene("1") { applyScene1() }
        addScene("2") { applyScene2() }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = Adapter(LayoutInflater.from(this))
    }

    private fun addScene(i: String, listener: (View) -> Unit) {
        scenes.addView(Button(this).apply {
            text = i
            setOnClickListener(listener)
        })
    }

    private fun applyScene1() {
        TransitionManager.beginDelayedTransition(uber_layout)

        with(view_1.uberLayoutParams) {
            height = 56.dp()
            collapsible = true
            collapsedHeight = 0.dp()
        }
        view_1.visibility = View.VISIBLE

        with(view_2.uberLayoutParams) {
            height = 120.dp()
            collapsible = true
            collapsedHeight = 0.dp()
        }
        view_2.visibility = View.VISIBLE

        with(view_3.uberLayoutParams) {
            height = 56.dp()
            collapsible = true
            collapsedHeight = 0.dp()
        }
        view_3.visibility = View.VISIBLE

        with(view_4.uberLayoutParams) {
            height = 56.dp()
            collapsible = true
            collapsedHeight = 0.dp()
        }
        view_4.visibility = View.VISIBLE

        uber_layout.requestLayout()
    }

    private fun applyScene2() {
        TransitionManager.beginDelayedTransition(uber_layout)

        with(view_1.uberLayoutParams) {
            height = 56.dp()
            collapsible = false
            collapsedHeight = 0.dp()
        }
        view_1.visibility = View.VISIBLE

        with(view_2.uberLayoutParams) {
            height = 121.dp()
            collapsible = true
            collapsedHeight = 0.dp()
        }
        view_2.visibility = View.VISIBLE

        with(view_3.uberLayoutParams) {
            height = 56.dp()
            collapsible = true
            collapsedHeight = 0.dp()
        }
        view_3.visibility = View.VISIBLE

        with(view_4.uberLayoutParams) {
            height = 56.dp()
            collapsible = false
            collapsedHeight = 0.dp()
        }
        view_4.visibility = View.GONE

        uber_layout.requestLayout()
    }

    class Adapter(private val layoutInflater: LayoutInflater) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return Holder(layoutInflater.inflate(R.layout.item_dummy, parent, false))
        }

        override fun getItemCount() = 50

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        }
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val View.uberLayoutParams: HeadersLayout.LayoutParams
        get() = layoutParams as HeadersLayout.LayoutParams

    private fun Int.dp(): Int {
        val density = resources.displayMetrics.density
        return Math.round(this.toFloat() * density)
    }
}