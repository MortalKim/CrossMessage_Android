package com.kim.jetpackmvi.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.kim.jetpackmvi.ui.activity.ui.theme.JetpackComposeMVVMTheme
import com.kim.jetpackmvi.ui.state.IUiState
import com.kim.jetpackmvi.ui.state.SingleUIEvent
import com.kim.jetpackmvi.ui.widget.BoxLoadingDialog
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

abstract class BaseActivity<VM: BaseViewModel<UIState>, UIState:IUiState>() : FragmentActivity() {
    lateinit var viewModel: VM
    private var uIStateClass :Class<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GetViewModel()
            viewModel.uiState = remember { mutableStateOf(viewModel.uiStateFlow.value) }
            lifecycleScope.launchWhenStarted {
                viewModel.startActivityEvent.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .collect{
                        startActivity(Intent(this@BaseActivity, it.clazz), null)
                        if(it.finish) finish()
                    }
            }
            lifecycleScope.launchWhenStarted {
                viewModel.toastEvent.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .collect{
                        Toast.makeText(this@BaseActivity, it.msg, Toast.LENGTH_SHORT).show()
                    }
            }

            lifecycleScope.launchWhenStarted {
                viewModel.finishEvent.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .collect{
                        finish()
                    }
            }

            lifecycleScope.launchWhenStarted {
                viewModel.sUiStateFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .collect{
                        onUIEvent(it)
                    }
            }

            lifecycleScope.launchWhenStarted {
                viewModel.uiStateFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .collect{
                        if(isSubClassOfUIState(it)){
                            onUIStateChange(it)
                            // viewModel.uiState?.value = it
                        }
                    }
            }
            GetContent()
            if(viewModel.loadingState.value){
                BoxLoadingDialog(msg = viewModel.loadingMsg.value)
            }
        }
    }

    fun isSubClassOfUIState(i: Any): Boolean {
        if (uIStateClass == null) {
            val genericSuperclass: Type? = javaClass.genericSuperclass
            if (genericSuperclass != null) {
                val actualTypeArguments: Array<Type> =
                    (genericSuperclass as (ParameterizedType)).actualTypeArguments
                val viewModelClass = actualTypeArguments[1]
                uIStateClass = viewModelClass as Class<*>
            }
        }
        return uIStateClass!!.isAssignableFrom(i.javaClass)
    }

    /**
     * UI状态变更事件
     *
     * @param uiState 当前新的UI状态和消息
     */
    abstract fun onUIStateChange(uiState: UIState)

    /**
     * UI单次消费事件
     *
     * @param uiEvent 当前事件
     */
    abstract fun onUIEvent(uiEvent: SingleUIEvent)

    @Composable
    fun GetViewModel(){
        val genericSuperclass: Type? = javaClass.genericSuperclass
        if (genericSuperclass != null) {
            val actualTypeArguments: Array<Type> = (genericSuperclass as (ParameterizedType)).actualTypeArguments
            val viewModelClass = actualTypeArguments[0] as Class<VM>
            viewModel = remember { mutableStateOf(ViewModelProvider(this)[viewModelClass]) }.value
            //让ViewModel拥有View的生命周期感应
            lifecycle.addObserver(viewModel)
        }
    }

    @Composable
    abstract fun GetContent()
}

@Composable
fun Greeting() {
    Text(
        text = "Hello"
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JetpackComposeMVVMTheme {
        Greeting()
    }
}
