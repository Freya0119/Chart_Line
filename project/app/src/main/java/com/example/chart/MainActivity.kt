package com.example.chart

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.chart.ui.theme.ChartTheme
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

const val DATA_MSG = "DATA_MSG"
const val CHART_MSG = "CHART_MSG"

const val STOCK_ID = 2330

val testList = mutableListOf(Entry(0f, 0f), Entry(1f, 1f), Entry(2f, 4f))

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<DataViewModel>()

    private var entry = mutableStateOf(listOf<Entry>())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.setLifecycle(this)

        viewModel.entry.observe(this) {
//            entry.value = it
        }

        setContent {
            ChartTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
//                    ToLineChart(viewModel)
                    TestNextUnit(viewModel)
                }
            }
        }
    }
}

@Composable
fun TestNextUnit(vm: DataViewModel) {
    val lineDataSets by vm.dataSets.observeAsState()

    LaunchedEffect(key1 = lineDataSets, block = {
        Log.d(CHART_MSG, "LineDataSets entry count: ${lineDataSets?.first()?.entryCount}")
    })

    Column(modifier = Modifier.fillMaxSize()) {
        TestValueChange(LineData(lineDataSets))
    }
}

@Composable
fun TestValueChange(lineData: LineData) {
    var text by rememberSaveable { mutableStateOf("Chart") }

    LaunchedEffect(key1 = text, block = {
//        Log.d(CHART_MSG, "LineData change: ${lineData.dataSets.size}")
//        lineData.notifyDataChanged()
        Log.d(CHART_MSG, "text change: $text")
    })

    Column {
        AndroidView(
            factory = { it ->
                val markerView = MyMarker(it, R.layout.marker_view)
                markerView.let {
                    it.setOffset(10f, 100f)
                }
                val lineChart = LineChart(it)
                markerView.chartView = lineChart
                lineChart.marker = markerView
                markerView.setLegend()
                lineChart.legend.let {
//                    it.position // ?
                    it.verticalAlignment = Legend.LegendVerticalAlignment.TOP // legend position
                    it.orientation = Legend.LegendOrientation.VERTICAL  // 縱向
                }
                lineChart.data = lineData
                lineChart
            }, modifier = Modifier.fillMaxSize(),
            update = {
                Log.d(CHART_MSG, "update, ${lineData.dataSets.first().entryCount}")
                it.data = lineData
                Log.d(CHART_MSG, "marker offset: ${it.marker.offset.x}")
                it.invalidate()
            }
        )
    }
}

