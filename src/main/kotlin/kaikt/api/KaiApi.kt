package kaikt.api

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import kaikt.api.entity.definition.*
import kaikt.api.entity.enum.KChannelType
import kaikt.api.entity.enum.KGuildMuteType
import kaikt.api.entity.permission.KPermission
import kaikt.api.entity.request.*
import kaikt.api.entity.response.*
import kaikt.api.util.uploadAsset
import kaikt.api.util.valueNotNullMapOf
import kaikt.gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.intellij.lang.annotations.MagicConstant
import org.slf4j.LoggerFactory
import java.io.File

class KaiApi(private val token: KToken) {

	private val log = LoggerFactory.getLogger("KaiApi")
	private val cli = OkHttpClient.Builder().build()

	private val mediaTypeJson = "application/json;charset=UTF-8".toMediaType()

	companion object {
		lateinit var unspecifiedInstance: KaiApi
	}

	init {
		unspecifiedInstance = this
	}

	internal fun doGet(
		endpoint: String,
		query: Map<String, Any> = valueNotNullMapOf(),
		headers: Map<String, String> = valueNotNullMapOf(),
		noCompress: Boolean = true,
	): Response {

		// Fix '/'
		var finalEndpoint = endpoint
		if(endpoint.startsWith('/')) {
			finalEndpoint = endpoint.substring(1)
		}

		log.debug("[GET] $endpoint")
		log.debug("[GET] ${query.entries.toList()}")

		val url = HttpUrl.Builder()
			.scheme("https")
			.host("www.kookapp.cn")
			.addPathSegments(finalEndpoint)
			.addQueryParameter("compress", (if(noCompress) "0" else "1"))
			.apply {
				query.forEach { (key, value) ->
					this.addQueryParameter(key, value.toString())
				}
			}
			.build()

		val request = Request.Builder()
			.get()
			.url(url)
			.addHeader("Authorization", token.toFullToken())
			.apply {
				headers.forEach { (headerName, headerValue) ->
					addHeader(headerName, headerValue)
				}
			}
			.build()

		return cli.newCall(request).execute()
	}

	internal fun doPost(
		endpoint: String,
		body: RequestBody,
		headers: Map<String, String> = valueNotNullMapOf(),
		noCompress: Boolean = true,
	): Response {

		// Fix '/'
		var finalEndpoint = endpoint
		if(endpoint.startsWith('/')) {
			finalEndpoint = endpoint.substring(1)
		}

		val url = HttpUrl.Builder()
			.scheme("https")
			.host("www.kookapp.cn")
			.addPathSegments(finalEndpoint)
			.addQueryParameter("compress", (if(noCompress) "0" else "1"))
			.build()

		val request = Request.Builder()
			.url(url)
			.post(body)
			.addHeader("Authorization", token.toFullToken())
			.apply {
				headers.forEach { (headerName, headerValue) ->
					addHeader(headerName, headerValue)
				}
			}
			.build()

		return cli.newCall(request).execute()
	}

	internal fun doPost(
		endpoint: String,
		body: String = "",
		headers: Map<String, String> = valueNotNullMapOf(),
		noCompress: Boolean = true,
	) = doPost(endpoint, body.toRequestBody("application/json".toMediaType()), headers, noCompress).also {
		log.debug("[POST] $endpoint")
		log.debug("[POST] $body")
	}

	internal fun doPost(
		endpoint: String,
		body: Map<*, *>,
		headers: Map<String, String> = valueNotNullMapOf(),
		noCompress: Boolean = true,
	) = doPost(endpoint, gson.toJson(body), headers, noCompress)

	override fun toString(): String {
		return "KaiApi(token=$token)"
	}

	val guild by lazy { Guild() }

