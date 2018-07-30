package vinova.intern.nhomxnxx.mexplorer.device

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View.GONE
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.content_home_layout.*
import vinova.intern.nhomxnxx.mexplorer.adapter.DeviceAdapter
import vinova.intern.nhomxnxx.mexplorer.baseInterface.BaseActivity
import vinova.intern.nhomxnxx.mexplorer.databaseSQLite.DatabaseHandler
import vinova.intern.nhomxnxx.mexplorer.home.HomeInterface
import vinova.intern.nhomxnxx.mexplorer.model.Devices

@SuppressLint("Registered")
class DeviceActivity: BaseActivity(), DeviceInterface.View{
    private lateinit var adapter : DeviceAdapter
    private var mPresenter: DeviceInterface.Presenter = DevicePresenter(this)
    lateinit var token : String
    override fun showList(devices : List<Devices>?) {
        adapter.setData(devices!!)
        adapter.notifyDataSetChanged()
    }

    override fun setPresenter(presenter: HomeInterface.Presenter) {

    }

    override fun showLoading(isShow: Boolean) {

    }

    override fun showError(message: String) {

    }

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.onCreateDrawer()
        fab_add.visibility = GONE
        setDevice()
        token = DatabaseHandler(this).getToken()!!
        if (savedInstanceState==null){
            mPresenter.getDevice(DatabaseHandler(this).getToken())
            swipeContent.isRefreshing = false
        }
    }
    fun setDevice(){
        adapter = DeviceAdapter(this)
        rvContent.layoutManager = LinearLayoutManager(this)
        rvContent.adapter= adapter
        mPresenter.getDevice(DatabaseHandler(this).getToken())
        swipeContent.setOnRefreshListener {
            swipeContent.isRefreshing = false
        }

    }

}