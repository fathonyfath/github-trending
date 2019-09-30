package id.fathonyfath.githubtrending.utils

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import id.fathonyfath.githubtrending.module.GlideApp

fun ImageView.loadImageWithCircleTransformation(fromUri: Uri) {
    GlideApp.with(this)
        .load(fromUri)
        .centerCrop()
        .transition(DrawableTransitionOptions.withCrossFade())
        .apply(RequestOptions.circleCropTransform())
        .into(this)
}

fun ImageView.loadImageWithCircleTransformation(fromDrawable: Drawable) {
    GlideApp.with(this)
        .load(fromDrawable)
        .centerCrop()
        .transition(DrawableTransitionOptions.withCrossFade())
        .apply(RequestOptions.circleCropTransform())
        .into(this)
}