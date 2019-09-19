package com.xenix.award.features.popup

import android.app.Dialog
import android.opengl.Visibility
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.view.View
import android.widget.*
import com.google.gson.Gson
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import com.xenix.award.R
import com.xenix.award.base.BaseActivity
import com.xenix.award.helper.Helper
import com.xenix.award.model.Filter
import com.xenix.award.model.rxbusevent.RefreshHomeEvent


class PopupFilter: BottomSheetDialogFragment(), View.OnClickListener {

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_close -> {
                dismiss()
            }
            R.id.btn_cari -> {
                filter = Filter()
                filter.setPointNeededMin(minOnChange)
                filter.setPointNeededMax(maxOnChange)
                var awardType : String = ""
                if(rg == 1)
                    awardType = "Vouchers"
                else if (rg == 2)
                    awardType = "Products"
                else if (rg == 3)
                    awardType = "Giftcard"
                filter.setAwardType(awardType)

                BaseActivity.getRxBus.send(RefreshHomeEvent(filter))

                dismiss()
            }
            R.id.tv_clear -> {
                filter = Filter()
                lytParent.requestFocus()
                radioGroup.check(R.id.rb_alltype)
                tvMin.text = helper.toPoinFormat(min)
                tvMax.text = helper.toPoinFormat(max)
            }
        }
    }

    private lateinit var mBehavior: BottomSheetBehavior<*>

    private val helper = Helper.instance
    private lateinit var filter: Filter
    private var min: Int = 0
    private var max: Int = 0
    private var minOnChange: Int = min
    private var maxOnChange: Int = max
    private var rg: Int = 0

    private lateinit var tvMin: TextView
    private lateinit var tvMax: TextView
    private lateinit var rangeSeekBar: RangeSeekBar
    private lateinit var radioGroup: RadioGroup
    private lateinit var lytParent: LinearLayout
    private lateinit var tvClear: TextView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view = View.inflate(context, R.layout.popup_filter, null)
        dialog.setContentView(view)
        mBehavior = BottomSheetBehavior.from(view.parent as View)
        mBehavior.setHideable(false)
        mBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO)

        setBottomSheetCallback();

        view.findViewById<ImageButton>(R.id.btn_close).setOnClickListener(this)
        view.findViewById<Button>(R.id.btn_cari).setOnClickListener(this)
        view.findViewById<TextView>(R.id.tv_clear).setOnClickListener(this)
        tvMin = view.findViewById(R.id.tv_min)
        tvMax = view.findViewById(R.id.tv_max)
        rangeSeekBar = view.findViewById(R.id.rangeSeekBar)
        radioGroup = view.findViewById(R.id.radioGroup)
        lytParent = view.findViewById(R.id.lyt_parent)
        tvClear = view.findViewById(R.id.tv_clear)

        val mArgs = arguments ?: return dialog
        val filterString = mArgs.getString(Filter::class.java.getSimpleName())
        filter = Gson().fromJson<Filter>(filterString, Filter::class.java)
        min = mArgs.getInt("min")
        max = mArgs.getInt("max")

        initAdapter();

        return dialog
    }

    private fun initAdapter() {
        tvMin.text = helper.toPoinFormat(min)
        tvMax.text = helper.toPoinFormat(max)
        rangeSeekBar.setRange(min.toFloat(), max.toFloat(), 500.toFloat())
        rangeSeekBar.setProgress(min.toFloat(), max.toFloat())
        rangeSeekBar.setOnRangeChangedListener(object  : OnRangeChangedListener {
            override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

            }

            override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

            }

            override fun onRangeChanged(view: RangeSeekBar?, leftValue: Float, rightValue: Float, isFromUser: Boolean) {
                tvMin.text = helper.toPoinFormat(leftValue.toInt());
                tvMax.text = helper.toPoinFormat(rightValue.toInt());
                minOnChange = leftValue.toInt()
                maxOnChange = rightValue.toInt()

                /*if(leftValue.toInt() != min && rightValue.toInt() != max)
                    tvClear.visibility = View.GONE
                else
                    tvClear.visibility = View.VISIBLE*/
            }

        })

        radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            for (i in 0 until group.childCount) {
                if (group.getChildAt(i).id == checkedId) {
                    when (i) {
                        0 -> {
                            rg = 0
                            tvClear.visibility = View.GONE
                        }
                        1 -> {
                            rg = 1
                            tvClear.visibility = View.VISIBLE
                        }
                        2 -> {
                            rg = 2
                            tvClear.visibility = View.VISIBLE
                        }
                        3 -> {
                            rg = 3
                            tvClear.visibility = View.VISIBLE
                        }
                    }
                }
            }
        })

        if(filter.getAwardType().equals(""))
            radioGroup.check(R.id.rb_alltype)
        else if(filter.getAwardType().equals("Vouchers"))
            radioGroup.check(R.id.rb_vouchers)
        else if(filter.getAwardType().equals("Products"))
            radioGroup.check(R.id.rb_products)
        else if(filter.getAwardType().equals("Giftcard"))
            radioGroup.check(R.id.rb_others)

        /*if(filter.getPointNeededMin() == min && filter.getPointNeededMax() == max)
            tvClear.visibility = View.GONE
        else
            tvClear.visibility = View.VISIBLE*/
    }

    private fun setBottomSheetCallback() {
        mBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (BottomSheetBehavior.STATE_DRAGGING == newState) {
                    mBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }

                if (BottomSheetBehavior.STATE_EXPANDED == newState) {

                }
                if (BottomSheetBehavior.STATE_COLLAPSED == newState) {

                }

                if (BottomSheetBehavior.STATE_HIDDEN == newState) {
                    dismiss()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
    }

    override fun onStart() {
        super.onStart()
        mBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onStop() {
        helper.hideSoftKeypad(activity)
        super.onStop()
    }
}