package com.luja93.dbms_benchmark.greendao

import android.content.Context
import androidx.benchmark.junit4.BenchmarkRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.luja93.dbms_benchmark.BaseBenchmark
import com.luja93.dbms_performance_benchmark.greendao.City_GreenDao
import com.luja93.dbms_performance_benchmark.greendao.DaoSession
import com.luja93.dbms_performance_benchmark.greendao.GreenDaoHelpers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * \brief Benchmark which will execute on an Android device.
 * \details The body of [BenchmarkRule.measureRepeated] is measured in a loop, and Android Studio
 * will output the result. Modify the code to see how it affects performance.
 *
 * Build/run the class before you run the tests. Since at the moment of writing this code, Android
 * Studio (3.6.3) does not work well with running the tests managed by the JetPack Benchmark Library,
 * use the following adb commands to successfully run the tests and get the detailed test reports
 * output on the device.
 *
 * For regular run, use:
 *
 * adb shell am instrument -w -e "androidx.benchmark.output.enable" "true" -e \
 * "additionalTestOutputDir" "/sdcard/benchmark-results/greendao/" -e class \
 * com.luja93.dbms_benchmark.greendao.GreenDaoBenchmark \
 * com.luja93.dbms_benchmark.test/androidx.benchmark.junit4.AndroidBenchmarkRunner
 *
 * To run with Android Test Orchestrator, use:
 *
 * adb shell CLASSPATH=$(pm path androidx.test.services) app_process / \
 * androidx.test.services.shellexecutor.ShellMain am instrument -r -w -e targetInstrumentation \
 * com.luja93.dbms_benchmark.test/androidx.benchmark.junit4.AndroidBenchmarkRunner -e \
 * clearPackageData true -e debug false -w -e "androidx.benchmark.output.enable" "true" -e \
 * "additionalTestOutputDir" "/sdcard/benchmark-results/greendao/" -e class \
 * 'com.luja93.dbms_benchmark.greendao.GreenDaoBenchmark' \
 * androidx.test.orchestrator/androidx.test.orchestrator.AndroidTestOrchestrator
 *
 *
 * @author  Luka Leopoldović
 * @version 1.0
 * \date 13/04/2020
 * \copyright
 *     This code and information is provided "as is" without warranty of
 *     any kind, either expressed or implied, including but not limited to
 *     the implied warranties of merchantability and/or fitness for a
 *     particular purpose.
 */
@RunWith(AndroidJUnit4::class)
class GreenDaoBenchmark : BaseBenchmark<City_GreenDao, DaoSession, GreenDaoHelpers>() {

    //region CLASS PROPERTIES
    override val benchmarkRule = BenchmarkRule()
    override val context: Context = ApplicationProvider.getApplicationContext()
    override var cities: List<City_GreenDao> = emptyList()
    override val helpers: GreenDaoHelpers = GreenDaoHelpers

    private lateinit var db: DaoSession
    //endregion


    //region BEFORE/AFTER
    @Before
    fun init() {
        initialize(10_000) {
            db = helpers.buildDb(context)
        }
    }

    @After
    fun destroy() {
        db.database.close()
    }
    //endregion


    //region TESTS
    @Test
    fun benchmark_insert() {
        insert(db)
    }

    @Test
    fun benchmark_read() {
        read(db)
    }

    @Test
    fun benchmark_update() {
        update(db) {
            it.map { city -> city.name = city.name + "_updated"; city }
        }
    }

    @Test
    fun benchmark_delete() {
        delete(db)
    }
    //endregion
}