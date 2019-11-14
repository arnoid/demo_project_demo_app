package com.example.csv.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.csv.CsvApplication
import com.example.csv.R
import com.example.csv.ui.main.view.RowsAdapter
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    val adapter = RowsAdapter()

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rclr_records.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        CsvApplication.mainComponent.inject(viewModel)

        viewModel.viewStateLiveData().observe(this, Observer { viewState -> this@MainFragment.onViewState(viewState) })

        viewModel.rowsLiveData().observe(this, Observer { rows ->
            adapter.rows = rows
            adapter.notifyDataSetChanged()
        })

        viewModel.headersLiveData().observe(this, Observer { headers ->
            adapter.headers = headers
            adapter.notifyItemChanged(RowsAdapter.POSITION_HEADER)
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.retrieveRows()
    }

    fun onViewState(viewState: ViewState) {

        //loading indicator
        when (viewState) {
            is ViewState.Loading -> v_loading_indicator.visibility = View.VISIBLE
            else -> v_loading_indicator.visibility = View.GONE
        }

        //on error
        when (viewState) {
            is Error -> {
                Toast.makeText(context, viewState.message, Toast.LENGTH_LONG).show()
            }
        }

        //empty state
        when (viewState) {
            is ViewState.Loading, is ViewState.Error -> {
                v_empty_state.visibility = View.VISIBLE
            }
            else -> {
                v_empty_state.visibility = View.GONE
            }
        }

        //Content
        when (viewState) {
            is ViewState.Loading, is ViewState.Error -> {
                rclr_records.visibility = View.GONE
            }
            else -> {
                rclr_records.visibility = View.VISIBLE
            }
        }

    }


}