	inner class Guild internal constructor() {

		/**
		 * 获取当前用户加入的服务器列表
		 */
		fun getGuildList(): KListResponse<KGuildDefinition> {
			return doGet("/api/v3/guild/list").toJson().getTyped()
		}

		/**
		 * 获取服务器详情
		 * @param guildId 服务器ID
		 */
		fun getGuildView(guildId: String): KResponse<KGuildViewDefinition> {
			return doGet("/api/v3/guild/view", mapOf("guild_id" to guildId)).toJson().getTyped()
		}

		/**
		 * 获取服务器中的用户列表
		 * @param guildId 服务器ID
		 * @param conf 请求设置
		 */
		fun getGuildUserList(
			guildId: String,
			conf: KGuildUserListRequest.() -> Unit = {},
		): KResponse<KGuildUserListData> {
			return doGet(
				"/api/v3/guild/user-list",
				KGuildUserListRequest(guildId).apply(conf).toQueryMap()
			).toJson().getTyped()
		}

		/**
		 * 修改服务器中用户的昵称
		 * @param guildId 服务器ID
		 * @param userId 用户ID
		 * @param nickname 昵称
		 */
		fun postGuildNickname(guildId: String, userId: String, nickname: String): KBasicResponse {
			return doPost(
				"/api/v3/guild/nickname",
				valueNotNullMapOf(
					"guild_id" to guildId,
					"user_id" to userId,
					"nickname" to nickname
				).toJson()
			).toJson().getTyped()
		}

		/**
		 * 离开服务器
		 * @param guildId 服务器ID
		 */
		fun postGuildLeave(guildId: String): KBasicResponse {
			return doPost(
				"/api/v3/guild/leave",
				mapOf("guild_id" to guildId).toJson()
			).toJson().getTyped()
		}

		/**
		 * 踢出服务器
		 * @param guildId 服务器ID
		 * @param targetId 目标用户ID
		 */
		fun postKickOut(guildId: String, targetId: String): KBasicResponse {
			return doPost(
				"/api/v3/guild/kickout",
				valueNotNullMapOf(
					"guild_id" to guildId,
					"target_id" to targetId
				).toJson()
			).toJson().getTyped()
		}

		/**
		 * 服务器静音列表
		 * @param guildId 服务器ID
		 */
		fun getGuildMuteList(guildId: String, returnType: String = "detail"): KResponse<KGuildMuteListData> {
			return doGet(
				"/api/v3/guild-mute/list",
				valueNotNullMapOf(
					"guild_id" to guildId,
					"return_type" to returnType
				)
			).toJson().getTyped()
		}

		/**
		 * 使用户被服务器静音静言
		 * @param guildId 服务器ID
		 * @param userId 目标用户ID
		 * @param type 静音/静言
		 */
		fun postGuildMuteCreate(
			guildId: String,
			userId: String,
			@MagicConstant(valuesFromClass = KGuildMuteType::class) type: Int,
		): KBasicResponse {
			return doPost(
				"/api/v3/guild-mute/create",
				valueNotNullMapOf(
					"guild_id" to guildId,
					"user_id" to userId,
					"type" to type
				).toJson()
			).toJson().getTyped()
		}

		/**
		 * 使用户取消服务器静音静言
		 * @param guildId 服务器ID
		 * @param userId 目标用户ID
		 * @param type 静音/静言
		 */
		fun postGuildMuteDelete(
			guildId: String,
			userId: String,
			@MagicConstant(valuesFromClass = KGuildMuteType::class) type: Int,
		): KBasicResponse {
			return doPost(
				"/api/v3/guild-mute/delete",
				valueNotNullMapOf(
					"guild_id" to guildId,
					"user_id" to userId,
					"type" to type
				).toJson()
			).toJson().getTyped()
		}

	}

	val channel by lazy { Channel() }

