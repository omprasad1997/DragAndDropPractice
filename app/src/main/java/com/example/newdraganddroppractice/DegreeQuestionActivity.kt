package com.example.newdraganddroppractice

import android.content.ClipData
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.JustifyContent

class DegreeQuestionActivity : AppCompatActivity(), View.OnDragListener, View.OnTouchListener {
    private lateinit var originalContainer: RecyclerView
    private lateinit var mParent: ConstraintLayout

    private lateinit var dataList: ArrayList<Data>
    private var fillOptionsList = ArrayList<String>()

    private lateinit var ll_comparative_1: LinearLayout
    private lateinit var ll_superlative_1: LinearLayout
    private lateinit var ll_comparative_2: LinearLayout
    private lateinit var ll_superlative_2: LinearLayout
    private lateinit var ll_comparative_3: LinearLayout
    private lateinit var ll_superlative_3: LinearLayout
    private lateinit var submitButton : Button

    private var isViewDroppedInsideOriginalContainer = false
    private var isViewDroppedInsideComparative1Container = false
    private var isViewDroppedInsideSuperlative1Container = false
    private var isViewDroppedInsideComparative2Container = false
    private var isViewDroppedInsideSuperlative2Container = false
    private var isViewDroppedInsideComparative3Container = false
    private var isViewDroppedInsideSuperlative3Container = false

