package kaikt.api

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import kaikt.api.entity.definition.*
import kaikt.api.entity.enum.KChannelType
import kaikt.api.entity.enum.KGuildMuteType
import kaikt.api.entity.permission.KPermission
import kaikt.api.entity.request.*
import kaikt.api.entity.response.*
import kaikt.api.util.valueNotNullMapOf
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.intellij.lang.annotations.MagicConstant
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URLEncoder

private val log = LoggerFactory.getLogger("KaiApi")

class KaiApi(private val token: KToken) {

	private val cli = OkHttpClient.Builder().build()

	private val mediaTypeJson = "application/json;charset=UTF-8".toMediaType()

	internal fun doGet(
		endpoint: String,
		query: Map<String, Any> = valueNotNullMapOf(),
		headers: Map<String, String> = valueNotNullMapOf(),
		noCompress: Boolean = true
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
			.host("www.kaiheila.cn")
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
			.addHeader("Authorization", token.toString())
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
		noCompress: Boolean = true
	): Response {

		// Fix '/'
		var finalEndpoint = endpoint
		if(endpoint.startsWith('/')) {
			finalEndpoint = endpoint.substring(1)
		}

		val url = HttpUrl.Builder()
			.scheme("https")
			.host("www.kaiheila.cn")
			.addPathSegments(finalEndpoint)
			.addQueryParameter("compress", (if(noCompress) "0" else "1"))
			.build()

		val request = Request.Builder()
			.url(url)
			.post(body)
			.addHeader("Authorization", token.toString())
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
		noCompress: Boolean = true
	) = doPost(endpoint, body.toRequestBody("application/json".toMediaType()), headers, noCompress).also {
		log.debug("[POST] $endpoint")
		log.debug("[POST] $body")
	}

	internal fun doPost(
		endpoint: String,
		body: Map<*, *>,
		headers: Map<String, String> = valueNotNullMapOf(),
		noCompress: Boolean = true
	) = doPost(endpoint, gson.toJson(body), headers, noCompress)

