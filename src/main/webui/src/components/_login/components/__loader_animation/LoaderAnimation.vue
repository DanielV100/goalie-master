<script setup>
import * as GlobalConfig from '../../../../globals/gloablConfig.js'
import * as LocalConfig from './resources/loaderAnimationConfig.js';
import {ref} from "vue";

const waitingText = ref();
//every two seconds there is another waiting text
const delay = (ms) => new Promise(resolve => setTimeout(resolve, ms));

/**
 * Changes the waiting texts every two seconds.
 * @returns {Promise<void>}
 */
async function changeWaitingText() {
  for(let i = 0; i < LocalConfig.WAITING_TEXTS.length; i++) {
    waitingText.value = LocalConfig.WAITING_TEXTS[i];
    await delay(2000);
  }
  GlobalConfig.changeWindowToTargetRoute('Overview');
}
changeWaitingText();
</script>

<template>
  <div class="__loader_animation">
      <div class="loader"></div>
      <p class="waiting_text">{{ waitingText }}</p>
  </div>
</template>

<style>
.loader {
  margin: auto auto;
  border: 16px solid var(--avocadoGreenLowOpacity);
  border-radius: 50%;
  border-top: 16px solid var(--avocadoGreen);
  width: 140px;
  height: 140px;
  -webkit-animation: spin 2s linear infinite;
  animation: spin 2s linear infinite;
}

.waiting_text {
  text-align: center;
  margin-top: 10px;
  opacity: 0;
  animation: fadeInOut 2s linear infinite;
}

@-webkit-keyframes spin {
  0% { -webkit-transform: rotate(0deg); }
  100% { -webkit-transform: rotate(360deg); }
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

@keyframes fadeInOut {
  0%, 100% {
    opacity: 0;
  }
  50% {
    opacity: 1;
  }
}
</style>