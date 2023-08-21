package com.kim.jetpackmvi.ui.activity

import android.app.Activity
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kim.jetpackmvi.ui.state.FinishEvent
import com.kim.jetpackmvi.ui.state.IUiState
import com.kim.jetpackmvi.ui.state.SingleUIEvent
import com.kim.jetpackmvi.ui.state.StartActivityEvent
import com.kim.jetpackmvi.ui.state.ToastEvent
import com.kim.jetpackmvi.ui.widget.BoxLoadingDialog
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * @ClassName: BaseViewModel
 * @author Kim
 * @date 2021/2/19 3:17 PM
 * @Description: 包含内部状态的ViewModel
 */
abstract class BaseViewModel<UiState:IUiState>: DefaultLifecycleObserver, ViewModel() {
    fun <T: Activity> startActivity(clazz: Class<T>, finish: Boolean = false) {
        startActivity(clazz, null, finish)
    }

    fun <T:Activity> startActivity(clazz: Class<T>, bundle: Bundle?, finish: Boolean = false) {
        viewModelScope.launch {
            _startActivityEvent.send(StartActivityEvent(clazz, finish))
        }
    }

    fun showToast(msg: String) {
        viewModelScope.launch {
            _toastEvent.send(ToastEvent(msg))
        }
    }

    fun finish(){
        viewModelScope.launch {
            _finishEvent.send(FinishEvent())
        }
    }

    /**
     * 可以重复消费的事件
     */
    private val _uiStateFlow = MutableStateFlow(initUiState())
    var uiState: MutableState<UiState>? = null
    val uiStateFlow: StateFlow<UiState> = _uiStateFlow

    /**
     * 一次性事件 且 一对一的订阅关系
     * 例如：弹Toast、导航Fragment等
     * Channel特点
     * 1.每个消息只有一个订阅者可以收到，用于一对一的通信
     * 2.第一个订阅者可以收到 collect 之前的事件
     */
    private val _sUiStateFlow: Channel<SingleUIEvent> = Channel()
    val sUiStateFlow: Flow<SingleUIEvent> = _sUiStateFlow.receiveAsFlow()

    private val _startActivityEvent: Channel<StartActivityEvent<*>> = Channel()
    val startActivityEvent: Flow<StartActivityEvent<*>> = _startActivityEvent.receiveAsFlow()

    private val _toastEvent: Channel<ToastEvent> = Channel()
    val toastEvent: Flow<ToastEvent> = _toastEvent.receiveAsFlow()

    private val _finishEvent: Channel<FinishEvent> = Channel()
    val finishEvent: Flow<FinishEvent> = _finishEvent.receiveAsFlow()

    /**
     * 加载弹窗显示状态
     */
    val loadingState = mutableStateOf(false)
    val loadingMsg = mutableStateOf("请稍后...")

    protected abstract fun initUiState(): UiState

    fun sendUiState(uiState: UiState) {
        _uiStateFlow.update { uiState }
        this.uiState?.value = uiState
    }

    fun sendSingleUiState(sUiState: SingleUIEvent) {
        viewModelScope.launch {
            _sUiStateFlow.send(sUiState)
        }
    }

    /**
     * 为非Activity的Composable 注册额外的状态，比如加载框等
     * Activity的ViewModel不需要注册
     */
    @Composable
    fun RegisterAdditionalUIStateForComposable(){
        uiState = remember { mutableStateOf(uiStateFlow.value) }
        if(loadingState.value){
            BoxLoadingDialog(msg = loadingMsg.value)
        }
//        val coroutineScope = rememberCompositionContext()
//        LocalContext.current .lifecycleScope.launchWhenStarted {
//            startActivityEvent.flowOn(coroutineScope)
//                .collect{
//                    startActivity(Intent(this@BaseActivity, it.clazz), null)
//                    if(it.finish) finish()
//                }
//        }
//        lifecycleScope.launchWhenStarted {
//            viewModel.toastEvent.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
//                .collect{
//                    Toast.makeText(this@BaseActivity, it.msg, Toast.LENGTH_SHORT).show()
//                }
//        }
//
//        lifecycleScope.launchWhenStarted {
//            viewModel.finishEvent.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
//                .collect{
//                    finish()
//                }
//        }
//
//        lifecycleScope.launchWhenStarted {
//            viewModel.sUiStateFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
//                .collect{
//                    onUIEvent(it)
//                }
//        }

//        lifecycleScope.launchWhenStarted {
//            viewModel.uiStateFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
//                .collect{
//                    if(isSubClassOfUIState(it)){
//                        onUIStateChange(it)
//                        // viewModel.uiState?.value = it
//                    }
//                }
//        }
    }
}
