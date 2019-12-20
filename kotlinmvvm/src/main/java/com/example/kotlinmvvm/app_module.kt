package com.example.kotlinmvvm

import com.example.kotlinmvvm.vm.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainViewModel = module {
      viewModel {
          MainViewModel()
      }
}
fun main(){

}