    private lateinit var reArrangeTextInsideOrderSentenceContainer: View.OnDragListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_degree_question)

        mParent = findViewById(R.id.mParent)
        originalContainer = findViewById(R.id.originalContainer)
        ll_comparative_1 = findViewById(R.id.ll_comparative_1)
        ll_superlative_1 = findViewById(R.id.ll_superlative_1)
        ll_comparative_2 = findViewById(R.id.ll_comparative_2)
        ll_superlative_2 = findViewById(R.id.ll_superlative_2)
        ll_comparative_3 = findViewById(R.id.ll_comparative_3)
        ll_superlative_3 = findViewById(R.id.ll_superlative_3)
        submitButton = findViewById(R.id.submitButton)

        dataList = getData()

        originalContainer.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                initAdapter()

                originalContainer.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }

        })

        originalContainer.setOnDragListener(this)
        ll_comparative_1.setOnDragListener(this)
        ll_superlative_1.setOnDragListener(this)
        ll_comparative_2.setOnDragListener(this)
        ll_superlative_2.setOnDragListener(this)
        ll_comparative_3.setOnDragListener(this)
        ll_superlative_3.setOnDragListener(this)

        reArrangeTextInsideOrderSentenceContainer = View.OnDragListener { view, event ->
            when (event!!.action) {
                DragEvent.ACTION_DRAG_STARTED -> true
                DragEvent.ACTION_DRAG_ENTERED -> true
                DragEvent.ACTION_DRAG_LOCATION -> true
                DragEvent.ACTION_DRAG_EXITED -> true

                DragEvent.ACTION_DROP -> {

                    val draggedView = event.localState as View

                    val oldParent = draggedView.parent as ViewGroup

                    val v = view?.parent as ViewGroup

                    when (v?.id) {
                        R.id.ll_comparative_1 -> {
                            isViewDroppedInsideComparative1Container = true
                            updateDraggedData(oldParent, ll_comparative_1, draggedView)
                        }

                        R.id.ll_superlative_1 -> {
                            isViewDroppedInsideSuperlative1Container = true
                            updateDraggedData(oldParent, ll_superlative_1, draggedView)
                        }

                        R.id.ll_comparative_2 -> {
                            isViewDroppedInsideComparative2Container = true
                            updateDraggedData(oldParent, ll_comparative_2, draggedView)
                        }

                        R.id.ll_superlative_2 -> {
                            isViewDroppedInsideSuperlative2Container = true
                            updateDraggedData(oldParent, ll_superlative_2, draggedView)
                        }

                        R.id.ll_comparative_3 -> {
                            isViewDroppedInsideComparative3Container = true
                            updateDraggedData(oldParent, ll_comparative_3, draggedView)
                        }

                        R.id.ll_superlative_3 -> {
                            isViewDroppedInsideSuperlative3Container = true
                            updateDraggedData(oldParent, ll_superlative_3, draggedView)
                        }
                    }

                    true

                }
                DragEvent.ACTION_DRAG_ENDED -> {

                   // val v = view?.parent as ViewGroup

                    if(fillOptionsList.size != dataList.size){
                        submitButton.visibility = View.GONE
                    } else {
                        submitButton.visibility = View.VISIBLE
                    }

                    true
                }

                else -> {

                    true
                }
            }
        }

    }

    private fun getData(): ArrayList<Data> {

        val list = ArrayList<Data>()

        list.add(Data("Prettiest", "1"))
        list.add(Data("Worst", "2"))
        list.add(Data("Prettier", "3"))
        list.add(Data("Better", "4"))
        list.add(Data("Worse", "5"))
        list.add(Data("Best", "6"))

        return list

    }

    private fun initAdapter() {
        originalContainer.layoutManager = GridLayoutManager(this, dataList.size)
        originalContainer.adapter = MyDegreeAdapter(dataList)
    }

    inner class MyDegreeAdapter(var list: ArrayList<Data>) :
        RecyclerView.Adapter<MyDegreeAdapter.MyViewHolder>() {
        inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var p = view.findViewById<FlexboxLayout>(R.id.mParent)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            return MyViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
            )
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            (holder.p as FlexboxLayout).let {
                it.justifyContent = JustifyContent.CENTER
            }

            with(getTextView(list[position].value)) {
                setTag(R.id.quiz_play_order_la_id, id)
                setTag(R.id.meta_id, list[position].metaDataId)
                setTag(R.id.option_text, list[position].value)

                holder.p.addView(this)
            }
        }

        override fun getItemCount(): Int {
            return list.size
        }
    }

    fun getTextView(text: String): TextView {

        val tv = TextView(this)

        val boxParams = FlexboxLayout.LayoutParams(
            convertDpToPixel(this@DegreeQuestionActivity, 75f),
            convertDpToPixel(this@DegreeQuestionActivity, 80f)
        )

        boxParams.setMargins(2, 2, 2, 2)


        tv.apply {
            this.text = text
            this.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            this.gravity = Gravity.CENTER
            setTextColor(Color.parseColor("#FF000000"))
            //background = ContextCompat.getDrawable(this@MainActivity, R.drawable.correct_icon)

            setPadding(10, 10, 10, 10)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, 34.00f)

            //layoutParams = boxParams

            setOnTouchListener(this@DegreeQuestionActivity)

            id = View.generateViewId()
        }

        return tv

    }

    private fun convertDpToPixel(context: Context, unitDp: Float): Int {
        return (unitDp * context.resources.displayMetrics.widthPixels).toInt()
    }


    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {

            val clipData = ClipData.newPlainText("id", "")

            val dragShadowBuilder = View.DragShadowBuilder(v)
            v?.startDragAndDrop(clipData, dragShadowBuilder, v, View.DRAG_FLAG_OPAQUE)
        }

        return true
    }

    override fun onDrag(v: View?, event: DragEvent?): Boolean { //v -> accept drop view
        when (event?.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                isViewDroppedInsideOriginalContainer = false
                isViewDroppedInsideComparative1Container = false
                isViewDroppedInsideSuperlative1Container = false
                isViewDroppedInsideComparative2Container = false
                isViewDroppedInsideSuperlative2Container = false
                isViewDroppedInsideComparative3Container = false
                isViewDroppedInsideSuperlative3Container = false


                val draggedView = event.localState as View
                draggedView.visibility = View.INVISIBLE

                return true
            }
            DragEvent.ACTION_DRAG_ENTERED -> return true

            DragEvent.ACTION_DRAG_LOCATION -> return true

            DragEvent.ACTION_DRAG_EXITED -> return true

            DragEvent.ACTION_DROP -> {

                val draggedView = event.localState as View

                val oldParent = draggedView.parent as ViewGroup

                if (oldParent.id == v?.id)
                    return true

                when (v?.id) {
                    R.id.ll_comparative_1 -> {
                        isViewDroppedInsideComparative1Container = true
                        updateDraggedData(oldParent, ll_comparative_1, draggedView)
                    }

                    R.id.ll_superlative_1 -> {
                        isViewDroppedInsideSuperlative1Container = true
                        updateDraggedData(oldParent, ll_superlative_1, draggedView)
                    }

                    R.id.ll_comparative_2 -> {
                        isViewDroppedInsideComparative2Container = true
                        updateDraggedData(oldParent, ll_comparative_2, draggedView)
                    }

                    R.id.ll_superlative_2 -> {
                        isViewDroppedInsideSuperlative2Container = true
                        updateDraggedData(oldParent, ll_superlative_2, draggedView)
                    }

                    R.id.ll_comparative_3 -> {
                        isViewDroppedInsideComparative3Container = true
                        updateDraggedData(oldParent, ll_comparative_3, draggedView)
                    }

                    R.id.ll_superlative_3 -> {
                        isViewDroppedInsideSuperlative3Container = true
                        updateDraggedData(oldParent, ll_superlative_3, draggedView)
                    }

                    R.id.originalContainer -> {
                        isViewDroppedInsideOriginalContainer = true

                        if (oldParent is LinearLayout) {
                            val previousView = oldParent[0] as TextView
                            fillOptionsList.remove(previousView.text.toString())
                            oldParent.removeAllViews()
                        }

                        mParent.findViewById<View>(
                            draggedView.getTag(R.id.quiz_play_order_la_id).toString().toInt()
                        ).apply {
                            visibility = View.VISIBLE
                        }
                    }

                }

                return true
            }

            DragEvent.ACTION_DRAG_ENDED -> {

                if (!isViewDroppedInsideOriginalContainer && !isViewDroppedInsideComparative1Container && !isViewDroppedInsideSuperlative1Container
                    && !isViewDroppedInsideComparative2Container && !isViewDroppedInsideSuperlative2Container
                    && !isViewDroppedInsideComparative3Container && !isViewDroppedInsideSuperlative3Container
                ) {

                    (event.localState as View).apply {
                        visibility = View.VISIBLE
                    }
                }

                if(fillOptionsList.size != dataList.size){
                    submitButton.visibility = View.GONE
                } else {
                    submitButton.visibility = View.VISIBLE
                }

                return true
            }

            else -> {
                return false
            }
        }

        return true
    }

    private fun updateDraggedData(oldParent: ViewGroup, option: LinearLayout, draggedView: View) {
        if (oldParent is LinearLayout) {
            val previousView = oldParent[0] as TextView
            fillOptionsList.remove(previousView.text.toString())
            oldParent.removeAllViews()
        }


        if (option.childCount != 0) {
            val previousView = option[0] as TextView
            fillOptionsList.remove(previousView.text.toString())
            option.removeAllViews()
            showInOriginalContainer(previousView, draggedView)
        }

        getTextView(draggedView.getTag(R.id.option_text).toString()).let {
            it.setTag(R.id.option_text, draggedView.getTag(R.id.option_text).toString())
            it.setTag(
                R.id.quiz_play_order_la_id,
                draggedView.getTag(R.id.quiz_play_order_la_id)
            )
            it.setTag(R.id.meta_id, draggedView.getTag(R.id.meta_id))
            it.setOnDragListener(reArrangeTextInsideOrderSentenceContainer)
            option.addView(it)
            fillOptionsList.add(it.text.toString())
        }
    }

    private fun showInOriginalContainer(previousView: View, draggedView: View) {
        if (previousView.getTag(R.id.quiz_play_order_la_id) != draggedView.getTag(R.id.quiz_play_order_la_id)) {
            mParent.findViewById<View>(
                previousView.getTag(R.id.quiz_play_order_la_id).toString().toInt()
            ).apply {
                visibility = View.VISIBLE
            }
        }
    }
}