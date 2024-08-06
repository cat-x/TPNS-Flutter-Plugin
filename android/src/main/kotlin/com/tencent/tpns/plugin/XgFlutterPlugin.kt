@file:Suppress("DEPRECATION")

package com.tencent.tpns.plugin

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.annotation.Keep
import com.tencent.android.tpush.XGIOperateCallback
import com.tencent.android.tpush.XGPushConfig
import com.tencent.android.tpush.XGPushConstants
import com.tencent.android.tpush.XGPushManager
import com.tencent.tpns.baseapi.XGApiConfig
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.PluginRegistry.Registrar
import io.flutter.plugin.common.StandardMethodCodec


public class XgFlutterPlugin : FlutterPlugin, MethodCallHandler {

    constructor() {
        instance = this
    }

    constructor(binding: FlutterPlugin.FlutterPluginBinding, methodChannel: MethodChannel) {
        context = binding.applicationContext
        channel = methodChannel
        instance = this
    }

    constructor(mRegistrar: Registrar, mChannel: MethodChannel) {
        context = mRegistrar.context().applicationContext
        channel = mChannel
        instance = this
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: XgFlutterPlugin
        lateinit var channel: MethodChannel
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null

        @Keep
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val messenger = registrar.messenger()
            val taskQueue =
                messenger.makeBackgroundTaskQueue()
            val useChannel: MethodChannel = if (checkChannelInit()) {
                channel
            } else {
                MethodChannel(messenger, "tpns_flutter_plugin", StandardMethodCodec.INSTANCE, taskQueue)
            }
            if (checkInstanceInit()){
                context = registrar.context().applicationContext
                channel = useChannel
                useChannel.setMethodCallHandler(instance)
            }else{
                useChannel.setMethodCallHandler(XgFlutterPlugin(registrar, useChannel))
            }


            Log.i(TAG, "methodChannel registerWith XgFlutterPlugin")
            Log.i(TAG, "instance = $instance")
        }

        fun checkChannelInit(): Boolean {
            return try {
                this::channel.isInitialized
            } catch (e: Exception) {
                false
            }
        }

        fun checkInstanceInit(): Boolean {
            return try {
                this::instance.isInitialized
            } catch (e: Exception) {
                false
            }
        }

