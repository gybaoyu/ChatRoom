<script setup>
import {ref, onMounted, onUnmounted} from 'vue';

const messages = ref([]);
const inputMessage = ref('');
const socket = ref(null);
let username = ref('');
// 获取URL参数


const wsPort = 8081; // 默认8081
// 初始化WebSocket连接
function initWebSocket() {
  console.log(wsPort)
  const socketInstance = new WebSocket(`ws://localhost:8081/ws`);
  socketInstance.onopen = () => {
    console.log('WebSocket连接已建立 (端口8081)');
  };

  socketInstance.onmessage = (event) => {
    const data = JSON.parse(event.data);
    messages.value.push(data);
    console.log('收到消息:', event.data);
  };

  socketInstance.onclose = () => {
    console.log('WebSocket连接已关闭');
  };

  return socketInstance;
}

onMounted(() => {
  socket.value = initWebSocket();
});

onUnmounted(() => {
  socket.value?.close();
});

// 发送消息
function sendMessage() {
  if (inputMessage.value.trim() && socket.value?.readyState === WebSocket.OPEN) {
    socket.value.send('{' +
        '"name": "' + username + '",' +
        '"message": "' + inputMessage.value+'"}');
    inputMessage.value = '';
  }
}

function register() {
  if (inputMessage.value.trim() && socket.value?.readyState === WebSocket.OPEN) {
    socket.value.send('{' +
        '"name": "' + inputMessage.value + '",' +
        '"message": "/register/"}');
    username = inputMessage.value;
    inputMessage.value = '';
  }
}
</script>

<template>
  <div class="websocket-container">
    <div class="message-display">
      <div class="message-box">
        <h3>{{wsPort}}</h3>
        <h3>消息列表:</h3>
        <div
            v-for="(msg, index) in messages"
            :key="index"
            class="message-item"
        >
          [{{msg.msgDate}}]{{ msg.name }}: {{msg.message}}
        </div>
      </div>
    </div>
    <br>
    <div class="input-area">
      <input
          v-model="inputMessage"
          @keyup.enter="sendMessage"
          placeholder="输入消息..."
          class="message-input"
      />

    </div>
    <button @click="sendMessage" class="send-button">发送</button><br>
    <button @click="register" class="send-button" v-if="username===''">注册用户</button>
  </div>
</template>

<style scoped>
.websocket-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.input-area {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.message-input {
  flex: 1;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.send-button {
  padding: 10px 20px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.send-button:hover {
  background-color: #3aa876;
}

.message-display {
  display: grid;
  grid-template-columns: 1fr;
  gap: 20px;
}

.message-box {
  border: 1px solid #eee;
  border-radius: 4px;
  padding: 15px;
  max-height: 400px;
  overflow-y: auto;
}

.message-item {
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
}

.message-item:last-child {
  border-bottom: none;
}
</style>