package com.example.chart.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.core.os.ConfigurationCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.chart.DATA_MSG
import com.example.chart.DataViewModel
import com.example.chart.chart.CustomFillFormatter
import com.example.chart.chart.CustomFillLineRender
import com.example.chart.chart.CustomLineDataSet
import com.example.chart.chart.CustomXAxisFormatter
import com.example.chart.data.btStrList
import com.example.chart.data.colors
import com.example.chart.data.priceColor
import com.example.chart.data.startRangePointList
import com.example.chart.databinding.ButtonBinding
import com.example.chart.databinding.LineChartBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

class LineChartFragment() : Fragment() {

    private var _binding: LineChartBinding? = null
    private val binding get() = _binding!!

    private val viewModels: DataViewModel by activityViewModels<DataViewModel>()

    private val stock get() = viewModels.stock
    private val priceEntry get() = viewModels.priceEntry
    private val riverEntry get() = viewModels.riverEntry

    private lateinit var chart: LineChart

    // button
    private lateinit var btList: List<ButtonBinding>

    private var btFocus = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = LineChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        viewModels.setLifecycle(viewLifecycleOwner)

        viewModels.dataList.observe(viewLifecycleOwner) {
            Log.d(DATA_MSG, "DataSets observe")

            viewModels.updateEntry()
            updateChartData()
            updateXLabel()

            val lastInd = stock.value?.riverMapInfo?.lastIndex ?: 0
            updateText(lastInd)
            // with invalidate
            chart.invalidate()
        }
    }

    private fun updateChartData() {
        if (riverEntry.size > 0) {
            val lineDataSets = mutableListOf<ILineDataSet>()

            if (riverEntry.size == colors.size) {
                val lastInd = riverEntry.lastIndex
                // formatter
                val boundEntry = riverEntry[lastInd].map { Entry(it.x, it.y - 50f) }
                val formatter = CustomFillFormatter(LineDataSet(boundEntry, ""))

                for (i in 0..lastInd) {
                    val entry = riverEntry[i]
                    val lineDataSet = CustomLineDataSet(entry, "")

                    lineDataSet.setCircle()
                    lineDataSet.setLineColor(colors[i], true)
                    lineDataSet.fillFormatter = formatter

                    lineDataSets.add(lineDataSet)
                }
            }

            val priceLine = CustomLineDataSet(priceEntry, "")
            priceLine.setCircle()
            priceLine.setLineColor(priceColor, false)
            lineDataSets.add(priceLine)

            chart.data = LineData(lineDataSets)
            // render
            val render = CustomFillLineRender(chart, chart.animator, chart.viewPortHandler)
            chart.renderer = render
        }
    }

    private fun init() {
        initPageView()
        initBtForm()
        initChart()

        binding.buttonGroup.btOne.root.performClick()
    }

    private fun setChartClickListener() {
        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (e != null) {
                    updateText(e.x.toInt())
                }
            }

            override fun onNothingSelected() {
                Log.d(DATA_MSG, "Nothing selected")
            }
        })
    }

    private fun updateText(x: Int) {
        // 日期: 遠 -> 近
        val sortRiver = stock.value?.riverMapInfo?.sortedBy { it?.yearMonth }

        val riverInfo = sortRiver?.get(x)

        val date = riverInfo?.yearMonth ?: ""
        binding.tvGroup.date.tv.text = "日期: ${date.toYearMonth()}"

        val price = riverInfo?.closingPriceMonth
        binding.tvGroup.price.tv.text = "股價: $price"
        // price: high -> low
        val rStandardList = stock.value?.perStandard?.sortedByDescending { it } ?: emptyList()
        val rPriceList = riverInfo?.perStockPriceStandard?.sortedByDescending { it } ?: emptyList()
        val riverTvList = listOf(
            binding.tvGroup.river1.tv,
            binding.tvGroup.river2.tv,
            binding.tvGroup.river3.tv,
            binding.tvGroup.river4.tv,
            binding.tvGroup.river5.tv,
            binding.tvGroup.river6.tv,
        )
        if (rStandardList.size == riverTvList.size &&
            rPriceList.size == riverTvList.size
        ) {
            riverTvList.mapIndexed { index, tv ->
                tv.text = "${rStandardList[index]}倍: ${rPriceList[index]}"
            }
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

    private fun updateXLabel() {
        val dateList = stock.value?.riverMapInfo?.map {
            it?.yearMonth ?: "No date"
        }?.sortedBy { it }
            ?: emptyList()
        if (dateList.isNotEmpty()) {
            val formatter = CustomXAxisFormatter(dateList)
            chart.xAxis.valueFormatter = formatter
        }
    }

    private fun setLcRange(range: Float) {
        chart.xAxis.axisMinimum = range
        chart.notifyDataSetChanged()
        chart.invalidate()
    }

    // button
    private fun changeFocus(index: Int) {
        btFocus = index
    }

    private fun updateUnderLine() {
        btList.mapIndexed { index, buttonBinding ->
            if (index == btFocus) {
                buttonBinding.underline.isVisible = true
            } else {
                buttonBinding.underline.isVisible = false
            }
        }
    }

    // init
    private fun initPageView() {
        // button list
        btList = listOf(
            binding.buttonGroup.btOne,
            binding.buttonGroup.btThree,
            binding.buttonGroup.btFive
        )
        // button text & listener
        btList.mapIndexed { index, buttonBinding ->
            buttonBinding.tv.text = btStrList[index]

            buttonBinding.root.setOnClickListener {
                changeFocus(index)
                updateUnderLine()
                setLcRange(startRangePointList[index])
            }
        }
    }

    private fun initBtForm() {
        // date without form
        binding.tvGroup.date.form.isVisible = false
        binding.tvGroup.price.form.setBackgroundColor(priceColor)

        val formList = listOf(
            binding.tvGroup.river1.form,
            binding.tvGroup.river2.form,
            binding.tvGroup.river3.form,
            binding.tvGroup.river4.form,
            binding.tvGroup.river5.form,
            binding.tvGroup.river6.form
        )
        formList.mapIndexed { index, form ->
            form.setBackgroundColor(colors[index])
        }
    }

    private fun initChart() {
        chart = binding.lc

        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.labelCount = 4

        chart.axisLeft.spaceTop = 20f
        chart.axisRight.spaceTop = 20f

        chart.legend.isEnabled = false

        chart.description.isEnabled = false

        setChartClickListener()

        updateXLabel()

        setLcRange(startRangePointList[btFocus])
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}