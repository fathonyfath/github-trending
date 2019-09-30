package id.fathonyfath.githubtrending.di

import androidx.lifecycle.ViewModel
import dagger.Lazy

class TestViewModelFactory<VM : ViewModel>(viewModel: VM) : ViewModelFactory<VM>(Lazy { viewModel })