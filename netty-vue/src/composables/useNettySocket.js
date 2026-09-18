import { ref, computed, onUnmounted } from 'vue';

/**
 * useNettySocket —— 把浏览器原生 WebSocket 封装成 Vue3 组合式 API
 *
 * 学习对照（前端 <-> Netty 服务端 WebSocketServerHandler）：
 *   ws.onopen    <-> handlerAdded()      连接建立
 *   ws.onmessage <-> channelRead0()      收到 TextWebSocketFrame
 *   ws.onclose   <-> handlerRemoved()    连接断开
 *
 * 暴露的响应式状态可直接在模板里绑定，做到"连接状态实时上屏"。
 */
export function useNettySocket(url = 'ws://localhost:8090/ws?token=abc123') {
  // 连接状态：connecting | open | closed
  const status = ref('closed');
  // 消息日志列表，元素：{ from: 'me' | 'server' | 'sys', text, time }
  const messages = ref([]);
  // 未读在线标记，便于按钮禁用控制
  const online = computed(() => status.value === 'open');

  let ws = null;
  // 心跳与自动重连相关
  let heartbeatTimer = null;
  let reconnectTimer = null;
  let manualClosed = false; // 是否用户主动关闭（主动关闭不重连）

  function now() {
    return new Date().toLocaleTimeString();
  }

  function push(from, text) {
    messages.value.push({ from, text, time: now() });
  }

  function connect() {
    if (ws && (ws.readyState === WebSocket.OPEN || ws.readyState === WebSocket.CONNECTING)) {
      return;
    }
    manualClosed = false;
    status.value = 'connecting';
    push('sys', `正在连接 ${url} ...`);

    ws = new WebSocket(url);

    ws.onopen = () => {
      status.value = 'open';
      push('sys', 'WebSocket 连接已建立');
      startHeartbeat();
    };

    ws.onmessage = (event) => {
      push('server', event.data);
    };

    ws.onerror = () => {
      push('sys', 'WebSocket 发生错误');
    };

    ws.onclose = () => {
      status.value = 'closed';
      stopHeartbeat();
      push('sys', '连接已关闭');
      if (!manualClosed) {
        scheduleReconnect();
      }
    };
  }

  function send(text) {
    const content = (text || '').trim();
    if (!content) return;
    if (!ws || ws.readyState !== WebSocket.OPEN) {
      push('sys', '未连接，无法发送');
      return;
    }
    ws.send(content);
    push('me', content);
  }

  function disconnect() {
    manualClosed = true;
    clearTimeout(reconnectTimer);
    if (ws) ws.close();
  }

  // 心跳：每 30s 发送一个 ping 文本，维持连接（服务端把非 bye 都当普通消息回应）
  function startHeartbeat() {
    stopHeartbeat();
    heartbeatTimer = setInterval(() => {
      if (ws && ws.readyState === WebSocket.OPEN) {
        ws.send('ping');
      }
    }, 30000);
  }

  function stopHeartbeat() {
    if (heartbeatTimer) {
      clearInterval(heartbeatTimer);
      heartbeatTimer = null;
    }
  }

  // 断线自动重连（3s 后重试）
  function scheduleReconnect() {
    clearTimeout(reconnectTimer);
    push('sys', '3 秒后尝试重新连接...');
    reconnectTimer = setTimeout(connect, 3000);
  }

  // 组件卸载时清理，避免内存泄漏
  onUnmounted(() => {
    manualClosed = true;
    stopHeartbeat();
    clearTimeout(reconnectTimer);
    if (ws) ws.close();
  });

  return { status, messages, online, connect, send, disconnect };
}