	inner class Channel internal constructor() {

		/**
		 * 获取服务器频道列表
		 * @param guildId 服务器ID
		 * @param type 频道类型；1-文字，2-语音
		 */
		fun getChannelList(guildId: String, type: KChannelType? = null): KListResponse<KChannelDefinition> {
			return doGet(
				"/api/v3/channel/list",
				valueNotNullMapOf(
					"guild_id" to guildId,
					"type" to type?.type
				)
			).toJson().getTyped()
		}

		/**
		 * 获取频道详细信息
		 * @param channelId 频道ID
		 */
		fun getChannelView(channelId: String): KResponse<KChannelViewDefinition> {
			return doGet(
				"/api/v3/channel/view",
				mapOf("target_id" to channelId)
			).toJson().getTyped()
		}

		/**
		 * 创建频道
		 * @param guildId 服务器ID
		 * @param name 频道名称
		 * @param request 其他设置
		 */
		fun postChannelCreate(
			guildId: String,
			name: String,
			request: KChannelCreateRequest.() -> Unit = {},
		): KResponse<KChannelViewDefinition> {
			val body = KChannelCreateRequest(guildId, name).apply(request).body
			return doPost(
				"/api/v3/channel/create",
				body
			).toJson().getTyped()
		}

		/**
		 * 删除频道
		 * @param channelId 频道ID
		 */
		fun postChannelDelete(channelId: String): KBasicResponse {
			return doPost(
				"/api/v3/channel/delete",
				mapOf("channel_id" to channelId).toJson()
			).toJson().getTyped()
		}

		/**
		 * 移动用户到语音频道
		 * @param channelId 目标语音频道（必须为语音频道）
		 * @param userId 用户
		 */
		fun postChannelMoveUser(channelId: String, vararg userId: String): KBasicResponse {
			return doPost(
				"/api/v3/channel/move-user",
				mapOf(
					"target_id" to channelId,
					"user_ids" to userId.toList()
				)
			).toJson().getTyped()
		}

		/**
		 * 频道身分组权限详细信息
		 * @param channelId 频道ID
		 */
		fun getChannelRoleIndex(channelId: String): KResponse<KChannelRoleIndexData> {
			return doGet(
				"/api/v3/channel-role/index",
				mapOf("channel_id" to channelId)
			).toJson().getTyped()
		}

		/**
		 * 创建频道身分组权限
		 * @param channelId 频道ID
		 * @param roleIdOrUserId 身分组对象（可以是[服务器身分组][RoleIdOrUserId.withRoleId]或[单独用户][RoleIdOrUserId.withUserId]）
		 */
		fun postChannelRoleCreate(channelId: String, roleIdOrUserId: RoleIdOrUserId): KBasicResponse {
			return doPost(
				"/api/v3/channel-role/create",
				mapOf(
					"channel_id" to channelId,
					"type" to roleIdOrUserId.type,
					"value" to roleIdOrUserId.value
				)
			).toJson().getTyped()
		}

		/**
		 * 更新频道身分组权限
		 * @param channelId 频道ID
		 * @param roleIdOrUserId 身分组对象（可以是[服务器身分组][RoleIdOrUserId.withRoleId]或[单独用户][RoleIdOrUserId.withUserId]）
		 * @param allow 允许权限；详见[开黑啦权限][KPermission]
		 * @param deny 禁止权限；详见[开黑啦权限][KPermission]
		 */
		fun postChannelRoleUpdate(
			channelId: String,
			roleIdOrUserId: RoleIdOrUserId,
			allow: Int? = null,
			deny: Int? = null,
		): KResponse<KChannelRoleUpdateData> {
			return doPost(
				"/api/v3/channel-role/update",
				valueNotNullMapOf(
					"channel_id" to channelId,
					"type" to roleIdOrUserId.type,
					"value" to roleIdOrUserId.value,
					"allow" to allow,
					"deny" to deny
				)
			).toJson().getTyped()
		}

		/**
		 * 删除频道身分组权限
		 * @param channelId 频道ID
		 * @param roleIdOrUserId 身分组对象（可以是[服务器身分组][RoleIdOrUserId.withRoleId]或[单独用户][RoleIdOrUserId.withUserId]）
		 */
		fun postChannelRoleDelete(channelId: String, roleIdOrUserId: RoleIdOrUserId): KBasicResponse {
			return doPost(
				"/api/v3/channel-role/delete",
				mapOf(
					"channel_id" to channelId,
					"type" to roleIdOrUserId.type,
					"value" to roleIdOrUserId.value
				)
			).toJson().getTyped()
		}

	}

	val message by lazy { Message() }

