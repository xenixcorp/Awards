package com.xenix.award.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.xenix.award.R
import com.xenix.award.helper.GlideApp
import com.xenix.award.helper.skeleton.SkeletonGroup
import com.xenix.award.model.Awards
import kotlinx.android.synthetic.main.list_awards_vertical.view.*


class ListAdapter(val context: Context) : RecyclerView.Adapter<ListAdapter.AwardsHolder>() {

    private var helper = com.xenix.award.helper.Helper.instance
    private val DEFAULT_MAX_POSITION = -1
    private var maxPosition = DEFAULT_MAX_POSITION
    private var awards = ArrayList<Awards>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): AwardsHolder {
        return AwardsHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.list_awards_vertical, viewGroup, false))
    }

    class AwardsHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.tv_name
        val tvPoin = view.tv_poin
        val tvType = view.tv_type
        val ivImage = view.iv_image
        val skeletonGroup = view.skeletonGroup
    }

    override fun getItemCount(): Int = awards.size

    override fun onBindViewHolder(holder: AwardsHolder, position: Int) {
        holder.skeletonGroup.setupFinishingAnimation()
        holder.skeletonGroup.finishAnimation()
        holder.skeletonGroup.setShowSkeleton(false)
        holder.skeletonGroup.setShowSkeleton(true)
        holder.skeletonGroup.startAnimation()

        holder.tvName.text = awards[position].name
        holder.tvPoin.text = helper.toPoinFormat(awards[position].point_needed)
        holder.tvType.text = awards[position].award_type

        if (awards[position].award_type.equals("Vouchers", ignoreCase = true)) {
            holder.tvType.background = context.getDrawable(R.drawable.textview_rounded_royalblue)
        } else if (awards[position].award_type.equals("Giftcard", ignoreCase = true)) {
            holder.tvType.background = context.getDrawable(R.drawable.textview_rounded_accent)
        } else if (awards[position].award_type.equals("Products", ignoreCase = true)) {
            holder.tvType.background = context.getDrawable(R.drawable.textview_rounded_chocolate)
        }

        if(awards[position].image_url.equals("")) {
            checkPosition(holder.skeletonGroup, position)
            GlideApp.with(context).clear(holder.ivImage)
            holder.ivImage.setImageDrawable(null)
            holder.ivImage.setVisibility(View.GONE)
        } else if(!awards[position].image_url.equals("")) {
            holder.ivImage.setVisibility(View.VISIBLE)
            GlideApp.with(context).load(awards[position].image_url)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(p0: GlideException?, p1: Any?, p2: com.bumptech.glide.request.target.Target<Drawable>?, p3: Boolean): Boolean {
                            checkPosition(holder.skeletonGroup, position)
                            holder.ivImage.setImageDrawable(null)
                            holder.ivImage.setVisibility(View.GONE)
                            return false
                        }
                        override fun onResourceReady(p0: Drawable?, p1: Any?, p2: com.bumptech.glide.request.target.Target<Drawable>?, p3: DataSource?, p4: Boolean): Boolean {
                            checkPosition(holder.skeletonGroup, position)
                            holder.ivImage.setVisibility(View.VISIBLE)
                            return false
                        }
                    })
                    .apply(RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                    .into(holder.ivImage)
        } else {
            checkPosition(holder.skeletonGroup, position)
            holder.ivImage.visibility = View.GONE
        }
    }

    private fun checkPosition(skeletonGroup: SkeletonGroup, position: Int) {
        if (maxPosition < position)
            maxPosition = position
        else
            skeletonGroup.setupFinishingAnimation()
        skeletonGroup.finishAnimation()
        skeletonGroup.setShowSkeleton(false)
    }

    fun populateData(awards: ArrayList<Awards>, isScroll: Boolean) {
        if (isScroll) {
            val positionStart = this.awards.size + 1
            this.awards.addAll(awards)
            notifyItemRangeInserted(positionStart, awards.size)
        } else {
            maxPosition = DEFAULT_MAX_POSITION
            this.awards = awards
            notifyDataSetChanged()
        }
    }

    fun clear() {
        awards.clear()
        notifyDataSetChanged()
    }
}