	inner class Guild {

		/**
		 * 获取当前用户加入的服务器列表
		 */
		fun getGuildList(): List<KGuildDefinition> {
			return doGet("/api/v3/guild/list").toJson().getData().getTypedList()
		}

		/**
		 * 获取服务器详情
		 * @param guildId 服务器ID
		 */
		fun getGuildView(guildId: String): KGuildViewDefinition {
			return doGet("/api/v3/guild/view", "guild_id" map guildId).toJson().getData().getTyped()
		}

		/**
		 * 获取服务器中的用户列表
		 * @param guildId 服务器ID
		 * @param conf 请求设置
		 */
		fun getGuildUserList(guildId: String, conf: KGuildUserListRequest.() -> Unit = {}): KGuildUserListData {
			return doGet(
				"/api/v3/guild/user-list",
				KGuildUserListRequest(guildId).apply(conf).toQueryMap()
			).toJson().getData().getTyped()
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
				("guild_id" map guildId).toJson()
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
		fun getGuildMuteList(guildId: String, returnType: String = "detail"): KGuildMuteListData {
			return doGet(
				"/api/v3/guild-mute/list",
				valueNotNullMapOf(
					"guild_id" to guildId,
					"return_type" to returnType
				)
			).toJson().getData().getTyped()
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
			@MagicConstant(valuesFromClass = KGuildMuteType::class) type: Int
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
			@MagicConstant(valuesFromClass = KGuildMuteType::class) type: Int
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

	inner class Channel {

		/**
		 * 获取服务器频道列表
		 * @param guildId 服务器ID
		 * @param type 频道类型；1-文字，2-语音
		 */
		fun getChannelList(guildId: String, type: KChannelType? = null): List<KChannelDefinition> {
			return doGet(
				"/api/v3/channel/list",
				valueNotNullMapOf(
					"guild_id" to guildId,
					"type" to type?.type
				)
			).toJson().getData().getTypedList()
		}

		/**
		 * 获取频道详细信息
		 * @param channelId 频道ID
		 */
		fun getChannelView(channelId: String): KChannelViewDefinition {
			return doGet(
				"/api/v3/channel/view",
				"target_id" map channelId
			).toJson().getData().getTyped()
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
			request: KChannelCreateRequest.() -> Unit = {}
		): KChannelViewDefinition {
			val body = KChannelCreateRequest(guildId, name).apply(request).body
			return doPost(
				"/api/v3/channel/create",
				body.apply { println(this) }
			).toJson().getData().getTyped()
		}

		/**
		 * 删除频道
		 * @param channelId 频道ID
		 */
		fun postChannelDelete(channelId: String): KBasicResponse {
			return doPost(
				"/api/v3/channel/delete",
				("channel_id" map channelId).toJson()
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
		fun getChannelRoleIndex(channelId: String): KChannelRoleIndexData {
			return doGet(
				"/api/v3/channel-role/index",
				"channel_id" map channelId
			).toJson().getData().getTyped()
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
			deny: Int? = null
		): KChannelRoleUpdateData {
			return doPost(
				"/api/v3/channel-role/update",
				valueNotNullMapOf(
					"channel_id" to channelId,
					"type" to roleIdOrUserId.type,
					"value" to roleIdOrUserId.value,
					"allow" to allow,
					"deny" to deny
				)
			).toJson().getData().getTyped()
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

	inner class Message {

		/**
		 * 获取频道聊天消息列表
		 * @param channelId 频道ID
		 * @param request 查询细节设置
		 */
		fun getMessageList(channelId: String, request: KMessageListRequest.() -> Unit = {}): List<KMessageDefinition> {
			val query = KMessageListRequest(channelId).apply(request).query
			return doGet(
				"/api/v3/message/list",
				query
			).toJson().getData().getTypedList()
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
			request: KMessageCreateRequest.() -> Unit = {}
		): KMessageCreateData {
			val body = KMessageCreateRequest(channelId, content).apply(request).body
			return doPost(
				"/api/v3/message/create",
				body
			).toJson().getData().getTyped()
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
			request: KMessageUpdateRequest.() -> Unit = {}
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
				"msg_id" map msgId
			).toJson().getTyped()
		}

		/**
		 * 获取频道消息某回应的用户列表
		 * @param msgId 消息ID
		 * @param emoji 表情ID
		 */
		fun getMessageReactionList(msgId: String, emoji: String): List<KUserDefinition> {
			return doGet(
				"/api/v3/message/reaction-list",
				mapOf(
					"msg_id" to msgId,
					"emoji" to emoji
				) // 这tm铁是开黑啦的问题，还有下面那个 [getDirectMessageReactionList]
			).toJson().getAsJsonArray("data").let {
				gson.fromJson(it, (object : TypeToken<List<KUserDefinition>>() {}.type))
			}
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

	inner class UserChat {

		/**
		 * 获取私聊列表
		 * @param page 目标页数
		 * @param pageSize 每页数据量
		 */
		fun getUserChatList(page: Int? = null, pageSize: Int? = null): List<KUserChatDefinition> {
			return doGet(
				"/api/v3/user-chat/list",
				valueNotNullMapOf(
					"page" to page,
					"page_size" to pageSize
				)
			).toJson().getData().getTypedList()
		}

		/**
		 * 获取私聊详细信息
		 * @param chatCode 私聊ID
		 */
		fun getUserChatView(chatCode: String): KUserChatViewDefinition {
			return doGet(
				"/api/v3/user-chat/view",
				"chat_code" map chatCode
			).toJson().getData().getTyped()
		}

		/**
		 * 发起私聊
		 * @param userId 私聊目标用户ID
		 */
		fun postUserChatCreate(userId: String): KUserChatViewDefinition {
			return doPost(
				"/api/v3/user-chat/create",
				("target_id" map userId).toJson()
			).toJson().getData().getTyped()
		}

		/**
		 * 删除私聊
		 * @param userId 私聊目标用户ID
		 */
		fun postUserChatDelete(userId: String): KBasicResponse {
			return doPost(
				"/api/v3/user-chat/delete",
				("target_id" map userId).toJson()
			).toJson().getTyped()
		}

	}

	inner class DirectMessage {

		/**
		 * 获取私聊消息列表
		 * @param targetIdOrChatCode 私聊目标ID或会话ID
		 * @param request 查询细节设置
		 */
		fun getDirectMessageList(
			targetIdOrChatCode: TargetIdOrChatCode,
			request: KDirectMessageRequest.() -> Unit = {}
		): List<KMessageDefinition> {
			val query = KDirectMessageRequest(targetIdOrChatCode).apply(request).query
			return doGet(
				"/api/v3/direct-message/list",
				query
			).toJson().getData().getTypedList()
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
			request: KDirectMessageCreateRequest.() -> Unit = {}
		): KMessageCreateData {
			val body = KDirectMessageCreateRequest(targetIdOrChatCode, content).apply(request).body
			return doPost(
				"/api/v3/direct-message/create",
				body
			).toJson().getData().getTyped()
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
				"msg_id" map msgId
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

	inner class Gateway {

		/**
		 * 获取网关连接地址（用于 WebSocket 方式）
		 */
		fun getGateway(): KGatewayData {
			return doGet("/api/v3/gateway/index").toJson().getData().getTyped()
		}

	}

	inner class User {

		/**
		 * 获取当前登录的用户的信息
		 */
		fun getUserMe(): KUserDefinition {
			return doGet("/api/v3/user/me").toJson().getData().getTyped()
		}

		/**
		 * 获取目标用户信息
		 * @param userId 目标用户ID
		 * @param guildId 服务器ID
		 */
		fun getUserView(userId: String, guildId: String? = null): KUserDefinition {
			return doGet(
				"/api/v3/user/view",
				valueNotNullMapOf(
					"user_id" to userId,
					"guild_id" to guildId
				)
			).toJson().getData().getTyped()
		}

	}

	// TODO: 媒体模块接口

	/**
	 * 增删查改服务器身分组。
	 * 单独管理某频道的身分组权限请前往 [Channel][KaiApi.Channel] 模块。
	 */
	inner class GuildRole {

		/**
		 * 获取服务器身分组列表
		 * @param guildId 服务器ID
		 * @param page 目标页数
		 * @param pageSize 每页数据量
		 */
		fun getGuildRoleList(guildId: String, page: Int? = null, pageSize: Int? = null): List<KRoleDefinition> {
			return doGet(
				"/api/v3/guild-role/list",
				valueNotNullMapOf(
					"guild_id" to guildId,
					"page" to page,
					"page_size" to pageSize
				)
			).toJson().getData().getTypedList()
		}

		/**
		 * 创建服务器身分组
		 * @param guildId 服务器ID
		 * @param name 身分组名称（默认“新角色”）
		 */
		fun postGuildRoleCreate(guildId: String, name: String? = null): KRoleDefinition {
			return doPost(
				"/api/v3/guild-role/create",
				valueNotNullMapOf(
					"guild_id" to guildId,
					"name" to name
				)
			).toJson().getData().getTyped()
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
			request: KGuildRoleUpdateRequest.() -> Unit = {}
		): KRoleDefinition {
			val body = KGuildRoleUpdateRequest(guildId, roleId).apply(request).body
			return doPost(
				"/api/v3/guild-role/update",
				body
			).toJson().getData().getTyped()
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
		fun postGuildRoleGrant(guildId: String, userId: String, roleId: String): KGuildRoleGrantRevokeData {
			return doPost(
				"/api/v3/guild-role/grant",
				mapOf(
					"guild_id" to guildId,
					"user_id" to userId,
					"role_id" to roleId
				)
			).toJson().getData().getTyped()
		}

		/**
		 * 剥夺用户身分组
		 * @param guildId 服务器ID
		 * @param userId 用户ID
		 * @param roleId 身分组
		 */
		fun postGuildRoleRevoke(guildId: String, userId: String, roleId: String): KGuildRoleGrantRevokeData {
			return doPost(
				"/api/v3/guild-role/revoke",
				mapOf(
					"guild_id" to guildId,
					"user_id" to userId,
					"role_id" to roleId
				)
			).toJson().getData().getTyped()
		}

	}

	inner class GuildEmoji {

		/**
		 * 获取服务器表情列表
		 * @param guildId 服务器ID
		 */
		fun getGuildEmojiList(guildId: String): List<KGuildEmojiDefinition> {
			return doGet(
				"/api/v3/guild-emoji/list",
				"guild_id" map guildId
			).toJson().getData().getTypedList()
		}

		/**
		 * 创建服务器表情
		 * @param guildId 服务器ID
		 * @param emoji 表情文件（PNG格式）
		 * @param name 表情名称
		 */
		@WorkInProgress
		fun postGuildEmojiCreate(guildId: String, emoji: File, name: String): KGuildEmojiCreateData {
			return doPost(
				"/api/v3/guild-emoji/create",
				MultipartBody.Builder().apply {
					setType(MultipartBody.FORM)

					addFormDataPart("guild_id", guildId)
					addFormDataPart("emoji", name, emoji.asRequestBody())
				}.build().apply {
					println(this)
				}
			).toJson().getData().getTyped()
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

}

/*

  后面这坨屎山日后再改。希望开黑啦别瞎JB改Api。
				by Taskeren-3

 */

private val gson = Gson()

private fun Response.toJson(): KaiResponseData = JsonParser.parseReader(this.body?.charStream()).asResponseData()

/**
 * 开黑啦Api返回回来的数据
 */
typealias KaiResponseData = JsonObject
typealias KaiResponseDataItems = JsonArray

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
private fun JsonElement.asResponseData(): KaiResponseData {
	log.debug(this.toString())
	if(this.isJsonObject) {
		return this.asJsonObject
	} else {
		throw ClassCastException("!object")
	}
}

private fun JsonElement.getData(): KaiResponseData {
	if(this.isJsonObject && this.asJsonObject.has("data")) {
		return this.asJsonObject.getAsJsonObject("data")
	} else {
		throw NoSuchElementException("data")
	}
}

private fun JsonObject.getItems(): KaiResponseDataItems {
	if(this.has("items")) {
		return this.getAsJsonArray("items")
	} else {
		throw NoSuchElementException("items")
	}
}

private inline fun <reified T> KaiResponseData.getTyped(): T = gson.fromJson(this, (object : TypeToken<T>() {}.type))
private inline fun <reified T> KaiResponseData.getTypedList(): T =
	gson.fromJson(this.getItems().also { println(it) }, (object : TypeToken<T>() {}.type))

private infix fun <A, B> A.map(that: B): Map<A, B> = mapOf(this to that)

private fun <K, V> Map<K, V>.toJson(): String = gson.toJson(this)

// Encode

private fun Any.urlEncode() = URLEncoder.encode(this.toString(), Charsets.UTF_8)

// Public

fun Boolean.toInt() = if(this) 1 else 0

fun Boolean?.toInt() = this?.toInt()