	inner class Message internal constructor() {

		/**
		 * 获取频道聊天消息列表
		 * @param channelId 频道ID
		 * @param request 查询细节设置
		 */
		fun getMessageList(
			channelId: String,
			request: KMessageListRequest.() -> Unit = {},
		): KListResponse<KMessageDefinition> {
			val query = KMessageListRequest(channelId).apply(request).query
			return doGet(
				"/api/v3/message/list",
				query
			).toJson().getTyped()
		}

		/**
		 * 发送频道聊天消息
		 * @param channelId 频道ID
		 * @param content 消息内容
		 * @param request 发送细节设置
		 */
		fun postMessageCreate(
			channelId: String,
			content: String,
			request: KMessageCreateRequest.() -> Unit = {},
		): KResponse<KMessageCreateData> {
			val body = KMessageCreateRequest(channelId, content).apply(request).body
			return doPost(
				"/api/v3/message/create",
				body
			).toJson().getTyped()
		}

		/**
		 * 更新频道聊天消息
		 * @param msgId 消息ID
		 * @param content 更新后的消息内容
		 * @param request 更新细节设置
		 */
		fun postMessageUpdate(
			msgId: String,
			content: String,
			request: KMessageUpdateRequest.() -> Unit = {},
		): KBasicResponse {
			val body = KMessageUpdateRequest(msgId, content).apply(request).body
			return doPost(
				"/api/v3/message/update",
				body
			).toJson().getTyped()
		}

		/**
		 * 删除频道聊天消息
		 * @param msgId 消息ID
		 */
		fun postMessageDelete(msgId: String): KBasicResponse {
			return doPost(
				"/api/v3/message/delete",
				mapOf("msg_id" to msgId)
			).toJson().getTyped()
		}

		/**
		 * 获取频道消息某回应的用户列表
		 * @param msgId 消息ID
		 * @param emoji 表情ID
		 */
		fun getMessageReactionList(msgId: String, emoji: String): KBuggedListResponse<KUserDefinition> {
			return doGet(
				"/api/v3/message/reaction-list",
				mapOf(
					"msg_id" to msgId,
					"emoji" to emoji
				) // 这tm铁是开黑啦的问题，还有下面那个 [getDirectMessageReactionList]
			).toJson().getTyped()
		}

		/**
		 * 给某个消息添加回应
		 * @param msgId 消息ID
		 * @param emojiId 表情ID
		 */
		fun postMessageAddReaction(msgId: String, emojiId: String): KBasicResponse {
			return doPost(
				"/api/v3/message/add-reaction",
				mapOf(
					"msg_id" to msgId,
					"emoji" to emojiId
				)
			).toJson().getTyped()
		}

		/**
		 * 删除消息的某个用户的回应
		 * @param msgId 消息ID
		 * @param emojiId 表情ID
		 * @param userId 用户（默认为自己）
		 */
		fun postMessageDeleteReaction(msgId: String, emojiId: String, userId: String? = null): KBasicResponse {
			return doPost(
				"/api/v3/message/delete-reaction",
				valueNotNullMapOf(
					"msg_id" to msgId,
					"emoji" to emojiId,
					"user_id" to userId
				)
			).toJson().getTyped()
		}

	}

	val userChat by lazy { UserChat() }

	inner class UserChat internal constructor() {

		/**
		 * 获取私聊列表
		 * @param page 目标页数
		 * @param pageSize 每页数据量
		 */
		fun getUserChatList(page: Int? = null, pageSize: Int? = null): KListResponse<KUserChatDefinition> {
			return doGet(
				"/api/v3/user-chat/list",
				valueNotNullMapOf(
					"page" to page,
					"page_size" to pageSize
				)
			).toJson().getTyped()
		}

		/**
		 * 获取私聊详细信息
		 * @param chatCode 私聊ID
		 */
		fun getUserChatView(chatCode: String): KResponse<KUserChatViewDefinition> {
			return doGet(
				"/api/v3/user-chat/view",
				mapOf("chat_code" to chatCode)
			).toJson().getTyped()
		}

		/**
		 * 发起私聊
		 * @param userId 私聊目标用户ID
		 */
		fun postUserChatCreate(userId: String): KResponse<KUserChatViewDefinition> {
			return doPost(
				"/api/v3/user-chat/create",
				mapOf("target_id" to userId).toJson()
			).toJson().getTyped()
		}

		/**
		 * 删除私聊
		 * @param userId 私聊目标用户ID
		 */
		fun postUserChatDelete(userId: String): KBasicResponse {
			return doPost(
				"/api/v3/user-chat/delete",
				mapOf("target_id" to userId).toJson()
			).toJson().getTyped()
		}

	}

