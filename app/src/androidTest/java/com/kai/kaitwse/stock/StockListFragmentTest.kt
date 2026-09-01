package com.kai.kaitwse.stock

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.material.appbar.MaterialToolbar
import com.kai.kaitwse.MainActivity
import com.kai.kaitwse.R
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StockListFragmentTest {

    @Test
    fun launchScreen() {
        launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                val toolbar = activity.findViewById<MaterialToolbar>(R.id.stock_toolbar)
                val recyclerView = activity.findViewById<RecyclerView>(R.id.stock_recycler_view)

                assertNotNull(toolbar)
                assertNotNull(recyclerView)
                assertTrue(recyclerView.width >= 0)
            }
        }
    }
}
