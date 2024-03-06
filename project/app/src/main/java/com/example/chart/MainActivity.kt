package com.example.chart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
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
        TestValueChange(LineData(lineDataSets))
    }
}

@Composable
fun TestValueChange(lineData: LineData) {
    val setsCount = lineData.maxEntryCountSet.entryCount.toFloat()
    var minRange by rememberSaveable { mutableFloatStateOf(setsCount - 12f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { minRange = setsCount - 12f }) {
                Text(text = "一年")
            }
            Button(onClick = { minRange = setsCount - 36f }) {
                Text(text = "三年")
            }
            Button(onClick = { minRange = setsCount - 60f }) {
                Text(text = "五年")
            }
        }
        AndroidView(
            factory = { it ->
                val lineChart = CustomLineChart(it)
                val markerView = CustomMarker(it, R.layout.marker_view)

                lineChart.marker = markerView
                markerView.chartView = lineChart

                markerView.setLegends()
                markerView.setLegendPosition()

                lineChart.setCustom()

                lineChart.xAxis.let {
                    it.setLabelCount(3)
                }

                lineChart
            },
            update = {
                it.data = lineData
                it.setRange(minRange)
                it.invalidate()
            },
            modifier = Modifier
                .fillMaxSize(),
        )
    }
}