	val directMessage by lazy { DirectMessage() }

	inner class DirectMessage internal constructor() {

		/**
		 * 获取私聊消息列表
		 * @param targetIdOrChatCode 私聊目标ID或会话ID
		 * @param request 查询细节设置
		 */
		fun getDirectMessageList(
			targetIdOrChatCode: TargetIdOrChatCode,
			request: KDirectMessageRequest.() -> Unit = {},
		): KListResponse<KMessageDefinition> {
			val query = KDirectMessageRequest(targetIdOrChatCode).apply(request).query
			return doGet(
				"/api/v3/direct-message/list",
				query
			).toJson().getTyped()
		}

		/**
		 * 发送私聊消息
		 * @param targetIdOrChatCode 私聊目标ID或会话ID
		 * @param content 消息内容
		 * @param request 发送细节设置
		 */
		fun postDirectMessageCreate(
			targetIdOrChatCode: TargetIdOrChatCode,
			content: String,
			request: KDirectMessageCreateRequest.() -> Unit = {},
		): KResponse<KMessageCreateData> {
			val body = KDirectMessageCreateRequest(targetIdOrChatCode, content).apply(request).body
			return doPost(
				"/api/v3/direct-message/create",
				body
			).toJson().getTyped()
		}

		/**
		 * 更新私聊消息
		 * @param msgId 消息ID
		 * @param content 更新后的消息内容
		 * @param quote 回复/引用的消息ID
		 */
		fun postDirectMessageUpdate(msgId: String, content: String, quote: String? = null): KBasicResponse {
			return doPost(
				"/api/v3/direct-message/update",
				valueNotNullMapOf(
					"msg_id" to msgId,
					"content" to content,
					"quote" to quote
				)
			).toJson().getTyped()
		}

		/**
		 * 删除私聊消息
		 * @param msgId 消息ID
		 */
		fun postDirectMessageDelete(msgId: String): KBasicResponse {
			return doPost(
				"/api/v3/direct-message/delete",
				mapOf("msg_id" to msgId)
			).toJson().getTyped()
		}

		/**
		 * 获取频道消息某回应的用户列表
		 * @param msgId 消息ID
		 * @param emojiId 表情ID
		 */
		fun getDirectMessageReactionList(msgId: String, emojiId: String): List<KUserDefinition> {
			return doGet(
				"/api/v3/direct-message/reaction-list",
				mapOf(
					"msg_id" to msgId,
					"emoji" to emojiId
				)
			).toJson().getAsJsonArray("data").let {
				gson.fromJson(it, (object : TypeToken<List<KUserDefinition>>() {}.type))
			}
		}

		/**
		 * 给某个消息添加回应
		 * @param msgId 消息ID
		 * @param emojiId 表情ID
		 */
		fun postDirectMessageAddReaction(msgId: String, emojiId: String): KBasicResponse {
			return doPost(
				"/api/v3/direct-message/add-reaction",
				mapOf(
					"msg_id" to msgId,
					"emoji" to emojiId
				)
			).toJson().getTyped()
		}

		/**
		 * 删除消息的某个回应
		 * @param msgId 消息ID
		 * @param emojiId 表情ID
		 * @param userId 用户（默认为自己）
		 */
		fun postDirectMessageDeleteReaction(msgId: String, emojiId: String, userId: String? = null): KBasicResponse {
			return doPost(
				"/api/v3/direct-message/delete-reaction",
				valueNotNullMapOf(
					"msg_id" to msgId,
					"emoji" to emojiId,
					"user_id" to userId
				)
			).toJson().getTyped()
		}

	}

	val gateway by lazy { Gateway() }

