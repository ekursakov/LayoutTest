package com.github.ekursakov.headerslayout.example

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.github.ekursakov.headerslayout.HeadersLayout
import kotlinx.android.synthetic.main.test_view.view.*

/**
 * @author Evgeniy Kursakov
 */
class TestView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    init {
        View.inflate(context, R.layout.test_view, this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        val layoutParams = (layoutParams as? HeadersLayout.LayoutParams) ?: return

        toggleCollapsible.isChecked = layoutParams.collapsible
        toggleCollapsible.setOnCheckedChangeListener { _, isChecked ->
            layoutParams.collapsible = isChecked
            requestLayout()
        }
        toggleSnap.isChecked = layoutParams.snap
        toggleSnap.setOnCheckedChangeListener { _, isChecked ->
            layoutParams.snap = isChecked
            requestLayout()
        }

        etCollapsedHeight.setText(layoutParams.collapsedHeight.toString())
        etCollapsedHeight.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                layoutParams.collapsedHeight = s.toString().toIntOrNull() ?: 0
                requestLayout()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
        })

        addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            tvHeight.text = layoutParams.height.toString()
        }
    }
}