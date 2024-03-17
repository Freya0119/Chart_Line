package com.example.chart.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.chart.DATA_MSG
import com.example.chart.DataViewModel
import com.example.chart.R
import com.example.chart.chart.CustomFillFormatter
import com.example.chart.chart.CustomFillLineRender
import com.example.chart.data.colors
import com.example.chart.data.dateColor
import com.example.chart.data.priceColor
import com.example.chart.databinding.ButtonBinding
import com.example.chart.databinding.LineChartBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

// TODO: alpha

class LineChartFragment() : Fragment() {

    private var _binding: LineChartBinding? = null
    private val binding get() = _binding!!

    private val viewModels: DataViewModel by activityViewModels<DataViewModel>()

    private lateinit var chart: LineChart

    private val stock get() = viewModels.stock

    private var entry = mutableListOf<List<Entry>>()

    // TODO
    private lateinit var btList: List<ButtonBinding>
    private val btStrList = listOf("一年", "三年", "五年")
    private val rangeList = listOf(60f, 36f, 12f)

    private var btFocus = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = LineChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        init()
//

        // TODO: for test
        chart = binding.lc

        viewModels.setLifecycle(viewLifecycleOwner)

        viewModels.dataSets.observe(viewLifecycleOwner) {
            Log.d(DATA_MSG, "DataSets observe")
//            chart.data = LineData(it)

            // TODO: doing
            entry = viewModels.getEntry().toMutableList()
            if (entry.size > 0) {
                val lineData = LineData()
                entry.forEach {
                    // TODO: change to custom
                    val lineDataSet = LineDataSet(it, "")
                    lineData.addDataSet(lineDataSet)
                }
                chart.data = lineData
            }
            chart.invalidate()
        }

//        // TODO: test
//        val aList = listOf(0f, 1f, 2f, 3f, 4f)
//        val bList = listOf(1f, 2f, 3f, 5f, 7f)
//        val entry = aList.zip(bList) { x, y -> Entry(x, y) }
//        val linedataset = LineDataSet(entry, "test")
//        linedataset.setDrawFilled(true)
//
//        val boundLine = entry.map { Entry(it.x, it.y - 1f) }
//        val formatter = CustomFillFormatter(LineDataSet(boundLine, ""))
//        linedataset.fillFormatter = formatter
//
//        val linedata = LineData(linedataset)
//        val newchart = binding.lc
//
//        val render = CustomFillLineRender(newchart, newchart.animator, newchart.viewPortHandler)
//        newchart.renderer = render
//
//        newchart.data = linedata
    }

    private fun setLcRange(range: Float) {
        chart.xAxis.axisMinimum = range
        chart.notifyDataSetChanged()
        chart.invalidate()
    }

    private fun changeFocus(index: Int) {
        btFocus = index
    }

    private fun init() {
        btList = listOf(
            binding.buttonGroup.btOne,
            binding.buttonGroup.btThree,
            binding.buttonGroup.btFive
        )

        btList.mapIndexed { index, buttonBinding ->
            buttonBinding.tv.text = btStrList[index]

            buttonBinding.root.setOnClickListener {
                changeFocus(index)
                updateUnderLine()
                setLcRange(rangeList[index])
            }
        }

        initForm()
        initChart()
    }

    private fun initForm() {
        binding.tvGroup.date.form.setBackgroundColor(dateColor)
        binding.tvGroup.price.form.setBackgroundColor(priceColor)

        val formList = listOf(
            binding.tvGroup.river1.form,
            binding.tvGroup.river2.form,
            binding.tvGroup.river3.form,
            binding.tvGroup.river4.form,
            binding.tvGroup.river5.form,
            binding.tvGroup.river6.form
        )
        formList.mapIndexed { index, textView ->
            textView.setBackgroundColor(colors[index])
        }
    }

    private fun initChart() {
        chart = binding.lc
        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                Log.d(DATA_MSG, "Selected: $e")

                if (e != null) {
                    val river = stock.value?.riverMapInfo?.get(e.x.toInt())

                    val date = river?.yearMonth ?: ""
                    binding.tvGroup.date.tv.text = date.toYearMonth()

                    val price = river?.closingPriceMonth
                    binding.tvGroup.price.tv.text = "股價: $price"

                    val rStandardList = stock.value?.perStandard ?: emptyList()
                    val rPriceList = river?.perStockPriceStandard ?: emptyList()
                    val riverTvList = listOf(
                        binding.tvGroup.river1.tv,
                        binding.tvGroup.river2.tv,
                        binding.tvGroup.river3.tv,
                        binding.tvGroup.river4.tv,
                        binding.tvGroup.river5.tv,
                        binding.tvGroup.river6.tv,
                    )
                    if (rStandardList.size == riverTvList.size && rPriceList.size == riverTvList.size) {
                        riverTvList.mapIndexed { index, tv ->
                            tv.text = "${rStandardList[index]}倍: ${rPriceList[index]}"
                        }
                    }
                }
            }

            override fun onNothingSelected() {
                Log.d(DATA_MSG, "Nothing selected")
            }
        })

        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.axisMinimum = rangeList[btFocus]

        chart.legend.isEnabled = false

        // TODO: no
        if (!viewModels.dataSets.value.isNullOrEmpty()) {
            Log.d(DATA_MSG, "Set formatter")
            val data = viewModels.dataSets.value?.get(viewModels.dataSets.value!!.lastIndex)
            val formatter = CustomFillFormatter(data)

            Log.d(DATA_MSG, "Set render")
            val render = CustomFillLineRender(chart, chart.animator, chart.viewPortHandler)
            chart.renderer = render
        }
    }

    fun String.toYearMonth(): String {
        if (this.length == 6) {
            val year = this.substring(0..3)
            val month = this.substring(4..5)
            return "$year/$month"
        } else {
            return "No date"
        }
    }

    private fun updateUnderLine() {
        btList.mapIndexed { index, buttonBinding ->
            val lineColor = if (index == btFocus) {
                resources.getColor(R.color.black)
            } else {
                // TODO: background color
                resources.getColor(R.color.purple_700)
            }
            buttonBinding.underline.setBackgroundColor(lineColor)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}