	inner class Gateway internal constructor() {

		/**
		 * 获取网关连接地址（用于 WebSocket 方式）
		 */
		fun getGateway(): KResponse<KGatewayData> {
			return doGet("/api/v3/gateway/index").toJson().getTyped()
		}

	}

	val user by lazy { User() }

	inner class User internal constructor() {

		/**
		 * 获取当前登录的用户的信息
		 */
		fun getUserMe(): KResponse<KUserDefinition> {
			return doGet("/api/v3/user/me").toJson().getTyped()
		}

		/**
		 * 获取目标用户信息
		 * @param userId 目标用户ID
		 * @param guildId 服务器ID
		 */
		fun getUserView(userId: String, guildId: String? = null): KResponse<KUserDefinition> {
			return doGet(
				"/api/v3/user/view",
				valueNotNullMapOf(
					"user_id" to userId,
					"guild_id" to guildId
				)
			).toJson().getTyped()
		}

	}

	val asset by lazy { Asset() }

	inner class Asset internal constructor() {

		/**
		 * 上传资源
		 * @param file 资源文件
		 */
		fun postAssetCreate(file: File): KResponse<KAssetCreateData> {
			return doPost(
				"/api/v3/asset/create",
				MultipartBody.Builder().apply {
					setType(MultipartBody.FORM)

					addFormDataPart("file", file.name, file.asRequestBody())
				}.build()
			).toJson().getTyped()
		}

		// 下面是辅助方法

		/**
		 * 将[src]下载到本地，再上传至开黑啦
		 * @throws IllegalStateException 当下载或上传失败时抛出
		 * @return 开黑啦资源链接
		 * @see kaikt.api.util.FileDownloader
		 */
		fun uploadExternal(src: String): String {
			return if(!isKaiAsset(src)) {
				uploadAsset(src, this)
			} else {
				src
			}
		}

		/**
		 * [url]是否是开黑啦的资源
		 */
		fun isKaiAsset(url: String): Boolean {
			return url.startsWith("https://img.kaiheila.cn")
		}

	}

	val guildRole by lazy { GuildRole() }

	/**
	 * 增删查改服务器身分组。
	 * 单独管理某频道的身分组权限请前往 [Channel][KaiApi.Channel] 模块。
	 */
	inner class GuildRole internal constructor() {

		/**
		 * 获取服务器身分组列表
		 * @param guildId 服务器ID
		 * @param page 目标页数
		 * @param pageSize 每页数据量
		 */
		fun getGuildRoleList(
			guildId: String,
			page: Int? = null,
			pageSize: Int? = null,
		): KListResponse<KRoleDefinition> {
			return doGet(
				"/api/v3/guild-role/list",
				valueNotNullMapOf(
					"guild_id" to guildId,
					"page" to page,
					"page_size" to pageSize
				)
			).toJson().getTyped()
		}

		/**
		 * 创建服务器身分组
		 * @param guildId 服务器ID
		 * @param name 身分组名称（默认“新角色”）
		 */
		fun postGuildRoleCreate(guildId: String, name: String? = null): KResponse<KRoleDefinition> {
			return doPost(
				"/api/v3/guild-role/create",
				valueNotNullMapOf(
					"guild_id" to guildId,
					"name" to name
				)
			).toJson().getTyped()
		}

		/**
		 * 更新服务器身分组
		 * @param guildId 服务器ID
		 * @param roleId 身分组ID
		 * @param request 更新细节设置
		 */
		fun postGuildRoleUpdate(
			guildId: String,
			roleId: String,
			request: KGuildRoleUpdateRequest.() -> Unit = {},
		): KResponse<KRoleDefinition> {
			val body = KGuildRoleUpdateRequest(guildId, roleId).apply(request).body
			return doPost(
				"/api/v3/guild-role/update",
				body
			).toJson().getTyped()
		}

		/**
		 * 删除服务器身分组
		 * @param guildId 服务器ID
		 * @param roleId 身分组ID
		 */
		fun postGuildRoleDelete(guildId: String, roleId: String): KBasicResponse {
			return doPost(
				"/api/v3/guild-role/delete",
				mapOf(
					"guild_id" to guildId,
					"role_id" to roleId
				)
			).toJson().getTyped()
		}

		/**
		 * 授予用户身分组
		 * @param guildId 服务器ID
		 * @param userId 用户ID
		 * @param roleId 身分组ID
		 */
		fun postGuildRoleGrant(guildId: String, userId: String, roleId: String): KResponse<KGuildRoleGrantRevokeData> {
			return doPost(
				"/api/v3/guild-role/grant",
				mapOf(
					"guild_id" to guildId,
					"user_id" to userId,
					"role_id" to roleId
				)
			).toJson().getTyped()
		}

		/**
		 * 剥夺用户身分组
		 * @param guildId 服务器ID
		 * @param userId 用户ID
		 * @param roleId 身分组
		 */
		fun postGuildRoleRevoke(guildId: String, userId: String, roleId: String): KResponse<KGuildRoleGrantRevokeData> {
			return doPost(
				"/api/v3/guild-role/revoke",
				mapOf(
					"guild_id" to guildId,
					"user_id" to userId,
					"role_id" to roleId
				)
			).toJson().getTyped()
		}

	}