        //        private const val  TAG: String = "XgFlutterPlugin | Flutter"
        private const val TAG: String = "TPNSPlugin | Flutter"
    }

    override fun onMethodCall(p0: MethodCall, p1: MethodChannel.Result) {
        Log.i(TAG, p0.method)
        if (context == null) {
            Log.i(TAG, "调用native的函数" + p0.method + "失败context==null")
            return
        }
        when (p0.method) {
            Extras.FOR_FLUTTER_METHOD_REG_PUSH -> regPush(p0, p1)
            Extras.FOR_FLUTTER_METHOD_UNREGISTER_PUSH -> stopXg(p0, p1)
            Extras.FOR_FLUTTER_METHOD_SET_TAG -> setXgTag(p0, p1)
            Extras.FOR_FLUTTER_METHOD_SET_TAGS -> setXgTags(p0, p1)
            Extras.FOR_FLUTTER_METHOD_ADD_TAGS -> addXgTags(p0, p1)
            Extras.FOR_FLUTTER_METHOD_DELETE_TAG -> deleteXgTag(p0, p1)
            Extras.FOR_FLUTTER_METHOD_DELETE_TAGS -> deleteXgTags(p0, p1)
            Extras.FOR_FLUTTER_METHOD_CLEAN_TAGS -> cleanXgTags(p0, p1)
            Extras.FOR_FLUTTER_METHOD_GET_TOKEN -> xgToken(p0, p1)
            Extras.FOR_FLUTTER_METHOD_GET_SDK_VERSION -> xgSdkVersion(p0, p1)
            Extras.FOR_FLUTTER_METHOD_BIND_ACCOUNT -> bindAccount(p0, p1)
            Extras.FOR_FLUTTER_METHOD_APPEND_ACCOUNT -> appendAccount(p0, p1)
            Extras.FOR_FLUTTER_METHOD_DEL_ACCOUNT -> delAccount(p0, p1)
            Extras.FOR_FLUTTER_METHOD_DEL_ALL_ACCOUNT -> delAllAccount(p0, p1)
            Extras.FOR_FLUTTER_METHOD_UPSERT_ATTRIBUTES -> upsertAttributes(p0, p1)
            Extras.FOR_FLUTTER_METHOD_DEL_ATTRIBUTES -> delAttributes(p0, p1)
            Extras.FOR_FLUTTER_METHOD_CLEAR_AND_APPEND_ATTRIBUTES -> clearAndAppendAttributes(p0, p1)
            Extras.FOR_FLUTTER_METHOD_CLEAR_ATTRIBUTES -> clearAttributes(p0, p1)
            Extras.FOR_FLUTTER_METHOD_ENABLE_OTHER_PUSH -> enableOtherPush(p0, p1)
            Extras.FOR_FLUTTER_METHOD_ENABLE_OTHER_PUSH2 -> enableOtherPush2(p0, p1)
            Extras.FOR_FLUTTER_METHOD_GET_OTHER_PUSH_TOKEN -> getOtherPushToken(p0, p1)
            Extras.FOR_FLUTTER_METHOD_GET_OTHER_PUSH_TYPE -> getOtherPushType(p0, p1)
            Extras.FOR_FLUTTER_METHOD_ENABLE_PULL_UP_OTHER_APP -> enablePullUpOtherApp(p0, p1)
            Extras.FOR_FLUTTER_METHOD_SET_BADGE_NUM -> setBadgeNum(p0, p1)
            Extras.FOR_FLUTTER_METHOD_RESET_BADGE_NUM -> resetBadgeNum(p0, p1)
            Extras.FOR_FLUTTER_METHOD_CANCEL_ALL_NOTIFICATION -> cancelAllNotification(p0, p1)
            Extras.FOR_FLUTTER_METHOD_CREATE_NOTIFICATION_CHANNEL -> createNotificationChannel(p0, p1)
            Extras.FOR_FLUTTER_METHOD_SET_MI_PUSH_APP_ID -> setMiPushAppId(p0, p1)
            Extras.FOR_FLUTTER_METHOD_SET_MI_PUSH_APP_KEY -> setMiPushAppKey(p0, p1)
            Extras.FOR_FLUTTER_METHOD_SET_MZ_PUSH_ID -> setMzPushAppId(p0, p1)
            Extras.FOR_FLUTTER_METHOD_SET_MZ_PUSH_KEY -> setMzPushAppKey(p0, p1)
            Extras.FOR_FLUTTER_METHOD_ENABLE_OPPO_NOTIFICATION -> enableOppoNotification(p0, p1)
            Extras.FOR_FLUTTER_METHOD_SET_OPPO_PUSH_APP_KEY -> setOppoPushAppKey(p0, p1)
            Extras.FOR_FLUTTER_METHOD_SET_OPPO_PUSH_APP_ID -> setOppoPushAppId(p0, p1)
            Extras.FOR_FLUTTER_METHOD_IS_MIUI_ROM -> isMiuiRom(p0, p1)
            Extras.FOR_FLUTTER_METHOD_IS_EMUI_ROM -> isEmuiRom(p0, p1)
            Extras.FOR_FLUTTER_METHOD_IS_MEIZU_ROM -> isMeizuRom(p0, p1)
            Extras.FOR_FLUTTER_METHOD_IS_OPPO_ROM -> isOppoRom(p0, p1)
            Extras.FOR_FLUTTER_METHOD_IS_VIVO_ROM -> isVivoRom(p0, p1)
            Extras.FOR_FLUTTER_METHOD_IS_FCM_ROM -> isFcmRom(p0, p1)
            Extras.FOR_FLUTTER_METHOD_IS_GOOGLE_ROM -> isGoogleRom(p0, p1)
            Extras.FOR_FLUTTER_METHOD_IS_360_ROM -> is360Rom(p0, p1)
            Extras.FOR_FLUTTER_METHOD_ENABLE_DEBUG -> setEnableDebug(p0, p1)
            Extras.FOR_FLUTTER_METHOD_SET_HEADER_BEAT_INTERVAL_MS -> setHeartbeatIntervalMs(p0, p1)
            Extras.FOR_FLUTTER_METHOD_SET_SERVERSUFFIX -> setServerSuffix(p0, p1)
            Extras.FOR_FLUTTER_METHOD_SET_ACCESSID -> setAccessId(p0, p1)
            Extras.FOR_FLUTTER_METHOD_SET_ACCESSKEY -> setAccessKey(p0, p1)
        }
    }


    /**
     * 调用Flutter 函数
     *
     * @param methodName 函数名称
     * @param para       参数
     */
    fun toFlutterMethod(methodName: String, para: Map<String, Any?>?) {
        Log.i(TAG, "调用Flutter=>${methodName}")
        MainHandler.getInstance().post { channel.invokeMethod(methodName, para) }
    }

    fun toFlutterMethod(methodName: String, para: String) {
        Log.i(TAG, "调用Flutter=>${methodName}")
        MainHandler.getInstance().post { channel.invokeMethod(methodName, para) }
    }

    /**
     * 设置accessId
     */
    private fun setAccessId(call: MethodCall, result: MethodChannel.Result) {
        val map = call.arguments<Map<String, String>>()
        val accessId = map?.get(Extras.ACCESSID)
        if (accessId == null) {
            Log.e(TAG, "调用信鸽SDK-->setAccessId()-----accessId为空")
            result.error("setAccessId", "accessId为空", null)
            return
        }
        val value = accessId.toLong()
        Log.i(TAG, "调用信鸽SDK-->setAccessId()-----accessId=${value}")
        XGPushConfig.setAccessId(context, value)
        result.safeSuccess(null)
    }
    /**
     * 设置accessKey
     */
    private fun setAccessKey(call: MethodCall, result: MethodChannel.Result) {
        val map = call.arguments<Map<String, String>>()
        val accessKey = map?.get(Extras.ACCESSKEY)
        Log.i(TAG, "调用信鸽SDK-->setAccessKey()-----accessKey=${accessKey}")
        XGPushConfig.setAccessKey(context, accessKey)
        result.safeSuccess(null)
    }

    /**
     * 设置接入域名
     */
    private fun setServerSuffix(call: MethodCall, result: MethodChannel.Result) {
        val map = call.arguments<Map<String, String>>()
        val addr = map?.get(Extras.ADDR)
        Log.i(TAG, "调用信鸽SDK-->setServerSuffix()-----addr=${addr}")
        XGApiConfig.setServerSuffix(context, addr)
        result.safeSuccess(null)
    }

    /**
     * 信鸽推送注册
     */
    fun regPush(call: MethodCall?, result: MethodChannel.Result) {
        Log.i(TAG, "调用信鸽SDK-->registerPush()")
        XGPushManager.registerPush(context)
        result.safeSuccess(null)
    }

    fun mRegPush(methodName: String, para: Map<String, Any?>?) {
        MainHandler.getInstance().post { channel.invokeMethod("startXg", para) }
    }

    /**
     * 开启Debug模式
     */
    private fun setEnableDebug(call: MethodCall, result: MethodChannel.Result) {
        val map = call.arguments<HashMap<String, Any>>()
        val debug = map?.get(Extras.DEBUG) as Boolean
        Log.i(TAG, "调用信鸽SDK-->enableDebug()----->isDebug=${debug}")
        XGPushConfig.enableDebug(context, debug)
        result.safeSuccess(null)
    }

    /**
     * 设置心跳时间间隔
     */
    fun setHeartbeatIntervalMs(call: MethodCall, result: MethodChannel.Result) {
        val map = call.arguments<HashMap<String, Any>>()
        val interval = map?.get(Extras.HEADER_BEAT_INTERVAL_MS) as Int
        Log.i(TAG, "调用信鸽SDK-->setHeartbeatIntervalMs()----->interval=${interval}")
        XGPushConfig.setHeartbeatIntervalMs(context, interval)
        result.safeSuccess(null)
    }

    /**
     * 反注册
     *
     *
     * 当用户已退出或 App 被关闭，不再需要接收推送时，可以取消注册 App，即反注册。
     * （一旦设备反注册，直到这个设备重新注册成功期间内，下发的消息该设备都无法收到）
     *
     * @param call   方法
     * @param result 结果
     */
    fun stopXg(call: MethodCall?, result: MethodChannel.Result) {
        Log.i(TAG, "调用信鸽SDK-->unregisterPush()")
        XGPushManager.unregisterPush(context)
        result.safeSuccess(null)
    }

    /**
     * 设置标签,单个标签 call传参为TagName
     *
     * @param call   方法
     * @param result 结果
     */
    fun setXgTag(call: MethodCall, result: MethodChannel.Result) {
        val map = call.arguments<HashMap<String, String>>()
        val tagName = map?.get(Extras.TAG_NAME)
        val context = context
        Log.i(TAG, "调用信鸽SDK-->setTag()---->tagName${tagName}")
        XGPushManager.setTag(context, tagName, object : XGIOperateCallback {
            override fun onSuccess(p0: Any?, p1: Int) {
                Log.i(TAG, "setTag successful")
                val paraDict = mapOf("code" to 0,"type" to "tag", "msg" to  "setTag successful")
                toFlutterMethod(Extras.XG_PUSH_DID_UPDATED_WITH_IDENENTIFIER, paraDict)
                result.safeSuccess(true)
            }

            override fun onFail(p0: Any?, p1: Int, p2: String?) {
                Log.i(TAG, "setTag failure")
                val paraDict = mapOf("code" to p1,"type" to "tag", "msg" to  p2)
                toFlutterMethod(Extras.XG_PUSH_DID_UPDATED_WITH_IDENENTIFIER, paraDict)
                result.safeSuccess(false)
            }
        })
    }

    /**
     * 设置多tag call传参为List<String>tag的集合
     * 一次设置多个标签，会覆盖这个设备之前设置的标签。
     *
     * @param call   方法
     * @param result 结果
    </String> */
    fun setXgTags(call: MethodCall, result: MethodChannel.Result) {
        val map = call.arguments<Map<String, List<String>>>()
        val tags = HashSet<String>(map?.get(Extras.TAG_NAMES) ?: emptyList())
        //operateName用户定义的操作名称，回调结果会原样返回，用于标识回调属于哪次操作。
        val operateName = "setTags:" + System.currentTimeMillis()
        val context = context
        Log.i(TAG, "调用信鸽SDK-->setTags()")
        XGPushManager.setTags(context, operateName, tags, object : XGIOperateCallback {
            override fun onSuccess(p0: Any?, p1: Int) {
                Log.i(TAG, "setTags successful")
                val paraDict = mapOf("code" to 0,"type" to "tag", "msg" to  "setTags successful")
                toFlutterMethod(Extras.XG_PUSH_DID_UPDATED_WITH_IDENENTIFIER, paraDict)
                result.safeSuccess(true)
            }

            override fun onFail(p0: Any?, p1: Int, p2: String?) {
                Log.i(TAG, "setTags failure")
                val paraDict = mapOf("code" to p1,"type" to "tag", "msg" to  p2)
                toFlutterMethod(Extras.XG_PUSH_DID_UPDATED_WITH_IDENENTIFIER, paraDict)
                result.safeSuccess(false)
            }
        })
    }

    /**
     * 添加多个标签  call传参为List<String>tag的集合 每个 tag 不能超过40字节（超过会抛弃）不能包含空格（含有空格会删除空格)
     * 最多设置1000个 tag，超过部分会抛弃
     * 一次设置多个标签，会覆盖这个设备之前设置的标签。
     *
     *
     * 如果新增的标签的格式为 "test:2 level:2"，则会删除这个设备的全部历史标签，再新增 test:2 和 level。
     * 如果新增的标签有部分不带:号，如 "test:2 level"，则会删除这个设备的全部历史标签，再新增 test:2 和 level 标签。
     *
     *
     * 新增的 tags 中，:号为后台关键字，请根据具体的业务场景使用。
     * 此接口调用的时候需要间隔一段时间（建议大于5s），否则可能造成更新失败。
     *
     * @param call   方法
     * @param result 结果
    </String> */
    fun addXgTags(call: MethodCall, result: MethodChannel.Result) {
        val map = call.arguments<Map<String, List<String>>>()
        val tags = HashSet<String>(map?.get(Extras.TAG_NAMES) ?: emptyList())
        //operateName用户定义的操作名称，回调结果会原样返回，用于标识回调属于哪次操作。
        val operateName = "addTags:" + System.currentTimeMillis()
        val context = context
        Log.i(TAG, "调用信鸽SDK-->addTags()")
        XGPushManager.addTags(context, operateName, tags, object : XGIOperateCallback {
            override fun onSuccess(p0: Any?, p1: Int) {
                Log.i(TAG, "addTags successful")
                val paraDict = mapOf("code" to 0,"type" to "tag", "msg" to  "addTags successful")
                toFlutterMethod(Extras.XG_PUSH_DID_BIND_WITH_IDENENTIFIER, paraDict)
                result.safeSuccess(true)
            }

            override fun onFail(p0: Any?, p1: Int, p2: String?) {
                Log.i(TAG, "addTags failure")
                val paraDict = mapOf("code" to p1,"type" to "tag", "msg" to  p2)
                toFlutterMethod(Extras.XG_PUSH_DID_BIND_WITH_IDENENTIFIER, paraDict)
                result.safeSuccess(false)
            }
        })
    }

    /**
     * 删除指定标签 call传参为TagName需要删除的标签名称
     *
     * @param call   方法
     * @param result 结果
     */
    fun deleteXgTag(call: MethodCall, result: MethodChannel.Result) {
        val map = call.arguments<Map<String, String>>()
        val tagName = map?.get(Extras.TAG_NAME)
        val context = context
        Log.i(TAG, "调用信鸽SDK-->deleteTag()----tagName=${tagName}")
        XGPushManager.deleteTag(context, tagName, object : XGIOperateCallback {
            override fun onSuccess(p0: Any?, p1: Int) {
                Log.i(TAG, "deleteTag successful")
                val paraDict = mapOf("code" to 0,"type" to "tag", "msg" to  "deleteTag successful")
                toFlutterMethod(Extras.XG_PUSH_DID_UNBIND_WITH_IDENENTIFIER, paraDict)
                result.safeSuccess(true)
            }

            override fun onFail(p0: Any?, p1: Int, p2: String?) {
                Log.i(TAG, "deleteTag failure")
                val para = "deleteTag failure----->code=${p1}--->message=${p2}"
                val paraDict = mapOf("code" to p1,"type" to "tag", "msg" to  p2)
                toFlutterMethod(Extras.XG_PUSH_DID_UNBIND_WITH_IDENENTIFIER, paraDict)
                result.safeSuccess(false)
            }
        })
    }


    /**
     * 删除多个标签  call传参为List<String>tag的集合 每个标签是一个 String。限制：
     * 每个 tag 不能超过40字节（超过会抛弃），不能包含空格（含有空格会删除空格）。最多设置1000个tag，超过部分会抛弃。
     *
     * @param call   方法
     * @param result 结果
    </String> */
    fun deleteXgTags(call: MethodCall, result: MethodChannel.Result) {
        val map = call.arguments<Map<String, List<String>>>()
        val tags = HashSet<String>(map?.get(Extras.TAG_NAMES) ?: emptyList())
        //operateName用户定义的操作名称，回调结果会原样返回，用于标识回调属于哪次操作。
        val operateName = "deleteTags:" + System.currentTimeMillis()
        val context = context
        Log.i(TAG, "调用信鸽SDK-->deleteTags()----operateName=${operateName}")
        XGPushManager.deleteTags(context, operateName, tags, object : XGIOperateCallback {
            override fun onSuccess(p0: Any?, p1: Int) {
                Log.i(TAG, "deleteTags successful")
                val paraDict = mapOf("code" to 0,"type" to "tag", "msg" to  "deleteTags successful")
                toFlutterMethod(Extras.XG_PUSH_DID_UNBIND_WITH_IDENENTIFIER, paraDict)
                result.safeSuccess(true)
            }

            override fun onFail(p0: Any?, p1: Int, p2: String?) {
                Log.i(TAG, "deleteTags failure")
                val paraDict = mapOf("code" to p1,"type" to "tag", "msg" to  p2)
                toFlutterMethod(Extras.XG_PUSH_DID_UNBIND_WITH_IDENENTIFIER, paraDict)
                result.safeSuccess(false)
            }
        })
    }


    /**
     * 清楚所有标签
     *
     * @param call   方法
     * @param result 结果
     */
    fun cleanXgTags(call: MethodCall?, result: MethodChannel.Result) { //operateName用户定义的操作名称，回调结果会原样返回，用于标识回调属于哪次操作。
        val operateName = "cleanTags:" + System.currentTimeMillis()
        val context = context
        Log.i(TAG, "调用信鸽SDK-->cleanTags()----operateName=${operateName}")
        XGPushManager.cleanTags(context, operateName, object : XGIOperateCallback {
            override fun onSuccess(p0: Any?, p1: Int) {
                Log.i(TAG, "cleanTags successful")
                val paraDict = mapOf("code" to 0,"type" to "tag", "msg" to  "cleanTags successful")
                toFlutterMethod(Extras.XG_PUSH_DID_CLEAR_WITH_IDENENTIFIER, paraDict)
                result.safeSuccess(true)
            }

            override fun onFail(p0: Any?, p1: Int, p2: String?) {
                Log.i(TAG, "cleanTags failure")
                val paraDict = mapOf("code" to p1,"type" to "tag", "msg" to  p2)
                toFlutterMethod(Extras.XG_PUSH_DID_CLEAR_WITH_IDENENTIFIER, paraDict)
                result.safeSuccess(false)
            }
        })
    }

    /**
     * 获取Xg的token
     * App 第一次注册会产生 Token，之后一直存在手机上，不管以后注销注册操作，该 Token 一直存在，
     * 当 App 完全卸载重装了 Token 会发生变化。不同 App 之间的 Token 不一样。
     */
    fun xgToken(call: MethodCall?, result: MethodChannel.Result) {
        val token: String = XGPushConfig.getToken(context)
        Log.i(TAG, "调用信鸽SDK-->getToken()----token=${token}")
        result.success(token)
    }

    fun xgSdkVersion(call: MethodCall?, result: MethodChannel.Result) {
        val sdkVersion: String = XGPushConstants.SDK_VERSION
        Log.i(TAG, "调用信鸽SDK-->SDK_VERSION----${sdkVersion}")
        result.success(sdkVersion)
    }

    fun getAccountType(accountType: String): Int {
        when (accountType) {
            "UNKNOWN" -> return XGPushManager.AccountType.UNKNOWN.value
            "CUSTOM" -> return XGPushManager.AccountType.CUSTOM.value
//            "IDFA" -> return XGPushManager.AccountType.IDFA.getValue()
            "PHONE_NUMBER" -> return XGPushManager.AccountType.PHONE_NUMBER.value
            "WX_OPEN_ID" -> return XGPushManager.AccountType.WX_OPEN_ID.value
            "QQ_OPEN_ID" -> return XGPushManager.AccountType.QQ_OPEN_ID.value
            "EMAIL" -> return XGPushManager.AccountType.EMAIL.value
            "SINA_WEIBO" -> return XGPushManager.AccountType.SINA_WEIBO.value
            "ALIPAY" -> return XGPushManager.AccountType.ALIPAY.value
            "TAOBAO" -> return XGPushManager.AccountType.TAOBAO.value
            "DOUBAN" -> return XGPushManager.AccountType.DOUBAN.value
            "FACEBOOK" -> return XGPushManager.AccountType.FACEBOOK.value
            "TWITTER" -> return XGPushManager.AccountType.TWITTER.value
            "GOOGLE" -> return XGPushManager.AccountType.GOOGLE.value
            "BAIDU" -> return XGPushManager.AccountType.BAIDU.value
            "JINGDONG" -> return XGPushManager.AccountType.JINGDONG.value
            "LINKEDIN" -> return XGPushManager.AccountType.LINKEDIN.value
            "IMEI" -> return XGPushManager.AccountType.IMEI.value
            else -> return XGPushManager.AccountType.UNKNOWN.value
        }
    }

    /**
     * 绑定账号注册
     * 推荐有账号体系的App使用（此接口会覆盖设备之前绑定过的账号，仅当前注册的账号生效）
     *
     * @param call   String 账号
     * @param result 结果
     */
    fun bindAccount(call: MethodCall, result: MethodChannel.Result?) {
        val map = call.arguments<Map<String, String>>()
        val account = map?.get(Extras.ACCOUNT)
        val context = context
        val accountType: String = (map?.get(Extras.ACCOUNT_TYPE)) ?: "UNKNOWN"
        Log.i(TAG, "调用信鸽SDK-->bindAccount()----account=${account}, accountType=${accountType}")
        XGPushManager.bindAccount(context, account, getAccountType(accountType), object : XGIOperateCallback {
            override fun onSuccess(p0: Any?, p1: Int) {
                Log.i(TAG, "bindAccount successful")
                val paraDict = mapOf("code" to 0,"type" to "account", "msg" to  "bindAccount successful")
                toFlutterMethod(Extras.XG_PUSH_DID_BIND_WITH_IDENENTIFIER, paraDict)
            }

            override fun onFail(p0: Any?, p1: Int, p2: String?) {
                Log.i(TAG, "bindAccount failure")
                val paraDict = mapOf("code" to p1,"type" to "account", "msg" to  p2)
                toFlutterMethod(Extras.XG_PUSH_DID_BIND_WITH_IDENENTIFIER, paraDict)
            }
        })
    }

    /**
     * 添加账号
     * 推荐有账号体系的App使用（此接口保留之前的账号，只做增加操作，一个token下最多只能有10个账号超过限制会自动顶掉之前绑定的账号)
     *
     * @param call   String 账号
     * @param result 结果
     */
    fun appendAccount(call: MethodCall, result: MethodChannel.Result?) {
        val map = call.arguments<Map<String, String>>()
        val account = map?.get(Extras.ACCOUNT)
        val context = context
        val accountType: String = (map?.get(Extras.ACCOUNT_TYPE)) ?: "UNKNOWN"
        Log.i(TAG, "调用信鸽SDK-->appendAccount()----account=${account}, accountType=${accountType}")
        XGPushManager.appendAccount(context, account, getAccountType(accountType), object : XGIOperateCallback {
            override fun onSuccess(p0: Any?, p1: Int) {
                Log.i(TAG, "appendAccount successful")
                val paraDict = mapOf("code" to 0,"type" to "account", "msg" to  "appendAccount successful")
                toFlutterMethod(Extras.XG_PUSH_DID_BIND_WITH_IDENENTIFIER, paraDict)
            }

            override fun onFail(p0: Any?, p1: Int, p2: String?) {
                Log.i(TAG, "appendAccount failure")
                val paraDict = mapOf("code" to p1,"type" to "account", "msg" to  p2)
                toFlutterMethod(Extras.XG_PUSH_DID_BIND_WITH_IDENENTIFIER, paraDict)
            }
        })
    }

    /**
     * 解除指定账号绑定
     * 账号解绑只是解除 Token 与 App 账号的关联，若使用全量/标签/Token 推送仍然能收到通知/消息。
     *
     * @param call   String账号
     * @param result 结果
     */
    fun delAccount(call: MethodCall, result: MethodChannel.Result?) {
        val map = call.arguments<Map<String, String>>()
        val account = map?.get(Extras.ACCOUNT)
        val context = context
        val accountType: String = (map?.get(Extras.ACCOUNT_TYPE)) ?: "UNKNOWN"
        Log.i(TAG, "调用信鸽SDK-->delAccount()----account=${account}, accountType=${accountType}")
        XGPushManager.delAccount(context, account, getAccountType(accountType), object : XGIOperateCallback {
            override fun onSuccess(p0: Any?, p1: Int) {
                Log.i(TAG, "delAccount successful")
                val paraDict = mapOf("code" to 0,"type" to "account", "msg" to  "delAccount successful")
                toFlutterMethod(Extras.XG_PUSH_DID_UNBIND_WITH_IDENENTIFIER, paraDict)
            }

            override fun onFail(p0: Any?, p1: Int, p2: String?) {
                Log.i(TAG, "delAccount failure")
                val paraDict = mapOf("code" to p1,"type" to "account", "msg" to  p2)
                toFlutterMethod(Extras.XG_PUSH_DID_UNBIND_WITH_IDENENTIFIER, paraDict)
            }
        })
    }

    /**
     * 清除全部账号
     */
    fun delAllAccount(call: MethodCall, result: MethodChannel.Result?) {
        val context = context
        Log.i(TAG, "调用信鸽SDK-->delAllAccount()")
        XGPushManager.delAllAccount(context, object : XGIOperateCallback {
            override fun onSuccess(p0: Any?, p1: Int) {
                Log.i(TAG, "delAllAccount successful")
                val paraDict = mapOf("code" to 0,"type" to "account", "msg" to  "delAllAccount successful")
                toFlutterMethod(Extras.XG_PUSH_DID_CLEAR_WITH_IDENENTIFIER, paraDict)
            }

            override fun onFail(p0: Any?, p1: Int, p2: String?) {
                Log.i(TAG, "delAllAccount failure")
                val paraDict = mapOf("code" to p1,"type" to "account", "msg" to  p2)
                toFlutterMethod(Extras.XG_PUSH_DID_CLEAR_WITH_IDENENTIFIER, paraDict)
            }
        })
    }

    /**
     * 新增用户属性
     */
    fun upsertAttributes(call: MethodCall, result: MethodChannel.Result?) {
        val map = call.arguments<Map<String, String>>()
        val attributesMap = map?.get(Extras.ATTRIBUTES) as HashMap<String, String>
        Log.i(TAG, "调用信鸽SDK-->upsertAttributes()----->attributes=${attributesMap}")
        val context = context
        XGPushManager.upsertAttributes(context, "upsertAttributes", attributesMap, object : XGIOperateCallback {
            override fun onSuccess(p0: Any?, p1: Int) {
                Log.i(TAG, "upsertAttributes successful")
                val paraDict = mapOf("code" to 0,"type" to "attributes", "msg" to  "upsertAttributes successful")
                toFlutterMethod(Extras.XG_PUSH_DID_BIND_WITH_IDENENTIFIER, paraDict)
            }

            override fun onFail(p0: Any?, p1: Int, p2: String?) {
                Log.i(TAG, "upsertAttributes failure")
                val paraDict = mapOf("code" to p1,"type" to "attributes", "msg" to  p2)
                toFlutterMethod(Extras.XG_PUSH_DID_BIND_WITH_IDENENTIFIER, paraDict)
            }
        })
    }

    /**
     * 删除用户属性
     */
    fun delAttributes(call: MethodCall, result: MethodChannel.Result?) {
        val map = call.arguments<Map<String, List<String>>>()
        val attributesList = HashSet<String>(map?.get(Extras.ATTRIBUTES) ?: emptyList())
        Log.i(TAG, "调用信鸽SDK-->delAttributes()----->attributes=${attributesList}")
        val context = context
        XGPushManager.delAttributes(context, "delAttributes", attributesList, object : XGIOperateCallback {
            override fun onSuccess(p0: Any?, p1: Int) {
                Log.i(TAG, "delAttributes successful")
                val paraDict = mapOf("code" to 0,"type" to "attributes", "msg" to  "delAttributes successful")
                toFlutterMethod(Extras.XG_PUSH_DID_UNBIND_WITH_IDENENTIFIER, paraDict)
            }

            override fun onFail(p0: Any?, p1: Int, p2: String?) {
                Log.i(TAG, "delAttributes failure")
                val paraDict = mapOf("code" to p1,"type" to "attributes", "msg" to  p2)
                toFlutterMethod(Extras.XG_PUSH_DID_UNBIND_WITH_IDENENTIFIER, paraDict)
            }
        })
    }

    /**
     * 更新用户属性
     */
    fun clearAndAppendAttributes(call: MethodCall, result: MethodChannel.Result?) {
        val map = call.arguments<Map<String, String>>()
        val attributesMap = map?.get(Extras.ATTRIBUTES) as HashMap<String, String>
        Log.i(TAG, "调用信鸽SDK-->clearAndAppendAttributes()----->attributes=${attributesMap}")
        val context = context
        XGPushManager.clearAndAppendAttributes(context, "clearAndAppendAttributes", attributesMap, object : XGIOperateCallback {
            override fun onSuccess(p0: Any?, p1: Int) {
                Log.i(TAG, "clearAndAppendAttributes successful")
                val paraDict = mapOf("code" to 0,"type" to "attributes", "msg" to  "clearAndAppendAttributes successful")
                toFlutterMethod(Extras.XG_PUSH_DID_UPDATED_WITH_IDENENTIFIER, paraDict)
            }

            override fun onFail(p0: Any?, p1: Int, p2: String?) {
                Log.i(TAG, "clearAndAppendAttributes failure")
                val paraDict = mapOf("code" to p1,"type" to "attributes", "msg" to  p2)
                toFlutterMethod(Extras.XG_PUSH_DID_UPDATED_WITH_IDENENTIFIER, paraDict)
            }
        })
    }

    /**
     * 清除全部用户属性
     */
    fun clearAttributes(call: MethodCall, result: MethodChannel.Result?) {
        Log.i(TAG, "调用信鸽SDK-->clearAttributes()")
        val context = context
        XGPushManager.clearAttributes(context, "clearAttributes", object : XGIOperateCallback {
            override fun onSuccess(p0: Any?, p1: Int) {
                Log.i(TAG, "clearAttributes successful")
                val paraDict = mapOf("code" to 0,"type" to "attributes", "msg" to  "clearAttributes successful")
                toFlutterMethod(Extras.XG_PUSH_DID_CLEAR_WITH_IDENENTIFIER, paraDict)
            }

            override fun onFail(p0: Any?, p1: Int, p2: String?) {
                Log.i(TAG, "clearAttributes failure")
                val paraDict = mapOf("code" to p1,"type" to "attributes", "msg" to  p2)
                toFlutterMethod(Extras.XG_PUSH_DID_CLEAR_WITH_IDENENTIFIER, paraDict)
            }
        })
    }

    /**
     * 开启其他推送  XGPushManager.registerPush 前，开启第三方推送
     */
    fun enableOtherPush(call: MethodCall, result: MethodChannel.Result?) {
        Log.i(TAG, "调用信鸽SDK-->enableOtherPush()")
        XGPushConfig.enableOtherPush(context, true)
    }

    /**
     * 开启其他推送  XGPushManager.registerPush 前，开启第三方推送
     */
    fun enableOtherPush2(call: MethodCall, result: MethodChannel.Result?) {
        val map = call.arguments<HashMap<String, Any>>()
        val enable = map?.get("enable") as Boolean
        Log.i(TAG, "调用信鸽SDK-->enableOtherPush2()")
        XGPushConfig.enableOtherPush(context, enable)
    }

    /**
     * 获取厂商推送 token  XGPushManager.registerPush 成功后
     */
    fun getOtherPushToken(call: MethodCall?, result: MethodChannel.Result) {
        val otherPushToken: String = (XGPushConfig.getOtherPushToken(context))?:""
        Log.i(TAG, "调用信鸽SDK-->getOtherPushToken()---otherPushToken=${otherPushToken}")
        result.success(otherPushToken)
    }

    /**
     * 获取厂商推送品牌  XGPushManager.registerPush 成功后
     */
    fun getOtherPushType(call: MethodCall?, result: MethodChannel.Result) {
        val otherPushType: String = (XGPushConfig.getOtherPushType(context))?:""
        Log.i(TAG, "调用信鸽SDK-->getOtherPushType()---otherPushType=${otherPushType}")
        result.success(otherPushType)
    }

    fun enablePullUpOtherApp(call: MethodCall, result: MethodChannel.Result) {
        val map = call.arguments<HashMap<String, Any>>()
        val enable = map?.get("enable") as Boolean
        Log.i(TAG, "调用信鸽SDK-->enablePullUpOtherApp()")
        XGPushConfig.enablePullUpOtherApp(context, enable)
    }

    fun setBadgeNum(call: MethodCall, result: MethodChannel.Result?) {
        val map = call.arguments<Map<String, Int>>()
        val badgeNum = map?.get(Extras.BADGE_NUM) as Int
        Log.i(TAG, "调用信鸽SDK-->setBadgeNum()-----badgeNum=${badgeNum}")
        XGPushConfig.setBadgeNum(context, badgeNum)
    }

    fun resetBadgeNum(call: MethodCall, result: MethodChannel.Result?) {
        Log.i(TAG, "调用信鸽SDK-->resetBadgeNum()")
        XGPushConfig.resetBadgeNum(context)
    }

    fun cancelAllNotification(call: MethodCall, result: MethodChannel.Result?) {
        Log.i(TAG, "调用信鸽SDK-->cancelAllNotification()")
        XGPushManager.cancelAllNotifaction(context)
    }

    fun createNotificationChannel(call: MethodCall, result: MethodChannel.Result?) {
        val map = call.arguments<Map<String, Any>>() ?: emptyMap()
        val channelId = map[Extras.CHANNEL_ID] as String?
        val channelName = map[Extras.CHANNEL_NAME] as String?
        if (map.size == 2) {
            Log.i(TAG, "调用信鸽SDK-->createNotificationChannel(${channelId}, ${channelName})")
            XGPushManager.createNotificationChannel(context, 
                channelId, channelName, true, true, true, null)
        } else {
            val enableVibration = map[Extras.ENABLE_VIBRATION] as Boolean? ?: false
            val enableLights = map[Extras.ENABLE_LIGHTS] as Boolean? ?: false
            val enableSound = map[Extras.ENABLE_SOUND] as Boolean? ?: false
            val soundFileName = map[Extras.SOUND_FILE_NAME] as String? ?: ""

            val context = context
            if (context != null){
                val soundFileId = context.resources.getIdentifier(soundFileName, "raw", context.packageName)
                if (soundFileId > 0) {
                    val soundUri = "android.resource://" + context.packageName + "/" + soundFileId
                    Log.i(TAG, "调用信鸽SDK-->createNotificationChannel(${channelId}, ${channelName}, ${enableVibration}, ${enableLights}, ${enableSound}, ${soundUri})")
                    XGPushManager.createNotificationChannel(context,
                        channelId, channelName, enableVibration, enableLights, enableSound, Uri.parse(soundUri))
                } else {
                    Log.i(TAG, "调用信鸽SDK-->createNotificationChannel(${channelId}, ${channelName}, ${enableVibration}, ${enableLights}, ${enableSound}, null)")
                    XGPushManager.createNotificationChannel(context,
                        channelId, channelName, enableVibration, enableLights, enableSound, null)
                }
            }
        }
    }


    /**
     * 设置小米平台的APP_KEY
     * 推荐有账号体系的App使用（此接口保留之前的账号，只做增加操作，一个token下最多只能有10个账号超过限制会自动顶掉之前绑定的账号)
     *
     * @param call   String APP_KEY
     * @param result 结果
     */
    fun setMiPushAppKey(call: MethodCall, result: MethodChannel.Result?) {
        val map = call.arguments<Map<String, String>>()
        val appKey = map?.get(Extras.APP_KEY)
        Log.i(TAG, "调用信鸽SDK-->setMiPushAppKey()-----key=${appKey}")
        XGPushConfig.setMiPushAppKey(context, appKey)
    }

    /**
     * 设置小米平台的APP_ID
     *
     * @param call   String app_id
     * @param result 结果
     */
    fun setMiPushAppId(call: MethodCall, result: MethodChannel.Result?) {
        val map = call.arguments<Map<String, String>>()
        val appId = map?.get(Extras.APP_ID)
        Log.i(TAG, "调用信鸽SDK-->setMiPushAppId()-----appId=${appId}")
        XGPushConfig.setMiPushAppId(context, appId)
    }


    /**
     * 设置魅族平台的的APP_KEy
     */
    fun setMzPushAppKey(call: MethodCall, result: MethodChannel.Result?) {
        val map = call.arguments<Map<String, String>>()
        val appKey = map?.get(Extras.APP_KEY)
        Log.i(TAG, "调用信鸽SDK-->setMzPushAppKey()-----appKey=${appKey}")
        XGPushConfig.setMzPushAppKey(context, appKey)
    }

    /**
     * 设置魅族平台的的APP_KEy
     */
    fun setMzPushAppId(call: MethodCall, result: MethodChannel.Result?) {
        val map = call.arguments<Map<String, String>>()
        val appId = map?.get(Extras.APP_ID)
        Log.i(TAG, "调用信鸽SDK-->setMzPushAppId()-----appId=${appId}")
        XGPushConfig.setMzPushAppId(context, appId)
    }

    /**
     * 在调用腾讯移动推送 XGPushManager.registerPush前，调用以下代码：
     * 在应用首次启动时弹出通知栏权限请求窗口，应用安装周期内，提示弹窗仅展示一次。需 TPNS-OPPO 依赖包版本在 1.1.5.1 及以上支持，系统 ColorOS 5.0 以上有效。
     */
    private fun enableOppoNotification(call: MethodCall, result: MethodChannel.Result) {
        val map = call.arguments<Map<String, Any>>()
        val isNotification = map?.get("isNotification") as Boolean
        Log.i(TAG, "调用信鸽SDK-->enableOppoNotification()-----isNotification=${isNotification}")
        XGPushConfig.enableOppoNotification(context, isNotification)
    }

    /**
     * 设置OPPO的key
     *
     * @param call   String类型的APPKey
     * @param result
     */
    private fun setOppoPushAppKey(call: MethodCall, result: MethodChannel.Result) {
        val map = call.arguments<Map<String, String>>()
        val appKey = map?.get(Extras.APP_KEY)
        Log.i(TAG, "调用信鸽SDK-->setOppoPushAppKey()-----appKey=${appKey}")
        XGPushConfig.setOppoPushAppKey(context, appKey)
    }

    /**
     * 设置OPPO的appID
     *
     * @param call   String 类型发AppId
     * @param result
     */
    private fun setOppoPushAppId(call: MethodCall, result: MethodChannel.Result) {
        val map = call.arguments<Map<String, String>>()
        val appId = map?.get(Extras.APP_ID)
        Log.i(TAG, "调用信鸽SDK-->setOppoPushAppId()-----appId=${appId}")
        XGPushConfig.setOppoPushAppId(context, appId)
    }

    /**
     * 判断是否为支持FCM手机
     */
    fun isFcmRom(call: MethodCall?, result: MethodChannel.Result?) { //        boolean is
    }

    /**
     * 判断是否为谷歌手机
     */
    private fun isGoogleRom(call: MethodCall, result: MethodChannel.Result) {
      result.success(DeviceInfoUtil.isGoogleRom())
    }

    /**
     * 判断是否为360手机
     */
    private fun is360Rom(call: MethodCall, result: MethodChannel.Result) {
        Log.i(TAG, "is360Rom===" + DeviceInfoUtil.is360Rom())
        result.success(DeviceInfoUtil.is360Rom())
    }

    /**
     * 判断是否为Vivo手机
     */
    private fun isVivoRom(call: MethodCall, result: MethodChannel.Result) {
        Log.i(TAG, "isVivoRom===" + DeviceInfoUtil.isVivoRom())
        result.success(DeviceInfoUtil.isVivoRom())
    }

    /**
     * 判断是否为Oppo手机
     */
    private fun isOppoRom(call: MethodCall, result: MethodChannel.Result) {
        Log.i(TAG, "isOppoRom===" + DeviceInfoUtil.isOppoRom())
        result.success(DeviceInfoUtil.isOppoRom())
    }

    /**
     * 判断是否为魅族手机
     */
    private fun isMeizuRom(call: MethodCall, result: MethodChannel.Result) {
        Log.i(TAG, "isMeizuRom===" + DeviceInfoUtil.isMeizuRom())
        result.success(DeviceInfoUtil.isMeizuRom())
    }

    /**
     * 判断是否为华为手机
     */
    private fun isEmuiRom(call: MethodCall, result: MethodChannel.Result) {
        Log.i(TAG, "isEmuiRom===" + DeviceInfoUtil.isEmuiRom())
        result.success(DeviceInfoUtil.isEmuiRom())
    }

    /**
     * 判断是否为小米手机
     */
    private fun isMiuiRom(call: MethodCall, result: MethodChannel.Result) {
        Log.i(TAG, "isMiuiRom===" + DeviceInfoUtil.isMiuiRom())
        result.success(DeviceInfoUtil.isMiuiRom())
    }



    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        val taskQueue =
            flutterPluginBinding.binaryMessenger.makeBackgroundTaskQueue()
        val useChannel: MethodChannel = if (checkChannelInit()) {
            channel
        } else {
            MethodChannel(
                flutterPluginBinding.binaryMessenger,
                "tpns_flutter_plugin",
                StandardMethodCodec.INSTANCE,
                taskQueue
            )
        }

        if (checkInstanceInit()){
            context = flutterPluginBinding.applicationContext
            channel = useChannel
            useChannel.setMethodCallHandler(instance)
        }else{
            useChannel.setMethodCallHandler(XgFlutterPlugin(flutterPluginBinding, useChannel))
        }

        Log.i(TAG, "methodChannel onAttachedToEngine XgFlutterPlugin")
        Log.i(TAG, "onAttachedToEngine instance = $instance")
        XGMessageReceiver.sendHandlerMessage()
    }


    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
        context = null
    }
}

///MethodChannel.Result的扩展，安全返回数据
fun MethodChannel.Result.safeSuccess(data: Any?) {
    try {
        this.success(data)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
