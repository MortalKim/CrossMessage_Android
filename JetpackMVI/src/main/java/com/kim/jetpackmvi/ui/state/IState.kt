package com.kim.jetpackmvi.ui.state

/**
 * @ClassName: IState
 * @Description: java类作用描述
 * @Author: kim
 * @Date: 2/19/23 3:17 PM
 */
interface IUiState //重复性事件 可以多次消费
interface ISingleUiEvent //一次性事件，不支持多次消费

/**
 * 空的一次性事件
 */
object EmptySingleEvent : ISingleUiEvent

/**
 * 单次UI消费事件
 *
 * @property state 开放的状态码，由Activity及其ViewModel内部协议
 * @property msg 传递的消息
 */
data class SingleUIEvent(val state:Int, val msg: String = "") : ISingleUiEvent

/**
 * 基础UI状态类，用于保存UI状态
 *
 * @property state 新的开放的UI状态，由Activity及其ViewModel内部协议
 * @property msg 传递的消息
 */
data class UIState(val state:Int, val msg: String = "") : IUiState

/**
 * 通用UI状态类，用于保存UI状态
 *
 * @property state 新的开放的UI状态，由Activity及其ViewModel内部协议
 * @property msg 传递的消息
 */
sealed class CommonUIState() : IUiState{
    object Init : CommonUIState()
    object Loading : CommonUIState()
    data class Error(val msg: String) : CommonUIState()
}


/**
 * 启动Activity事件
 *
 * @param T 要启动的Activity
 * @property clazz 要启动的Activity
 * @property finish 是否结束当前Activity
 */
data class StartActivityEvent<T>(val clazz: Class<T>, val finish: Boolean) : ISingleUiEvent

/**
 * 显示toast事件
 *
 * @property msg toast内容
 */
data class ToastEvent(val msg: String) : ISingleUiEvent

/**
 * Activity结束事件
 */
class FinishEvent() : ISingleUiEvent