	val guildEmoji by lazy { GuildEmoji() }

	inner class GuildEmoji internal constructor() {

		/**
		 * 获取服务器表情列表
		 * @param guildId 服务器ID
		 */
		fun getGuildEmojiList(guildId: String): KListResponse<KGuildEmojiDefinition> {
			return doGet(
				"/api/v3/guild-emoji/list",
				mapOf("guild_id" to guildId)
			).toJson().getTyped()
		}

		/**
		 * 创建服务器表情
		 * @param guildId 服务器ID
		 * @param emoji 表情文件（PNG格式）
		 * @param name 表情名称
		 */
		@WorkInProgress
		fun postGuildEmojiCreate(guildId: String, emoji: File, name: String): KResponse<KGuildEmojiCreateData> {
			return doPost(
				"/api/v3/guild-emoji/create",
				MultipartBody.Builder().apply {
					setType(MultipartBody.FORM)

					addFormDataPart("guild_id", guildId)
					addFormDataPart("emoji", name, emoji.asRequestBody())
				}.build()
			).toJson().getTyped()
		}

		/**
		 * 更新（改名）服务器表情
		 * @param emojiId 表情ID
		 * @param name 新名称
		 */
		fun postGuildEmojiUpdate(emojiId: String, name: String): KBasicResponse {
			return doPost(
				"/api/v3/guild-emoji/update",
				mapOf(
					"id" to emojiId,
					"name" to name
				)
			).toJson().getTyped()
		}

		/**
		 * 删除服务器表情
		 * @param emojiId 表情ID
		 */
		fun postGuildEmojiDelete(emojiId: String): KBasicResponse {
			return doPost(
				"/api/v3/guild-emoji/delete",
				mapOf(
					"id" to emojiId
				)
			).toJson().getTyped()
		}

	}

	val invite by lazy { Invite() }

	inner class Invite internal constructor() {

		/**
		 * 获取邀请列表
		 * @param guildOrChannel 服务器或频道
		 */
		fun getInviteList(
			guildOrChannel: GuildOrChannel,
			page: Int? = null,
			pageSize: Int? = null,
		): KListResponse<KInviteData> {
			return doGet(
				"/api/v3/invite/list",
				valueNotNullMapOf(
					guildOrChannel.type to guildOrChannel.value,
					"page" to page,
					"page_size" to pageSize
				)
			).toJson().getTyped()
		}

		/**
		 * 创建邀请链接
		 * @param guildOrChannel 服务器或频道
		 * @param expireTime 过期时间（秒）
		 * @param expireUsageCounter 过期次数
		 */
		fun postInviteCreate(
			guildOrChannel: GuildOrChannel,
			expireTime: InviteDuration? = null,
			expireUsageCounter: InviteUsageCounter? = null,
		): KResponse<KInviteCreateData> {
			return doPost(
				"/api/v3/invite/create",
				valueNotNullMapOf(
					guildOrChannel.type to guildOrChannel.value,
					"duration" to expireTime?.value,
					"setting_times" to expireUsageCounter?.value
				)
			).toJson().getTyped()
		}

		/**
		 * 删除邀请链接
		 * @param urlCode 邀请码（例如：https://kaihei.co/A1B2C3 中的 A1B2C3）
		 * @param guildOrChannel 服务器或频道
		 */
		fun postInviteDelete(urlCode: String, guildOrChannel: GuildOrChannel): KBasicResponse {
			return doPost(
				"/api/v3/invite/delete",
				valueNotNullMapOf(
					"url_code" to urlCode,
					guildOrChannel.type to guildOrChannel.value
				)
			).toJson().getTyped()
		}
	}

