package com.xenix.award.features.home

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.gson.Gson
import com.xenix.award.R
import com.xenix.award.adapter.ListAdapter
import com.xenix.award.base.BaseActivity
import com.xenix.award.features.login.LoginActivity
import com.xenix.award.features.popup.PopupFilter
import com.xenix.award.helper.database.DBHelper
import com.xenix.award.model.Awards
import com.xenix.award.model.Filter
import com.xenix.award.model.rxbusevent.RefreshHomeEvent
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.content_home.*
import rx.Observer


class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, HomeView, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var presenter: HomePresenter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: ListAdapter
    var filter = Filter()

    private var isScroll = false

    var pastVisiblesItems: Int = 0
    var visibleItemCount:Int = 0
    var totalItemCount:Int = 0

    companion object {
        var mIsLoading = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        presenter = HomePresenter(this, this, DBHelper(this))
        setupNavigation()

        presenter.loadData(filter,false)

        BaseActivity.getRxBus.toObserverable().ofType<RefreshHomeEvent>(RefreshHomeEvent::class.java).subscribe(object : Observer<RefreshHomeEvent> {
            override fun onError(e: Throwable?) {
                Log.e("RxBus", "refreshHomeEvent Error: " + e?.localizedMessage)
            }

            override fun onNext(refreshHomeEvent: RefreshHomeEvent?) {
                adapter.clear()
                filter = refreshHomeEvent!!.filter
                presenter.loadData(refreshHomeEvent.filter, false)
            }

            override fun onCompleted() {
                Log.e("RxBus", "refreshKatalogFragmentEvent Completed")
            }
        })
    }

    private fun setupNavigation() {
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_subject)

        nav_view.setNavigationItemSelectedListener(this)
        val item = nav_view.getMenu().getItem(0).setChecked(true)
        onNavigationItemSelected(item)

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);

        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycleView.setLayoutManager(linearLayoutManager)
        adapter = ListAdapter(this)
        recycleView.setAdapter(adapter)

        recycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    visibleItemCount = linearLayoutManager.childCount
                    totalItemCount = linearLayoutManager.itemCount
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition()
                    if (!mIsLoading) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            filter.setOffset(adapter.itemCount)
                            presenter.loadData(filter,true)
                        }
                    }
                }
            }
        })
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        val item = menu.findItem(R.id.action_settings)
        item.setIcon(R.drawable.ic_filter_list)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_settings -> {
                val fragment = PopupFilter()
                val args = Bundle()
                args.putString(Filter::class.java.simpleName, Gson().toJson(filter))
                args.putInt("min", presenter.getMin())
                args.putInt("max", presenter.getMax())
                fragment.setArguments(args)
                fragment.show(getSupportFragmentManager(), "dialog")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {

            }
            R.id.nav_cards -> {

            }
            R.id.nav_profile -> {

            }
            R.id.nav_logout -> {
                goToLoginLogout(LoginActivity::class.java)
                finish()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onSuccessLoadData(awards: ArrayList<Awards>, isScroll: Boolean) {
        this.isScroll = isScroll;
        adapter.populateData(awards, isScroll);
        if(adapter.getItemCount()==0) {
            rl_no_data.setVisibility(View.VISIBLE);
            recycleView.setVisibility(View.GONE);
        } else {
            rl_no_data.setVisibility(View.GONE);
            recycleView.setVisibility(View.VISIBLE);
        }
    }

    override fun onRefresh() {
        filter.setOffset(0);
        presenter.loadData(filter,false);
    }

    override fun hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    override fun showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }
}
