package id.fathonyfath.githubtrending.main

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.fathonyfath.githubtrending.R
import id.fathonyfath.githubtrending.model.Repository

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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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

        fun bind(data: Repository) {
            repositoryAuthorView.text = data.author
            repositoryNameView.text = data.name
            repositoryDescriptionView.text = data.description

            bindRepositoryLanguage(data.language, data.languageColor)

            repositoryStarsView.text = data.stars.toString()
            repositoryForksView.text = data.forks.toString()
        }

        private fun bindRepositoryLanguage(language: String?, languageColor: String?) {
            if (language != null && languageColor != null) {
                repositoryLanguageColorView.visibility = View.VISIBLE
                repositoryLanguageView.visibility = View.VISIBLE

                repositoryLanguageView.text = language
                repositoryLanguageColorView.setImageDrawable(
                    ColorDrawable(Color.parseColor(languageColor))
                )
            } else {
                repositoryLanguageColorView.visibility = View.GONE
                repositoryLanguageView.visibility = View.GONE

                repositoryLanguageView.text = ""
            }
        }

        fun hideDetails() {
            repositoryDetailContainerView.visibility = View.GONE
        }

        fun showDetails() {
            repositoryDetailContainerView.visibility = View.VISIBLE
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Repository>() {
            override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean {
                return oldItem == newItem
            }

        }
    }
}