	val blacklist by lazy { Blacklist() }

	inner class Blacklist internal constructor() {

		/**
		 * 获取服务器黑名单列表
		 * @param guildId 服务器ID
		 */
		fun getBlacklist(guildId: String): KListResponse<KBlacklistData> {
			return doGet(
				"/api/v3/blacklist/list",
				valueNotNullMapOf(
					"guild_id" to guildId
				)
			).toJson().getTyped()
		}

		/**
		 * 加入黑名单
		 * @param guildId 服务器ID
		 * @param targetId 目标用户ID
		 * @param remark 加入黑名单原因
		 * @param delMessageDays 删除最近几天的消息（最大7天，默认为0）
		 */
		fun postBlacklistCreate(
			guildId: String,
			targetId: String,
			remark: String? = null,
			delMessageDays: Int? = null,
		): KBasicResponse {
			return doPost(
				"/api/v3/blacklist/create",
				valueNotNullMapOf(
					"guild_id" to guildId,
					"target_id" to targetId,
					"remark" to remark,
					"del_msg_days" to delMessageDays
				)
			).toJson().getTyped()
		}

		/**
		 * 移除黑名单
		 * @param guildId 服务器ID
		 * @param targetId 目标用户ID
		 */
		fun postBlacklistDelete(guildId: String, targetId: String): KBasicResponse {
			return doPost(
				"/api/v3/blacklist/delete",
				mapOf(
					"guild_id" to guildId,
					"target_id" to targetId
				)
			).toJson().getTyped()
		}
	}

	val badge by lazy { Badge() }

	inner class Badge internal constructor() {

		/**
		 * 获取服务器 Badge
		 * @param guildId 服务器ID
		 * @param style Badge 样式
		 * @return svg标签字符串
		 */
		fun getBadge(guildId: String, style: BadgeStyle? = null): Response {
			return doGet(
				"/api/v3/badge/guild",
				valueNotNullMapOf(
					"guild_id" to guildId,
					"style" to style?.value
				)
			)
		}

	}

}

/*

  后面这坨屎山日后再改。希望开黑啦别瞎JB改Api。
				by Taskeren-3

 */

/**
 * 把传进来的 JsonElement 转为 JsonObject，用于无 data 的操作
 *
 * {
 * 	"code": 0,
 * 	"message": "操作成功",
 * 	"data": {}
 * }
 *
 */
private fun JsonElement.asResponseData(): KaiResponseJsonObject {
	if(this.isJsonObject) {
		return this.asJsonObject
	} else {
		throw ClassCastException("!object")
	}
}

private fun Response.toJson(): KaiResponseJsonObject = JsonParser.parseReader(this.body?.charStream())
	.asResponseData().apply { // 错误检查
		if(!this.isJsonObject) {
			throw IllegalStateException("Response must be JsonObject, but currently is ${this.javaClass.simpleName}")
		}
	}

/**
 * 开黑啦Api返回回来的数据
 */
internal typealias KaiResponseJsonObject = JsonObject

private inline fun <reified T> KaiResponseJsonObject.getTyped(): T =
	gson.fromJson(this, (object : TypeToken<T>() {}.type))

private fun <K, V> Map<K, V>.toJson(): String = gson.toJson(this)

// Public

internal fun Boolean.toInt() = if(this) 1 else 0

internal fun Boolean?.toInt() = this?.toInt()