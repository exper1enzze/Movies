package com.example.movies.ui.main.toprated

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.ui.main.MovieListAdapter
import com.example.movies.utils.NpaGridLayoutManager
import com.example.movies.utils.datatypes.NetworkState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_popularity.recyclerViewPosters
import kotlinx.android.synthetic.main.fragment_top_rated.*

@AndroidEntryPoint
class TopRatedFragment : Fragment() {

    companion object {
        private const val POSITION_FIRST = 1
        private const val SPAN_COUNT_DEFAULT = 2
        private const val SPAN_COUNT_ONE = 1
    }

    private lateinit var viewModel: TopRatedViewModel
    private lateinit var layoutManager: NpaGridLayoutManager
    private lateinit var adapter: MovieListAdapter
    private var isScrolling = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_top_rated, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = MovieListAdapter(context)

        initViewModel()
        initListeners()
        initRecyclerView()
    }


    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(TopRatedViewModel::class.java)
        viewModel.getMoviesLiveData().observe(viewLifecycleOwner, Observer {
            adapter.submitList(it.toList())
        })
        viewModel.getNetworkStateLiveData().observe(viewLifecycleOwner, Observer {
            progress_bar_top_rated.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE

            txt_error_top_rated.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.CONNECTION_LOST) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty() && it == NetworkState.CONNECTION_LOST) {
                Snackbar.make(activity!!.VPMain, R.string.connection_error, Snackbar.LENGTH_LONG).show()
            }

            if (it == NetworkState.CONNECTION_RESTORED) {
                Snackbar.make(activity!!.VPMain, getString(R.string.connection_restored), Snackbar.LENGTH_SHORT).show()
                recyclerViewPosters.stopScroll()
                layoutManager.scrollToPosition(POSITION_FIRST)
            }

            if (!viewModel.listIsEmpty()) adapter.setNetState(it)
        })
    }

    private fun initRecyclerView() {
        layoutManager = NpaGridLayoutManager(context, SPAN_COUNT_DEFAULT)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = adapter.getItemViewType(position)
                return if (viewType == MovieListAdapter.VIEWTYPE_MOVIE) SPAN_COUNT_ONE else SPAN_COUNT_DEFAULT
            }
        }
        recyclerViewPosters.layoutManager = layoutManager
        recyclerViewPosters.adapter = adapter
    }

    private fun initListeners() {
        recyclerViewPosters.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL ||
                    newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                val total = adapter.itemCount

                if (isScrolling && (visibleItemCount + pastVisibleItem == total)) {
                    isScrolling = false
                    viewModel.loadTopRated()
                }
            }
        })
    }

}
