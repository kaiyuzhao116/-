<script setup>
// Vue3 <script setup> 语法：无需 export default，顶层变量自动暴露给模板
import { ref, computed, nextTick, watch } from 'vue';
import { useNettySocket } from './composables/useNettySocket.js';

// 从组合式函数拿到响应式状态和方法
const { status, messages, online, connect, send, disconnect, notice } = useNettySocket();

// 后端地址：登录走 Spring Boot(8080) REST，长连接走 Netty(8090) WebSocket
const REST_BASE = 'http://localhost:8080';
const WS_BASE = 'ws://localhost:8090/ws';

const input = ref('');
const logRef = ref(null);

// 登录状态
const username = ref('');
const token = ref('');
const logging = ref(false);
const loggedIn = computed(() => !!token.value);

// 登录拿 token -> 用该 token 建立 WebSocket 连接
async function loginAndConnect() {
  const name = username.value.trim();
  if (!name) {
    notice('请先输入用户名');
    return;
  }
  logging.value = true;
  try {
    const resp = await fetch(`${REST_BASE}/netty_user/login?username=${encodeURIComponent(name)}`, {
      method: 'POST',
    });
    if (!resp.ok) throw new Error(`HTTP ${resp.status}`);
    const data = await resp.json();
    token.value = data.token;
    notice('登录成功，已获取 token，开始连接...');
    connect(`${WS_BASE}?token=${encodeURIComponent(data.token)}`);
  } catch (e) {
    notice(`登录异常：${e.message}（请确认 8080 后端已启动）`);
  } finally {
    logging.value = false;
  }
}

// 退出：断开连接并清空登录态
function logout() {
  disconnect();
  token.value = '';
}

function onSend() {
  send(input.value);
  input.value = '';
}

// 新消息到达时自动滚动到底部
watch(
  () => messages.value.length,
  async () => {
    await nextTick();
    if (logRef.value) logRef.value.scrollTop = logRef.value.scrollHeight;
  }
);

const statusText = {
  connecting: '连接中',
  open: '已连接',
  closed: '未连接',
};
</script>

<template>
  <div class="app">
    <h2>Netty WebSocket × Vue3</h2>

    <div class="toolbar">
      <input
        v-model="username"
        type="text"
        class="name-input"
        placeholder="输入用户名"
        :disabled="online || logging"
        @keydown.enter="loginAndConnect"
      />
      <button class="green" :disabled="online || logging" @click="loginAndConnect">
        {{ logging ? '登录中…' : '登录并连接' }}
      </button>
      <span>状态：</span>
      <span class="badge" :class="status">{{ statusText[status] }}</span>
      <button class="red" :disabled="!online" @click="logout">断开</button>
    </div>

    <div v-if="loggedIn" class="who">
      当前用户：<b>{{ username }}</b>（已绑定 token，聊天时别人会看到你的名字）
    </div>

    <div ref="logRef" class="log">
      <div v-for="(m, i) in messages" :key="i" class="msg" :class="m.from">
        <span class="time">{{ m.time }}</span>
        <span class="who">
          {{ m.from === 'me' ? '我' : m.from === 'server' ? '服务端' : '系统' }}：
        </span>
        <span class="text">{{ m.text }}</span>
      </div>
      <div v-if="messages.length === 0" class="empty">还没有消息，输入用户名点「登录并连接」开始聊天</div>
    </div>

    <div class="input-row">
      <input
        v-model="input"
        type="text"
        placeholder="输入消息，回车或点发送"
        :disabled="!online"
        @keydown.enter="onSend"
      />
      <button :disabled="!online" @click="onSend">发送</button>
    </div>
  </div>
</template>

<style scoped>
/* scoped：样式只作用于本组件 */
.app {
  font-family: 'Microsoft YaHei', Arial, sans-serif;
  max-width: 720px;
  margin: 30px auto;
  padding: 0 16px;
  color: #2c3e50;
}
h2 {
  margin-bottom: 16px;
}
.toolbar {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
}
.name-input {
  width: 160px;
  padding: 6px 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.who {
  margin-bottom: 10px;
  font-size: 13px;
  color: #16a085;
}
.badge {
  display: inline-block;
  min-width: 56px;
  text-align: center;
  padding: 3px 10px;
  border-radius: 4px;
  color: #fff;
  background: #e74c3c;
  font-size: 13px;
}
.badge.open {
  background: #27ae60;
}
.badge.connecting {
  background: #f39c12;
}
.log {
  border: 1px solid #ddd;
  border-radius: 4px;
  height: 360px;
  overflow-y: auto;
  padding: 10px;
  background: #fafafa;
  line-height: 1.7;
}
.empty {
  color: #b0b0b0;
  text-align: center;
  margin-top: 20px;
}
.msg .time {
  color: #aaa;
  font-size: 12px;
  margin-right: 6px;
}
.msg.me .text {
  color: #2980b9;
}
.msg.server .text {
  color: #16a085;
}
.msg.sys .text {
  color: #7f8c8d;
  font-style: italic;
}
.input-row {
  display: flex;
  gap: 8px;
  margin-top: 12px;
}
.input-row input {
  flex: 1;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
button {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  background: #34495e;
  color: #fff;
  cursor: pointer;
}
button:disabled {
  background: #bdc3c7;
  cursor: not-allowed;
}
button.green {
  background: #27ae60;
}
button.red {
  background: #e74c3c;
}
</style>
