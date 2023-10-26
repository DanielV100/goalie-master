<script setup>
import { onMounted } from 'vue';
import * as GlobalConfig from '../../../../globals/gloablConfig.js'
import * as LocalConfig from './resources/overviewConfig.js';

const username = "daniel.vollmer!";
const delay = (ms) => new Promise(resolve => setTimeout(resolve, ms));

onMounted(() => {
  writeUsernameInTypewriterStyle();
});


/**
 * Writes username in typewriter style (every letter in ms seconds).
 * @returns {Promise<void>}
 */
async function writeUsernameInTypewriterStyle() {
  for(let i = 0; i < username.length; i++) {
    document.querySelector(".welcomeName").innerHTML += username.charAt(i);
    await delay(LocalConfig.TYPEWRITTERSPEED);
  }
}

function addGoalkeeperClicked() {
  GlobalConfig.changeWindowToTargetRoute('AddGoalkeeperForm');
}



</script>

<template>
  <div class="__overview">
      <div class="welcome_area">
        <p class="welcomeMessage"> {{ LocalConfig.GREETING }}</p>
        <p class="welcomeName"></p>
        <h2>{{ LocalConfig.WELCOME_MESSAGE }} </h2>
      </div>
      <div class="wrapper">
        <button class="custom-button">{{ LocalConfig.TO_DASHBOARD }}</button>
        <button class="custom-button">{{ LocalConfig.ADD_EXERCISE }}</button>
        <button class="custom-button">{{ LocalConfig.ADD_SESSION }}</button>
        <button @click="addGoalkeeperClicked" class="custom-button">{{ LocalConfig.ADD_GOALKEEPER }}</button>
      </div>
  </div>
</template>

<style scoped>
.__overview {
  display: grid;
}
.welcome_area {
  place-self: center;
  padding: 12pt;
  display: inline-block;
  margin-bottom: auto;
}
.welcomeMessage {
  display: inline;
  font-size: var(--bigHeadingFontSize);
  font-weight: 200;
}
.welcomeName {
  display: inline;
  font-size: var(--bigHeadingFontSize);
  font-weight: 800;
  color: var(--avocadoGreen)
}
.wrapper {
  text-align: center;
  max-width: 80%;
  min-width: 80%;
  background: var(--lightGrey);
}
.custom-button {
  width: calc(50% - 20px);
  margin: 0px 20px 20px 0px;
  border: none;
  border-radius: 5px;
  padding: 30px;
}
.custom-button:hover {
  text-align: left;
  font-weight: bold;
}

</style>