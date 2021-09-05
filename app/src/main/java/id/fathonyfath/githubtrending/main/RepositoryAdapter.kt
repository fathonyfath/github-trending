package id.fathonyfath.githubtrending.main

import android.animation.AnimatorInflater
import android.animation.StateListAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.fathonyfath.githubtrending.R
import id.fathonyfath.githubtrending.model.Repository
import id.fathonyfath.githubtrending.utils.loadImageWithCircleTransformation
import java.text.NumberFormat
import java.util.*

class RepositoryAdapter(context: Context) :
    ListAdapter<Repository, RepositoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    private val inflater = LayoutInflater.from(context)

    private var expandedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_repository, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val isExpanded = position == expandedPosition

        if (isExpanded) holder.showDetails() else holder.hideDetails()

        holder.itemView.isActivated = isExpanded

        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            val currentlyExpanded = holder.adapterPosition == expandedPosition
            val previousExpandedPosition = expandedPosition
            expandedPosition = if (currentlyExpanded) -1 else holder.adapterPosition

            notifyItemChanged(holder.adapterPosition)
            if (previousExpandedPosition >= 0) {
                notifyItemChanged(previousExpandedPosition)
            }
        }
    }

    fun closeExpandedItem() {
        if (expandedPosition >= 0) {
            val previousExpandedPosition = expandedPosition
            expandedPosition = -1
            notifyItemChanged(previousExpandedPosition)
        }
    }

    fun onSaveInstanceState(): Parcelable {
        return Bundle().apply {
            putInt(EXPANDED_POSITION_KEY, expandedPosition)
        }
    }

    fun onRestoreInstanceState(state: Parcelable) {
        if (state is Bundle) {
            expandedPosition = state.getInt(EXPANDED_POSITION_KEY, -1)
        }

        if (expandedPosition >= 0) {
            notifyItemChanged(expandedPosition)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val context = itemView.context

        private val repositoryAvatarView: ImageView = itemView.findViewById(R.id.repository_avatar)
        private val repositoryAuthorView: TextView = itemView.findViewById(R.id.repository_author)
        private val repositoryNameView: TextView = itemView.findViewById(R.id.repository_name)
        private val repositoryDetailContainerView: LinearLayout =
            itemView.findViewById(R.id.repository_detail_container)
        private val repositoryDescriptionView: TextView =
            itemView.findViewById(R.id.repository_description)
        private val repositoryLanguageColorView: ImageView =
            itemView.findViewById(R.id.repository_language_color)
        private val repositoryLanguageView: TextView =
            itemView.findViewById(R.id.repository_language)
        private val repositoryStarsView: TextView = itemView.findViewById(R.id.repository_stars)
        private val repositoryForksView: TextView = itemView.findViewById(R.id.repository_forks)

        private var animator: StateListAnimator? = null

        fun bind(data: Repository) {
            val author = data.builtBy.first()
            repositoryAvatarView.loadImageWithCircleTransformation(Uri.parse(author.avatar))
            repositoryAuthorView.text = author.username
            repositoryNameView.text = data.repositoryName
            repositoryDescriptionView.text = data.description

            bindRepositoryLanguage(data.language, data.languageColor)

            val numberFormatter = NumberFormat.getNumberInstance(Locale.US)

            repositoryStarsView.text = numberFormatter.format(data.totalStars)
            repositoryForksView.text = numberFormatter.format(data.forks)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                this.bindStateListAnimatorToItem()
            }
        }

        fun hideDetails() {
            repositoryDetailContainerView.visibility = View.GONE
        }

        fun showDetails() {
            repositoryDetailContainerView.visibility = View.VISIBLE
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        private fun bindStateListAnimatorToItem() {
            if (animator == null) {
                animator =
                    AnimatorInflater.loadStateListAnimator(
                        context,
                        R.animator.recycler_view_elevation
                    )
            }
            this.itemView.stateListAnimator = animator
        }

        private fun bindRepositoryLanguage(language: String?, languageColor: String?) {
            if (language != null && languageColor != null) {
                repositoryLanguageColorView.visibility = View.VISIBLE
                repositoryLanguageView.visibility = View.VISIBLE

                repositoryLanguageView.text = language
                repositoryLanguageColorView.loadImageWithCircleTransformation(
                    ColorDrawable(Color.parseColor(languageColor))
                )
            } else {
                repositoryLanguageColorView.visibility = View.GONE
                repositoryLanguageView.visibility = View.GONE

                repositoryLanguageView.text = ""
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Repository>() {
            override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean {
                return oldItem.repositoryName == newItem.repositoryName
            }

            override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean {
                return oldItem == newItem
            }

        }

        private const val EXPANDED_POSITION_KEY = "ExpandedPositionKey"
    }
}