@Composable
fun ToLineChart(viewModel: DataViewModel) {
    val data by viewModel.dataList.observeAsState()

//    if (entry.size > 0) {
//        Log.d(DATA_MSG, "Entry: ${entry.size}, x: ${entry[0].x}, y: ${entry[0].y}")
//    }
//    val entryList by remember { mutableStateOf(entry) }

//    val lineDataSet = LineDataSet(entryList, "月平均收盤價")
//    lineDataSet.values = entryList
//    Log.d(DATA_MSG, "LineDataSet values: ${lineDataSet.values.size}, entryList: ${entryList.size}")
//    LaunchedEffect(entryList){
//        Log.d(DATA_MSG, "Entry change")
//    }

    val entry by remember { mutableStateOf(viewModel.entry) }
    val lineDataSet = LineDataSet(entry.value, "月平均收盤價")
    Log.d(CHART_MSG, "Entry: ${entry.value?.size}, viewModel: ${viewModel.entry.value?.size}")
    LaunchedEffect(key1 = entry, block = {
        lineDataSet.values = entry.value
        Log.d(CHART_MSG, "EntryX: ${entry.value?.size}")
    })
    AndroidView(
        factory = { context ->
            val testChart = LineChart(context)
//            val lineDataSet = LineDataSet(entry.value, "月平均收盤價")
            lineDataSet.let {
//                it.setDrawFilled(true)  // 區域填色
                it.formSize = 12f   // label size
                it.lineWidth = 4f
                it.valueTextSize = 12f
                it.setDrawCircleHole(true)  // 點為實心 空心
                it.mode = LineDataSet.Mode.CUBIC_BEZIER   // 線模式: 默認折線
            }
            val lineData = LineData(lineDataSet)
            lineData.let {
//                it.setValueTextSize(32f)    // 和 linedataset.valuetextsize 一樣
            }

            val xLimitLine = LimitLine(1f, "x limit")   // 臨界線

            testChart.let {
                it.xAxis.let {
                    it.mAxisMinimum = 0f // ?
                    it.mAxisMaximum = 1f // ?
                    it.position = XAxisPosition.BOTTOM    // x軸 label位置 in or out
                    it.gridColor = Color.YELLOW
                    it.granularity = 2f   // x 顯示間隔
//                    it.gridLineWidth = 12f  // 豎 軸線
//                    it.gridColor = R.color.black
//                    it.isEnabled = true    // 默認 true
//                    it.axisLineWidth= 8f  // 橫 x線
                }
                it.axisLeft.let {
                    it.mAxisMinimum = 1f
                    it.mAxisMaximum = 120f
                    it.textColor = R.color.purple_700
//                    it.valueFormatter.set // label文字轉換
//                    it.spaceBottom = 20f    // 底部間距
//                    it.spaceTop = 20f
//                    it.zeroLineColor = R.color.black
                    it.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)  // 沒看懂?
                    it.setLabelCount(4) // 太難了
//                    it.isInverted = true  // y_left軸相對 顛倒
                    it.addLimitLine(xLimitLine)
                }
                it.description.let {
                    it.text = "TEST"
                    it.xOffset = 10f    // description offset
                    it.yOffset = 20f
                }
//                it.setViewPortOffsets(1f,1f,1f,0f)  // 相對縮放比例?
//                it.clipBounds.set(0,1,0,1)  // clip?
//                it.setBackgroundColor(R.color.purple_700)
                it.setScaleEnabled(true)    // 縮放
                it.setPinchZoom(false)   // ?邊界
                it.legend.let {
//                    it.isEnabled = true //  圖例(左下角)，默認true
                }
                it.isAutoScaleMinMaxEnabled = true
            }

            testChart.data = lineData
            testChart
        }, update = {
            Log.d(DATA_MSG, "update")
            it.data.notifyDataChanged()
            it.notifyDataSetChanged()
            it.invalidate()
        }, modifier = Modifier
            .fillMaxSize()
            .padding(Dp(8f))
    )
}

//@Preview(showBackground = true)
//@Composable
//fun PreToLineChart() {
////    ToLineChart(testList)
//}

@Preview(showBackground = true)
@Composable
fun PreTest() {
    val vm: DataViewModel = DataViewModel()
    TestNextUnit(vm)
}

fun setLineChart() {
//    val entry = listOf<Entry>(Entry(date, closeingPrice))
//
//    val lineDataSet = LineDataSet(entry, "Label") // set entry
//    lineDataSet.let {
//        it.color = R.color
//        it.circleColors = Color.Black
//        it.valueTextSize = 16f
//        it.isHighlightEnabled = true    // 虛線
//        it.setDrawFilled(true)
//        it.setFillFormatter(object : IFillFormatter {
//            override fun getFillLinePosition(dataSet: ILineDataSet?, dataProvider: LineDataProvider?): Float {
//                TODO("Not yet implemented") // 填充區域?
//            }
//        })
//        it.fillColor = Color.Black  // dialog?
//
//        it.axisDependency = YAxis.AxisDependency.LEFT   // 軸 依賴
//    }
//
//    val dataSet = LineData(lineDataSet) // data sets?
//    dataSet.let {
//        it.setAxis
//    }
//
//    val lineChart = LineChart(context: Context()) // = dataSet   // pic
//    lineChart.let {
//        it.setTouchEnabled(true)
//        it.background = Color.Black
//
//        it.xAxis.let {
//            it.mAxisMinimum = 200f
//            it.mAxisMaximum = 500f
//            it.isDrawGridLinesEnabled = true
//        }
//        it.axisLeft.let {
//            it.textColor = Color.Blue
//            it.granularity = true
//        }
//        it.axisRight.let {
//            it.textColor = Color.Red
//        }
//    }
}