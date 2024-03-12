package com.example.chart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.chart.chart.CustomMarker
import com.example.chart.chart.CustomLineChart
import com.example.chart.ui.theme.ChartTheme
import com.github.mikephil.charting.data.LineData

const val DATA_MSG = "DATA_MSG"

const val STOCK_ID = 2330

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<DataViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.setLifecycle(this)

        setContent {
            ChartTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainApp(viewModel)
                }
            }
        }
    }
}

@Composable
fun MainApp(vm: DataViewModel) {
    val lineDataSets by vm.dataSets.observeAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        ButtonAndChart(LineData(lineDataSets))
    }
}

@Composable
fun ButtonAndChart(lineData: LineData) {
    val ranges = listOf(12f, 36f, 60f)
    val rangeStr = listOf("1年", "3年", "5年")

    var range by rememberSaveable { mutableFloatStateOf(12f) }

    Surface(modifier = Modifier.fillMaxWidth()) {
        Row(horizontalArrangement = Arrangement.End) {

            for (index in 0..ranges.lastIndex) {

                Button(
                    onClick = { range = ranges[index] },
                    modifier = Modifier
                        .width(45.dp)
                        .height(30.dp)
                        .padding(2.dp),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.secondary
                    ),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    Text(
                        text = rangeStr[index.toInt()],
                        fontSize = 10.sp,
                        style = if (range == ranges[index]) {
                            TextStyle(textDecoration = TextDecoration.Underline)
                        } else {
                            LocalTextStyle.current
                        },
                    )
                }

            }
        }
    }
    ShowLineChart(lineData = lineData, range)
}

@Composable
fun ShowLineChart(lineData: LineData, range: Float) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        AndroidView(
            factory = { it ->
                val lineChart = CustomLineChart(it)
                val markerView = CustomMarker(it, R.layout.marker_view)

                lineChart.marker = markerView
                markerView.chartView = lineChart

                lineChart.setCustom()
                lineChart.setXAXis()

                lineChart.setLegendPosition()
                lineChart.setLegends()

                lineChart
            },
            update = {
                it.data = lineData
                it.setRange((it.xChartMax - range).toFloat())
                it.invalidate()
            },
            modifier = Modifier
                .fillMaxSize(),
        )
    }
}