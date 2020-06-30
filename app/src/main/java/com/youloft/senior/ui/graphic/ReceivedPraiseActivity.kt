package com.youloft.senior.ui.graphic

import android.content.Intent
import android.graphics.Color
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.youloft.coolktx.dp2px
import com.youloft.coolktx.launchIOWhenCreated
import com.youloft.core.base.BaseActivity
import com.youloft.core.base.BaseVMActivity
import com.youloft.senior.R
import com.youloft.senior.bean.Comment
import com.youloft.senior.bean.Praise
import com.youloft.senior.itembinder.ReceivedCommentViewBinder
import com.youloft.senior.itembinder.ReceivedPraiseForCommentViewBinder
import com.youloft.senior.itembinder.ReceivedPraiseForPostViewBinder
import com.youloft.senior.net.ApiHelper
import com.youloft.senior.ui.detail.DetailActivity
import com.youloft.senior.widgt.RecycleViewDivider
import kotlinx.android.synthetic.main.activity_received_comment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author you
 * @create 2020/6/30
 * @desc 收到的赞
 */
class ReceivedPraiseActivity : BaseActivity() {

    private val mAdapter = MultiTypeAdapter(mutableListOf<Praise>())

    override fun getLayoutResId(): Int {
        return R.layout.activity_received_comment
    }

    override fun initView() {
        common_title.onBack { finish() }
        common_title.setTitle("收到的赞")

        mAdapter.register(Praise::class)
            .to(
                ReceivedPraiseForPostViewBinder { postId, type ->
                    DetailActivity.start(this, postId, type)
                },
                ReceivedPraiseForCommentViewBinder { postId, type ->
                    DetailActivity.start(this, postId, type)
                }
            ).withKotlinClassLinker { _, item ->
                when (item.praiseType) {
                    0 -> {
                        ReceivedPraiseForPostViewBinder::class
                    }
                    else -> {
                        ReceivedPraiseForCommentViewBinder::class
                    }
                }
            }

        rv_comment.run {
            layoutManager = LinearLayoutManager(applicationContext)
            addItemDecoration(
                RecycleViewDivider(
                    application,
                    LinearLayoutManager.HORIZONTAL,
                    10.dp2px, Color.parseColor("#F6F6F6")
                )
            )

            adapter = mAdapter

        }
    }

    override fun initData() {
        showLoading()
        lifecycleScope.launchIOWhenCreated {
            val result = ApiHelper.api.getUnReadComment()
            withContext(Dispatchers.Main) {
                dismissLoading()
                ApiHelper.executeResponse(result, {
                    mAdapter.items = it
                    mAdapter.notifyDataSetChanged()
                })
            }

        }
    }

    companion object {
        fun start(context: FragmentActivity) {
            val intent = Intent(context, ReceivedPraiseActivity::class.java)
            context.startActivity(intent)
        }
    }
}