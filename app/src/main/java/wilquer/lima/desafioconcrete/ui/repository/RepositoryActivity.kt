package wilquer.lima.desafioconcrete.ui.repository

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.repository_activity.*
import org.jetbrains.anko.toast
import wilquer.lima.desafioconcrete.R
import wilquer.lima.desafioconcrete.data.model.Repository
import wilquer.lima.desafioconcrete.ui.pullrequest.PullRequestActivity
import wilquer.lima.desafioconcrete.util.Constants
import wilquer.lima.desafioconcrete.util.RecyclerClickListener
import wilquer.lima.desafioconcrete.util.adapter.RecyclerRepositoryAdapter

class RepositoryActivity : AppCompatActivity(), RepositoryContract.View, RecyclerClickListener {

    private var presenter: RepositoryContract.Presenter = RepositoryPresenter(this)
    private var listRepositories = ArrayList<Repository>()
    private var countPages = 1
    private val adapterRepository =
        RecyclerRepositoryAdapter(listRepositories, this@RepositoryActivity, this@RepositoryActivity)
    private val linearLayoutManager = LinearLayoutManager(this@RepositoryActivity, RecyclerView.VERTICAL, false)
    private var isLoading = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.repository_activity)

        window.navigationBarColor = Color.BLACK
        if (savedInstanceState != null) {
            listRepositories.addAll(savedInstanceState.getParcelableArrayList(Constants.SAVE_REPOSITORIES)!!)
            countPages = savedInstanceState.getInt(Constants.COUNT_PAGES)
        } else {
            presenter.initView(countPages)
        }


        recycleRepositories.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = adapterRepository
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = linearLayoutManager.itemCount
                    val lastVisible = linearLayoutManager.findLastVisibleItemPosition()
                    if (!isLoading && totalItemCount <= lastVisible + 15 * countPages) {
                        isLoading = true
                        (presenter as RepositoryPresenter).getRepositories(++countPages)
                    }
                }
            })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(Constants.SAVE_REPOSITORIES, listRepositories)
        outState.putInt(Constants.COUNT_PAGES, countPages)
    }

    override fun setProgress(active: Boolean) {
        if (active) {
            progress?.visibility = View.VISIBLE
        } else {
            progress?.visibility = View.INVISIBLE
        }
    }

    override fun repositories(listRepositories: List<Repository>?) {
        if (countPages > 1) {
            val lastPosition = this.listRepositories.size
            this.listRepositories.addAll(listRepositories!!)
            recycleRepositories.adapter?.notifyItemRangeInserted(lastPosition, listRepositories.size)
        } else {
            this.listRepositories.addAll(listRepositories!!)
            recycleRepositories.adapter?.notifyDataSetChanged()
        }
        isLoading = false
    }

    override fun error(msg: String) {
        toast(msg)
    }

    override fun onRecyclerClickListener(position: Int) {
        val intent = Intent(this@RepositoryActivity, PullRequestActivity::class.java)
        intent.putExtra(Constants.REPOSITORY_NAME, listRepositories[position].name)
        intent.putExtra(Constants.CREATOR, listRepositories[position].owner.login)
        startActivity(intent)
    }
}