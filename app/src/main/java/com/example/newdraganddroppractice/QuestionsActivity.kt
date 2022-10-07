package com.example.newdraganddroppractice

import android.content.ClipData
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.JustifyContent

class QuestionsActivity : AppCompatActivity(), View.OnDragListener, View.OnTouchListener {

    private lateinit var originalContainer: RecyclerView
    private lateinit var mParent: ConstraintLayout

    private lateinit var option1: LinearLayout
    private lateinit var option2: LinearLayout
    private lateinit var option3: LinearLayout
    private lateinit var option4: LinearLayout
    private lateinit var dataList: ArrayList<Data>

    private var isViewDroppedInsideOriginalContainer = false
    private var isViewDroppedInsideOption1Container = false
    private var isViewDroppedInsideOption2Container = false
    private var isViewDroppedInsideOption3Container = false
    private var isViewDroppedInsideOption4Container = false

    private lateinit var reArrangeTextInsideOrderSentenceContainer: View.OnDragListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mParent = findViewById(R.id.mParent)
        originalContainer = findViewById(R.id.originalContainer)
        option1 = findViewById(R.id.option1)
        option2 = findViewById(R.id.option2)
        option3 = findViewById(R.id.option3)
        option4 = findViewById(R.id.option4)

        dataList = getData()

        originalContainer.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                initAdapter()

                originalContainer.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }

        })

        originalContainer.setOnDragListener(this)
        option1.setOnDragListener(this)
        option2.setOnDragListener(this)
        option3.setOnDragListener(this)
        option4.setOnDragListener(this)


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

                    when (v.id) {
                        R.id.option1 -> {
                            isViewDroppedInsideOption1Container = true
                            updateDraggedData(oldParent, option1, draggedView)
                        }

                        R.id.option2 -> {
                            isViewDroppedInsideOption2Container = true
                            updateDraggedData(oldParent, option2, draggedView)
                        }

                        R.id.option3 -> {
                            isViewDroppedInsideOption3Container = true
                            updateDraggedData(oldParent, option3, draggedView)
                        }

                        R.id.option4 -> {
                            isViewDroppedInsideOption4Container = true
                            updateDraggedData(oldParent, option4, draggedView)
                        }
                    }

                    true
                }

                DragEvent.ACTION_DRAG_ENDED -> {

                    val v = view?.parent as ViewGroup


                    true
                }
                else -> {

                    true
                }
            }
        }
    }

    private fun initAdapter() {
        originalContainer.layoutManager = GridLayoutManager(this, dataList.size)
        originalContainer.adapter = MyAdapter(dataList)

//        with(originalContainer){
//            setHasFixedSize(true)
//            val layoutManager = FlexboxLayoutManager(this@MainActivity)
//            layoutManager.flexWrap = FlexWrap.WRAP
//            layoutManager.flexDirection = FlexDirection.ROW
//            layoutManager.justifyContent = JustifyContent.FLEX_START
//            layoutManager.alignItems = AlignItems.FLEX_START
//            originalContainer.layoutManager = layoutManager
//            adapter = MyAdapter(dataList)
//        }
    }

    private fun getData(): ArrayList<Data> {

        val list = ArrayList<Data>()

        list.add(Data("Mid-vein", "1"))
        list.add(Data("Apex", "2"))
        list.add(Data("Vein", "3"))
        list.add(Data("Blade", "4"))

        return list

    }


    override fun onDrag(v: View?, event: DragEvent?): Boolean { //v -> accept drop view
        when (event?.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                isViewDroppedInsideOriginalContainer = false
                isViewDroppedInsideOption1Container = false
                isViewDroppedInsideOption2Container = false
                isViewDroppedInsideOption3Container = false
                isViewDroppedInsideOption4Container = false

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
                    R.id.option1 -> {
                        isViewDroppedInsideOption1Container = true
                        updateDraggedData(oldParent, option1, draggedView)
                    }

                    R.id.option2 -> {
                        isViewDroppedInsideOption2Container = true
                        updateDraggedData(oldParent, option2, draggedView)
                    }

                    R.id.option3 -> {
                        isViewDroppedInsideOption3Container = true
                        updateDraggedData(oldParent, option3, draggedView)
                    }

                    R.id.option4 -> {
                        isViewDroppedInsideOption4Container = true
                        updateDraggedData(oldParent, option4, draggedView)
                    }

                    R.id.originalContainer -> {
                        isViewDroppedInsideOriginalContainer = true

                        if (oldParent is LinearLayout && oldParent.childCount != 1) {
                            oldParent.removeViewAt(1)
                            oldParent[0].visibility = View.VISIBLE
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

                if (!isViewDroppedInsideOriginalContainer && !isViewDroppedInsideOption1Container && !isViewDroppedInsideOption2Container
                    && !isViewDroppedInsideOption3Container && !isViewDroppedInsideOption4Container
                ) {

                    (event.localState as View).apply {
                        visibility = View.VISIBLE
                    }
                }
                return true
            }

            else -> {
                return false
            }
        }
    }

    private fun updateDraggedData(oldParent: ViewGroup, option: LinearLayout, draggedView: View) {
        if (oldParent is LinearLayout && oldParent.childCount != 1) {
            oldParent.removeViewAt(1)
            oldParent[0].visibility = View.VISIBLE
        }


        if (option.childCount != 1) {
            val previousView = option[1]
            option.removeViewAt(1)
            showInOriginalContainer(previousView, draggedView)
        } else{
            option[0].visibility = View.GONE
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

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {

            val clipData = ClipData.newPlainText("id", "")

            val dragShadowBuilder = View.DragShadowBuilder(v)
            v?.startDragAndDrop(clipData, dragShadowBuilder, v, View.DRAG_FLAG_OPAQUE)
        }

        return true
    }

    fun getTextView(text: String): TextView {
        val tv = TextView(this)

        val boxParams = FlexboxLayout.LayoutParams(
            convertDpToPixel(this@QuestionsActivity, 75f),
            convertDpToPixel(this@QuestionsActivity, 80f)
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
            typeface = Typeface.DEFAULT_BOLD

            //layoutParams = boxParams

            setOnTouchListener(this@QuestionsActivity)

            id = View.generateViewId()
        }

        return tv

    }

    private fun convertDpToPixel(context: Context, unitDp: Float): Int {
        return (unitDp * context.resources.displayMetrics.widthPixels).toInt()
    }

    inner class MyAdapter(var list: ArrayList<Data>) :
        RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
        inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var p = view.findViewById<FlexboxLayout>(R.id.mParent)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
            return MyViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
            )
        }

        override fun onBindViewHolder(holder: MyAdapter.MyViewHolder, position: Int) {

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

}