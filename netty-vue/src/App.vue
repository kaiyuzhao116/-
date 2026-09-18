<script setup>
// Vue3 <script setup> 语法：无需 export default，顶层变量自动暴露给模板
import { ref, nextTick, watch } from 'vue';
import { useNettySocket } from './composables/useNettySocket.js';

// 从组合式函数拿到响应式状态和方法
const { status, messages, online, connect, send, disconnect } = useNettySocket();

const input = ref('');
const logRef = ref(null);

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
      <span>状态：</span>
      <span class="badge" :class="status">{{ statusText[status] }}</span>
      <button class="green" :disabled="online || status === 'connecting'" @click="connect">连接</button>
      <button class="red" :disabled="!online" @click="disconnect">断开</button>
    </div>

    <div ref="logRef" class="log">
      <div v-for="(m, i) in messages" :key="i" class="msg" :class="m.from">
        <span class="time">{{ m.time }}</span>
        <span class="who">
          {{ m.from === 'me' ? '我' : m.from === 'server' ? '服务端' : '系统' }}：
        </span>
        <span class="text">{{ m.text }}</span>
      </div>
      <div v-if="messages.length === 0" class="empty">还没有消息，点击「连接」开始通讯</div>
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
